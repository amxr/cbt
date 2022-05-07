package com.fip.cbt.controller.api;

import com.fip.cbt.controller.request.ExamRequest;
import com.fip.cbt.controller.request.UpdateExamRequest;
import com.fip.cbt.model.Exam;
import com.fip.cbt.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/exam")
public class ExamController {
    @Autowired
    ExamService examService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Exam add(@Valid @RequestBody ExamRequest examRequest){
        return examService.add(examRequest);
    }

    @GetMapping
    public List<Exam> getAll(){
        return examService.getAll();
    }

    @GetMapping("/{examNumber}")
    @ResponseStatus(HttpStatus.FOUND)
    public Exam getOne(@PathVariable String examNumber){
        return examService.getOne(examNumber);
    }

    @DeleteMapping("/{examNumber}")
    public void delete(@PathVariable String examNumber){
        examService.delete(examNumber);
    }

    @PutMapping
    public Exam update(@Valid @RequestBody UpdateExamRequest updateExamRequest){
        return examService.update(updateExamRequest);
    }
}
