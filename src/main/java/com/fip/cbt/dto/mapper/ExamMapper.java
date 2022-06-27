package com.fip.cbt.dto.mapper;

import com.fip.cbt.controller.request.ExamRequest;
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

    public static void updateExam(Exam exam, ExamRequest updateExamRequest) {
        exam
                .setExamNumber(updateExamRequest.getExamNumber())
                .setTimed(updateExamRequest.isTimed())
                .setName(updateExamRequest.getName())
                .setDescription(updateExamRequest.getDescription())
                .setDuration(updateExamRequest.getDuration())
                .setInstructions(updateExamRequest.getInstructions())
                .setPassMark(updateExamRequest.getPassMark())
                .setStart(updateExamRequest.getStart())
                .setOpen(updateExamRequest.isOpen());
    }
}
