package com.fip.cbt.dto.mapper;

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
                .setOpen(examRequest.isOpen())
                .setDuration(examRequest.getDuration())
                .setTimed(examRequest.isTimed());
    }

    public static Exam toExam(UpdateExamRequest examRequest) {
        return new Exam()
                //TODO: question dto
                .setId(examRequest.getId())
                .setExamNumber(examRequest.getExamNumber())
                .setName(examRequest.getName())
                .setPassMark(examRequest.getPassMark())
                .setDescription(examRequest.getDescription())
                .setInstructions(examRequest.getInstructions())
                .setStart(examRequest.getStart())
                .setOpen(examRequest.isOpen())
                .setDuration(examRequest.getDuration())
                .setTimed(examRequest.isTimed())
                .setQuestions(examRequest.getQuestions());
    }
}
