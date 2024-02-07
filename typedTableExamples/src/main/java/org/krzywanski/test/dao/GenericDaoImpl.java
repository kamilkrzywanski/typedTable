package org.krzywanski.test.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.krzywanski.panel_v1.DataFlowController;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

public abstract class GenericDaoImpl<T extends Serializable, Id extends Serializable> implements GenericDao<T, Id>, DataFlowController<T> {
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
        Session session = sessionFactory.getCurrentSession();
        session.persist(entity);
        return entity;
    }

    @Override
    public void remove(T data) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(data);
    }

    @Override
    public T update(T data) {
        Session session = sessionFactory.getCurrentSession();
        data = session.merge(data);
        return data;
    }
}