package com.fip.cbt.mapper;

import com.fip.cbt.controller.request.TestRequest;
import com.fip.cbt.controller.request.UpdateTestRequest;
import com.fip.cbt.model.Test;

public class TestMapper {
    public static Test toTest(TestRequest testRequest){
        return new Test()
                .setTestNumber(testRequest.getTestNumber())
                .setName(testRequest.getName())
                .setPassMark(testRequest.getPassMark())
                .setDescription(testRequest.getDescription())
                .setInstructions(testRequest.getInstructions())
                .setStart(testRequest.getStart())
                .setDuration(testRequest.getDuration())
                .setTimed(testRequest.isTimed())
                .setQuestions(testRequest.getQuestions());
    }

    public static Test toTest(UpdateTestRequest testRequest){
        return new Test()
                .setId(testRequest.getId())
                .setTestNumber(testRequest.getTestNumber())
                .setName(testRequest.getName())
                .setPassMark(testRequest.getPassMark())
                .setDescription(testRequest.getDescription())
                .setInstructions(testRequest.getInstructions())
                .setStart(testRequest.getStart())
                .setDuration(testRequest.getDuration())
                .setTimed(testRequest.isTimed())
                .setQuestions(testRequest.getQuestions());
    }
}
