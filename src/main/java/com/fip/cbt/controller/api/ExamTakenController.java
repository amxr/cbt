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
    public List<ExamTaken> getAll(@RequestParam(required = false, defaultValue = "") String user, @RequestParam(required = false, defaultValue = "") String exam, @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails){
        return examTakenService.getAll(user.trim(), exam.trim(), userDetails);
    }
    
    @GetMapping("/{examId}")
    @Operation(summary = "This is used to get a taken exam be id")
    public ExamTaken getOne(@PathVariable String examId){
        return examTakenService.getOne(examId);
    }
    
    @DeleteMapping("/{examId}")
    @Operation(summary = "This is used to delete a taken exam by id")
    public void delete(@PathVariable String examId){
        examTakenService.delete(examId);
    }
    
//    @PutMapping
//    public void update(@Valid @RequestBody UpdateExamTakenRequest updateExamTakenRequest){
//        examTakenService.update(updateExamTakenRequest);
//    }
}
