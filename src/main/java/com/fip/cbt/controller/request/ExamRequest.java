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
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ExamRequest {

    @NotBlank(message = "Exam number cannot be blank!")
    @JsonProperty("exam_number")
    private String examNumber;

    @NotBlank(message = "Exam name cannot be blank!")
    private String name;

    @Min(1)
    @JsonProperty("pass_mark")
    private double passMark;

    @NotBlank(message = "Exam description cannot be blank!")
    private String description;

    @NotBlank(message = "Exam must have instructions!")
    private String instructions;

    private LocalDateTime start;

    @Min(300)
    private int duration;

    @NotNull
    @JsonProperty("timed")
    private boolean isTimed;

    @NotEmpty(message = "Questions cannot be empty!")
    private List<@Valid Question> questions;
}
