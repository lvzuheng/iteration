package com.lzh.iteration.service;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Entity;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Service
@Transactional
@Component
public class SqlDataManager {
	@Resource
	private SessionFactory sessionFactory;
	public Session Session() {
		return sessionFactory.getCurrentSession();
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public boolean saveOrUpdate(Object obj){
		try {
			Session().saveOrUpdate(obj);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	public boolean save(Object obj){
		try {
			Session().save(obj);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}


	public boolean merge(Object object){
		try {
			Session().merge(object);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
	public boolean update(Object object){
		try {
			Session().update(object);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
	public boolean update(String sql){
		try {
			Session().createQuery(sql).executeUpdate();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	public <T> List<T> search(Class<T> t){
		return Session().createQuery("FROM "+t.getSimpleName()).list();
	}
	public <T> List<T> search(String key,Object value,Class<T> t){
		return Session().createQuery("FROM "+t.getSimpleName()+" WHERE "+key+" = :value")
				.setParameter("value", value).list();
	}

	public List<Object> search(String sql) {
		return Session().createQuery(sql).list();
	}
	public List<Integer> searchReturnId(String sql) {
		return Session().createQuery(sql).list();
	}

	public Query searchList(String sql) {
		return Session().createQuery(sql);
	}
	
	public SQLQuery searchSqlList(String sql) {
		return Session().createSQLQuery(sql);
	}
	public <T> List<T> searchSqlList(String key,Collection<? extends Object> value,Class<T> t){
		return Session().createQuery("FROM "+t.getSimpleName()+" WHERE "+key+" in (:value)")
				.setParameterList("value", value).list();
	}

	public <T> void remove(T t){
		Session().delete(t);
	}
	public  void remove(String sql){
		Session().createQuery(sql).executeUpdate();
	}
}
