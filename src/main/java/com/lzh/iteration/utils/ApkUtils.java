package com.lzh.iteration.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.res.AXmlResourceParser;
import android.util.TypedValue;
import test.AXMLPrinter;



/**apk解析工具 */
public class ApkUtils {
	private InputStream inputStream ;
	private ZipFile zipFile ;
	private BufferedInputStream bufferedInputStream;
	
	public static ApkUtils ApkParse(String apkPath){
		ApkUtils apkUtils = new ApkUtils(apkPath);
		if(apkUtils.inputStream == null){
			return null;
		}
		return apkUtils;
	}
	private ApkUtils(String apkPath) {
		// TODO Auto-generated constructor stub
		try {
			inputStream = getXmlInputStream(apkPath);
			if(inputStream != null){
				bufferedInputStream = new BufferedInputStream(inputStream,inputStream.available());
				bufferedInputStream.mark(0);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private  InputStream getXmlInputStream(String apkPath) {

		try {
			zipFile = new ZipFile(apkPath);
			ZipEntry zipEntry = new ZipEntry("AndroidManifest.xml");
			inputStream = zipFile.getInputStream(zipEntry);
		} catch (IOException e) {
			System.err.println("error: "+ apkPath +"Not the correct format ");
		}
		return inputStream;
	}

	public List<String> parseAttrbute(String attr){
		List<String> valueList = new ArrayList<String>();
		try {

			AXmlResourceParser parser = new AXmlResourceParser();
			parser.open(bufferedInputStream);
			while (true) {
				int type = parser.next();
				if (type == XmlPullParser.END_DOCUMENT) {
					break;
				}

				if(type == XmlPullParser.START_TAG){
					for(int i = 0;i<parser.getAttributeCount();i++){
						if(parser.getAttributeName(i).equals(attr)){
							valueList.add(getAttributeValue(parser, i));
						}
					}
				}
			}

		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			bufferedInputStream.reset();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return valueList;
	}

	public String parseXML(){
		StringBuilder indent = new StringBuilder(10);
		try {
			AXmlResourceParser parser = new AXmlResourceParser();
			parser.open(inputStream);

			while (true) {
				int type = parser.next();
				if (type == XmlPullParser.END_DOCUMENT) {
					break;
				}
				switch (type) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					int namespaceCountBefore = parser.getNamespaceCount(parser.getDepth() - 1);
					int namespaceCount = parser.getNamespaceCount(parser.getDepth());
					for (int i = namespaceCountBefore; i != namespaceCount; ++i) {
						System.out.printf("%sxmlns:%s=\"%s\"",
								indent,
								parser.getNamespacePrefix(i),
								parser.getNamespaceUri(i));
						System.out.println();
					}
					for (int i = 0; i != parser.getAttributeCount(); ++i) {
						System.out.printf("%s%s%s=\"%s\"", 
								indent,
								getNamespacePrefix(parser.getAttributePrefix(i)),
								parser.getAttributeName(i),
								getAttributeValue(parser, i));
						System.out.println();
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				case XmlPullParser.TEXT:
					System.out.printf("%s%s", 
							indent,
							parser.getText());
					System.out.println();
				default:
					break;
				}
			}

		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return indent.toString();
	}


	private static String getNamespacePrefix(String prefix) {
		if (prefix == null || prefix.length() == 0) {
			return "";
		}
		return prefix + ":";
	}

	private static String getAttributeValue(AXmlResourceParser parser, int index) {
		int type = parser.getAttributeValueType(index);
		int data = parser.getAttributeValueData(index);
		if (type == TypedValue.TYPE_ATTRIBUTE) {
			return String.format("?%s%08X", getPackage(data), data);
		} 
		if (type == TypedValue.TYPE_INT_BOOLEAN) {
			return data != 0 ? "true" : "false";
		} 
		if (type == TypedValue.TYPE_DIMENSION) {
			return Float.toString(complexToFloat(data)) + DIMENSION_UNITS[data & TypedValue.COMPLEX_UNIT_MASK];
		} 
		if (type == TypedValue.TYPE_FLOAT) {
			return String.valueOf(Float.intBitsToFloat(data));
		} 
		if (type == TypedValue.TYPE_FRACTION) {
			return Float.toString(complexToFloat(data)) + FRACTION_UNITS[data & TypedValue.COMPLEX_UNIT_MASK];
		} 
		if (type == TypedValue.TYPE_INT_HEX) {
			return String.format("0x%08X", data);
		} 
		if (type == TypedValue.TYPE_REFERENCE) {
			return String.format("@%s%08X", getPackage(data), data);
		} 
		if (type == TypedValue.TYPE_STRING) {
			return parser.getAttributeValue(index);
		} 
		if (type >= TypedValue.TYPE_FIRST_COLOR_INT && type <= TypedValue.TYPE_LAST_COLOR_INT) {
			return String.format("#%08X", data);
		}
		if (type >= TypedValue.TYPE_FIRST_INT && type <= TypedValue.TYPE_LAST_INT) {
			return String.valueOf(data);
		}
		return String.format("<0x%X, type 0x%02X>", data, type);
	}

	private static String getPackage(int id) {
		if (id >>> 24 == 1) {
			return "android:";
		}
		return "";
	}

	public static float complexToFloat(int complex) {
		return (float) (complex & 0xFFFFFF00) * RADIX_MULTS[(complex >> 4) & 3];
	}

	private static final float RADIX_MULTS[] = {
			0.00390625F,3.051758E-005F,1.192093E-007F,4.656613E-010F
	};

	private static final String DIMENSION_UNITS[] = {
			"px","dip","sp","pt","in","mm","",""
	};

	private static final String FRACTION_UNITS[] = {
			"%","%p","","","","","",""
	};


	public void release() {
		try {
			inputStream.close();
			zipFile.close();
			bufferedInputStream.close();
			zipFile = null;
			bufferedInputStream = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}  