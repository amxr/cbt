package com.fip.cbt.service;

import com.fip.cbt.controller.request.TakenExamRequest;
import com.fip.cbt.model.TakenExam;

import java.util.List;

public interface TakenExamService {
    TakenExam submit(TakenExamRequest takenExamRequest);

    TakenExam getOne(String examNumber);

    void delete(Long id);

    //ExamTaken update(UpdateExamTakenRequest updateExamTakenRequest);

    List<TakenExam> getAll();
}
