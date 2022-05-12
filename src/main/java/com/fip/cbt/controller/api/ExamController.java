package com.fip.cbt.controller.api;

import com.fip.cbt.controller.request.AddCandidatesRequest;
import com.fip.cbt.controller.request.ExamRequest;
import com.fip.cbt.controller.request.UpdateExamRequest;
import com.fip.cbt.model.Exam;
import com.fip.cbt.service.ExamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/exam")
@SecurityRequirement(name = "cbt")
public class ExamController {
    @Autowired
    ExamService examService;

    @Operation(summary = "This is used to add a new exam")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Exam add(@Valid @RequestBody ExamRequest examRequest){
        return examService.add(examRequest);
    }

    @Operation(summary = "This is used to get all exams added")
    @GetMapping
    public List<Exam> getAll(){
        return examService.getAll();
    }

    @Operation(summary = "This is used to find an exam by its unique number")
    @GetMapping("/{examNumber}")
    @ResponseStatus(HttpStatus.FOUND)
    public Exam getOne(@PathVariable String examNumber){
        return examService.getOne(examNumber);
    }

    @Operation(summary = "This is used to delete an exam using its unique number")
    @DeleteMapping("/{examNumber}")
    public void delete(@PathVariable String examNumber){
        examService.delete(examNumber);
    }

    @Operation(summary = "This is used to update an existing exam")
    @PutMapping
    public Exam update(@Valid @RequestBody UpdateExamRequest updateExamRequest){
        return examService.update(updateExamRequest);
    }

    @Operation(summary = "This is used to add candidates for the exam")
    @PatchMapping("/{examNumber}")
    public Exam addCandidates(@PathVariable String examNumber, @RequestBody AddCandidatesRequest addCandidatesRequest){
        return examService.addCandidates(examNumber, addCandidatesRequest);
    }
}
