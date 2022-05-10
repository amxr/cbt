package com.fip.cbt.mapper;

import com.fip.cbt.controller.request.ExamTakenRequest;
import com.fip.cbt.controller.request.UpdateExamTakenRequest;
import com.fip.cbt.model.ExamTaken;

public class ExamTakenMapper {
    public static ExamTaken toExamTaken(ExamTakenRequest examTakenRequest){
        return new ExamTaken()
                .setUsername(examTakenRequest.getUsername())
                .setExamId(examTakenRequest.getExamId())
                .setResponseSet(examTakenRequest.getResponseSet())
                .setSubmissionDate(examTakenRequest.getSubmissionDate())
                .setTotalPoints(examTakenRequest.getTotalPoints())
                .setPassed(examTakenRequest.isPassed());
    }
    public static ExamTaken toExamTaken(UpdateExamTakenRequest updateExamTakenRequest){
        return new ExamTaken()
                .setId(updateExamTakenRequest.getId())
                .setUsername(updateExamTakenRequest.getUsername())
                .setExamId(updateExamTakenRequest.getExamId())
                .setResponseSet(updateExamTakenRequest.getResponseSet())
                .setSubmissionDate(updateExamTakenRequest.getSubmissionDate())
                .setTotalPoints(updateExamTakenRequest.getTotalPoints())
                .setPassed(updateExamTakenRequest.isPassed());
    }
}
