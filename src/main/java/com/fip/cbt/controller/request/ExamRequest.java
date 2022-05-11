package com.fip.cbt.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fip.cbt.model.Question;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExamRequest {

    @NotBlank(message = "Exam number cannot be blank!")
    @JsonProperty("exam_number")
    private String examNumber;

    @NotBlank(message = "Exam name cannot be blank!")
    private String name;

    @Min(value = 1, message = "pass_mark cannot be less than 1")
    @JsonProperty("pass_mark")
    private double passMark;

    @NotBlank(message = "Exam description cannot be blank!")
    private String description;

    @NotBlank(message = "Exam must have instructions!")
    private String instructions;

    @NotNull(message = "start cannot be empty")
    private LocalDateTime start;

    @Min(value = 300, message = "duration must be atleast 300 seconds")
    private int duration;

    @NotNull(message = "timed cannot be empty")
    @JsonProperty("timed")
    private boolean isTimed;

    @NotEmpty(message = "Questions cannot be empty!")
    private List<@Valid Question> questions;
}
