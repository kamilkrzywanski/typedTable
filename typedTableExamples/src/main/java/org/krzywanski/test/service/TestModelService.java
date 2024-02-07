package org.krzywanski.test.service;

import jakarta.persistence.EntityManager;
import org.krzywanski.panel_v1.DataFlowController;
import org.krzywanski.test.Main;
import org.krzywanski.test.dao.TestModelDao;
import org.krzywanski.test.dto.TestModelDto;
import org.krzywanski.test.mapper.TestModelMapper;
import org.krzywanski.test.model.TestModel;

public class TestModelService implements DataFlowController<TestModelDto> {
    TestModelDao testModelDao = new TestModelDao(Main.sessionFactory);

    @Override
    public void remove(TestModelDto data) {
        TestModel testModel = TestModelMapper.mapTestModelDto(data, new TestModel());
        testModelDao.remove(testModel);
    }

    @Override
    public TestModelDto insert(TestModelDto data) {
        TestModel testModel = TestModelMapper.mapTestModelDto(data, new TestModel());
        testModel = testModelDao.insert(testModel);

        return TestModelMapper.mapTestModelToDto(testModel, new TestModelDto());
    }

    @Override
    public TestModelDto update(TestModelDto data) {
        TestModel testModel = TestModelMapper.mapTestModelDto(data, new TestModel());
        testModel = testModelDao.update(testModel);
        return TestModelMapper.mapTestModelToDto(testModel, new TestModelDto());
    }
}
