package org.krzywanski.test.dao;

import org.hibernate.SessionFactory;
import org.krzywanski.test.dto.TestModelDto;
import org.krzywanski.test.model.TestModel;

public class TestModelDao extends GenericDaoImpl<TestModel, Integer>{
    public TestModelDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
