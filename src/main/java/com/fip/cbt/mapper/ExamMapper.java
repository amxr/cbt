package com.fip.cbt.mapper;

import com.fip.cbt.controller.request.ExamRequest;
import com.fip.cbt.controller.request.UpdateExamRequest;
import com.fip.cbt.model.Exam;

public class ExamMapper {
    public static Exam toExam(ExamRequest examRequest) {
        return new Exam()
                .setExamNumber(examRequest.getExamNumber())
                .setName(examRequest.getName())
                .setPassMark(examRequest.getPassMark())
                .setDescription(examRequest.getDescription())
                .setInstructions(examRequest.getInstructions())
                .setStart(examRequest.getStart())
                .setDuration(examRequest.getDuration())
                .setTimed(examRequest.isTimed())
                .setQuestions(examRequest.getQuestions());
    }

    public static Exam toExam(UpdateExamRequest examRequest) {
        return new Exam()
                .setId(examRequest.getId())
                .setExamNumber(examRequest.getExamNumber())
                .setName(examRequest.getName())
                .setPassMark(examRequest.getPassMark())
                .setDescription(examRequest.getDescription())
                .setInstructions(examRequest.getInstructions())
                .setStart(examRequest.getStart())
                .setDuration(examRequest.getDuration())
                .setTimed(examRequest.isTimed())
                .setQuestions(examRequest.getQuestions());
    }
}