package com.fip.cbt.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class TestRequest {

    @NotBlank(message = "Exam number cannot be blank!")
    private String examNumber;

    @NotBlank(message = "Exam name cannot be blank!")
    private String name;

    @Min(1)
    private int passMark;

    @NotBlank(message = "Exam description cannot be blank!")
    private String description;

    @NotBlank(message = "Exam must have instructions!")
    private String instructions;

    @Future
    private LocalDateTime start;

    @Min(300)
    private int duration;

    @NotNull
    private boolean isTimed;

    private List<@Valid Question> questions;
}
