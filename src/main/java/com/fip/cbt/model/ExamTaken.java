package com.fip.cbt.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Document(collection = "examstaken")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Accessors(chain = true)
public class ExamTaken {
    @Id
    private String id;
    
    private String username;
    
    private String examId;
    
    private Set<QuestionResponse> responseSet;
    
    private boolean isPassed;
    
    private int totalPoints;
    
    private LocalDateTime submissionDate;
}
