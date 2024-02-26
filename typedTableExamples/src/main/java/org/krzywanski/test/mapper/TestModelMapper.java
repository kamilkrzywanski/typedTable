package org.krzywanski.test.mapper;

import org.krzywanski.test.dto.TestModelDto;
import org.krzywanski.test.model.TestFormatClass;
import org.krzywanski.test.model.TestModel;

public class TestModelMapper {

    public static TestModelDto mapTestModelToDto(TestModel testModel, TestModelDto dto){
        dto.setId(testModel.getId());
        dto.setColumnA(testModel.getColumnA());
        dto.setColumnB(testModel.getColumnB());
        dto.setColumnC(testModel.getColumnC());
        dto.setDate(testModel.getMyDate());
        dto.setTestEnum(testModel.getTestEnum());
        dto.setBooleanValue(testModel.getBooleanValue());
        dto.setTestFormatClass(testModel.getTestFormatClass() != null ? new TestFormatClass(testModel.getTestFormatClass()) : null);
        return dto;
    }

    public static TestModel mapTestModelDto(TestModelDto dto, TestModel testModel){
        testModel.setId(dto.getId());
        testModel.setColumnA(dto.getColumnA());
        testModel.setColumnB(dto.getColumnB());
        testModel.setColumnC(dto.getColumnC());
        testModel.setMyDate(dto.getDate());
        testModel.setTestEnum(dto.getTestEnum());
        testModel.setBooleanValue(dto.getBooleanValue());
        testModel.setTestFormatClass(dto.getTestFormatClass() != null ? dto.getTestFormatClass().getValue() : null);
        return testModel;
    }

}
