package com.fip.cbt.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Accessors(chain = true)
public class Question {
    @Id
    @Min(1)
    private int id;

    @NotBlank(message = "Question text cannot be blank!")
    private String text;

    @Min(1)
    private int point;

    private List<@NotBlank(message = "No blank options allowed!") String> options;

    @NotBlank(message = "Answer cannot be blank!")
    private String answer;
}
