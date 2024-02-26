package org.krzywanski.test.service;

import org.krzywanski.panel_v1.dataflow.DataFlowAdapter;
import org.krzywanski.test.Main;
import org.krzywanski.test.dao.TestModelDao;
import org.krzywanski.test.dto.TestModelDto;
import org.krzywanski.test.mapper.TestModelMapper;
import org.krzywanski.test.model.TestModel;

public class TestModelService implements DataFlowAdapter<TestModelDto> {
    TestModelDao testModelDao = new TestModelDao(Main.sessionFactory);
    @Override
    public void remove(TestModelDto data) throws Exception{
        TestModel testModel = TestModelMapper.mapTestModelDto(data, new TestModel());
        testModelDao.remove(testModel);
    }

    @Override
    public TestModelDto insert(TestModelDto data) throws Exception{
        TestModel testModel = TestModelMapper.mapTestModelDto(data, new TestModel());
        testModel = testModelDao.insert(testModel);

        return TestModelMapper.mapTestModelToDto(testModel, new TestModelDto());
    }

    @Override
    public TestModelDto update(TestModelDto data) throws Exception{
        TestModel testModel = TestModelMapper.mapTestModelDto(data, new TestModel());
        testModel = testModelDao.update(testModel);
        return TestModelMapper.mapTestModelToDto(testModel, new TestModelDto());
    }
}
