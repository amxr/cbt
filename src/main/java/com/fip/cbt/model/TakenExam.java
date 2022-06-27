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
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TakenExam {
    @Id
    @SequenceGenerator(name = "taken_exam_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "taken_exam_sequence")
    private long id;

    @ManyToOne
    @JoinColumn(name = "exam_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"questions"})
    private Exam exam;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIncludeProperties({"id", "name", "email", "role"})
    private User candidate;

    @OneToMany(mappedBy = "takenExam", fetch = FetchType.EAGER)
    private Set<QuestionResponse> responses;

    private boolean isPassed;

    private double totalPoints;

    private LocalDateTime userStartTime;

    @CreationTimestamp
    private LocalDateTime submissionDate;

    @UpdateTimestamp
    private LocalDateTime lastModifiedOn;
}
