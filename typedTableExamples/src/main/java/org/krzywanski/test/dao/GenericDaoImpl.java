package org.krzywanski.test.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

public abstract class GenericDaoImpl<T extends Serializable, Id extends Serializable> implements GenericDao<T, Id>{
    private final SessionFactory sessionFactory;
    private final Class<T> persistentClass;

    public GenericDaoImpl(SessionFactory sessionFactory) {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.sessionFactory = sessionFactory;
    }

    @Override
    public T findById(Id id) {
        Session session = sessionFactory.getCurrentSession();
        return session.find(persistentClass, id);
    }

    @Override
    public T insert(T entity) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
        } catch (Exception x) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error while inserting data", x);
        }
        return entity;
    }

    @Override
    public void remove(T data) throws Exception {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            session.remove(data);
            transaction.commit();
        } catch (Exception x) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error while removing data", x);
        }
    }

    @Override
    public T update(T data) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            session.merge(data);
            transaction.commit();
        } catch (Exception x) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error while updating data", x);
        }
        return data;
    }
}