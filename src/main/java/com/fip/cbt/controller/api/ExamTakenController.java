package com.fip.cbt.controller.api;

import com.fip.cbt.controller.request.ExamTakenRequest;
import com.fip.cbt.controller.request.UpdateExamTakenRequest;
import com.fip.cbt.model.ExamTaken;
import com.fip.cbt.service.ExamTakenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/exam/taken")
@SecurityRequirement(name = "cbt")
public class ExamTakenController {
    
    @Autowired
    ExamTakenService examTakenService;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "This is used to submit a taken exam")
    public ExamTaken submit(@Valid @RequestBody ExamTakenRequest examTakenRequest, @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails){
        return examTakenService.add(examTakenRequest, userDetails);
    }
    
    @GetMapping
    @Operation(summary = "This is used to get all taken exams (with certain parameters), (works differently based on user role)")
    public List<ExamTaken> getAll(@Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails){
        return examTakenService.getAll(userDetails);
    }
    
    @GetMapping("/{examId}")
    @Operation(summary = "This is used to get a taken exam be id")
    public ExamTaken getOne(@PathVariable String examId, @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails){
        return examTakenService.getOne(examId, userDetails);
    }
    
    @DeleteMapping("/{examId}")
    @Operation(summary = "This is used to delete a taken exam by id")
    public void delete(@PathVariable String examId, @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails){
        examTakenService.delete(examId, userDetails);
    }
    
//    @PutMapping
//    public void update(@Valid @RequestBody UpdateExamTakenRequest updateExamTakenRequest){
//        examTakenService.update(updateExamTakenRequest);
//    }
}
