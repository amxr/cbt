package com.fip.cbt.dto.mapper;

import com.fip.cbt.controller.request.ExamTakenRequest;
import com.fip.cbt.model.ExamTaken;
import org.springframework.stereotype.Component;

//@Component
public class ExamTakenMapper {
    public static ExamTaken toExamTaken(ExamTakenRequest examTakenRequest){
        return new ExamTaken()
                .setResponses(examTakenRequest.getResponses())
                .setUserStartTime(examTakenRequest.getUserStartTime());
    }
//    public ExamTaken toExamTaken(UpdateExamTakenRequest updateExamTakenRequest){
//        return new ExamTaken()
//                .setId(updateExamTakenRequest.getId())
//                .setResponses(updateExamTakenRequest.getResponses());
//    }
}
