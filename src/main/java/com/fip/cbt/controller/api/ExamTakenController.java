package com.fip.cbt.controller.api;

import com.fip.cbt.controller.request.ExamTakenRequest;
import com.fip.cbt.controller.request.UpdateExamTakenRequest;
import com.fip.cbt.model.ExamTaken;
import com.fip.cbt.service.ExamTakenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/exams/taken")
public class ExamTakenController {
    
    @Autowired
    ExamTakenService examTakenService;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExamTaken add(@Valid @RequestBody ExamTakenRequest examTakenRequest){
        return examTakenService.add(examTakenRequest);
    }
    
    @GetMapping
    public List<ExamTaken> getAll(){
        return examTakenService.getAll();
    }
    
    @GetMapping("/{examId}")
    public ExamTaken getOne(@PathVariable String examId){
        return examTakenService.getOne(examId);
    }
    
    @DeleteMapping("/{examId}")
    public void delete(@PathVariable String examId){
        examTakenService.delete(examId);
    }
    
    @PutMapping
    public void update(@Valid @RequestBody UpdateExamTakenRequest updateExamTakenRequest){
        examTakenService.update(updateExamTakenRequest);
    }
}
