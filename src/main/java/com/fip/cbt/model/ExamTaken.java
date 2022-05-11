package com.fip.cbt.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "examstaken")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ExamTaken {
    @Id
    private String id;

    @DBRef
    @JsonIgnoreProperties({"questions"})
    private Exam exam;

    @DBRef
    @JsonIncludeProperties({"id", "name", "email", "role"})
    private User user;
    
    private List<QuestionResponse> responses;
    
    private boolean isPassed;
    
    private double totalPoints;

    private LocalDateTime userStartTime;

    @CreatedDate
    private LocalDateTime submissionDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedOn;
}
