package org.krzywanski.test.mapper;

import org.krzywanski.test.dto.TestModelDto;
import org.krzywanski.test.model.TestModel;

public class TestModelMapper {

    public static TestModelDto mapTestModelToDto(TestModel testModel, TestModelDto dto){
        dto.setColumnA(testModel.getColumnA());
        dto.setColumnB(testModel.getColumnB());
        dto.setColumnC(testModel.getColumnC());
        dto.setDate(testModel.getMyDate());
        dto.setTestEnum(testModel.getTestEnum());
        dto.setBooleanValue(testModel.getBooleanValue());
        return dto;
    }

}
