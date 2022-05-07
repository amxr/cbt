package com.fip.cbt.service;

import com.fip.cbt.controller.request.ExamRequest;
import com.fip.cbt.controller.request.UpdateExamRequest;
import com.fip.cbt.model.Exam;

import java.util.List;

public interface ExamService {
    Exam add(ExamRequest examRequest);

    Exam getOne(String testNumber);

    void delete(String testNumber);

    Exam update(UpdateExamRequest updateExamRequest);

    List<Exam> getAll();
}
