package com.fip.cbt.mapper;

import com.fip.cbt.controller.request.TestTakenRequest;
import com.fip.cbt.controller.request.UpdateTestTakenRequest;
import com.fip.cbt.model.TestTaken;

public class TestTakenMapper {
    public static TestTaken toTestTaken(TestTakenRequest testTakenRequest){
        return new TestTaken()
                .setUsername(testTakenRequest.getUsername())
                .setTestId(testTakenRequest.getTestId())
                .setResponseSet(testTakenRequest.getResponseSet())
                .setSubmissionDate(testTakenRequest.getSubmissionDate())
                .setTotalPoints(testTakenRequest.getTotalPoints())
                .setPassed(testTakenRequest.isPassed());
    }
    public static TestTaken toTestTaken(UpdateTestTakenRequest updateTestTakenRequest){
        return new TestTaken()
                .setId(updateTestTakenRequest.getId())
                .setUsername(updateTestTakenRequest.getUsername())
                .setTestId(updateTestTakenRequest.getTestId())
                .setResponseSet(updateTestTakenRequest.getResponseSet())
                .setSubmissionDate(updateTestTakenRequest.getSubmissionDate())
                .setTotalPoints(updateTestTakenRequest.getTotalPoints())
                .setPassed(updateTestTakenRequest.isPassed());
    }
}
