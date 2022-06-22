package com.fip.cbt.controller.api;

import com.fip.cbt.controller.request.ExamRequest;
import com.fip.cbt.model.Exam;
import com.fip.cbt.service.ExamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/exam")
@SecurityRequirement(name = "cbt")
public class ExamController {
    @Autowired
    ExamService examService;

    @Operation(summary = "This is used by testowner to add a new exam")
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
    public Exam getOne(@PathVariable String examNumber){
        return examService.getOne(examNumber);
    }

    @Operation(summary = "This is used by testowner to delete an exam using its unique number")
    @DeleteMapping("/{examNumber}")
    public void delete(@PathVariable String examNumber){
        examService.delete(examNumber);
    }

    @Operation(summary = "This is used by testowner to update an existing exam")
    @PutMapping("/update/{examNumber}")
    public Exam update(@PathVariable String examNumber, @Valid @RequestBody ExamRequest updateExamRequest){
        return examService.update(examNumber, updateExamRequest);
    }
    
    @Operation(summary = "This is used by testowner to approve candidates for the exam")
    @PatchMapping("/{examNumber}/candidates/approve")
    public Exam approveCandidates(@PathVariable String examNumber, @RequestBody Set<String> approvedCandidatesRequest){
        return examService.approveCandidates(examNumber, approvedCandidatesRequest);
    }
    
    @Operation(summary = "This is used by candidates to register for the exam")
    @PatchMapping("/{examNumber}/register")
    public Exam userRegistration(@PathVariable String examNumber){
        return examService.userRegistration(examNumber);
    }
}
