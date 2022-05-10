package com.fip.cbt.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@SuperBuilder
@Accessors(chain = true)
public class Question {
    
    @Id
    private String id;
    
    @NotBlank(message = "Question text cannot be blank!")
    private String text;

    @Min(value = 1, message = "minimum value for point is 1")
    private double point;

    @NotEmpty(message = "Options cannot be empty!")
    private List<@NotBlank(message = "No blank options allowed!") String> options;

    @NotBlank(message = "Answer cannot be blank!")
    private String answer;
}
