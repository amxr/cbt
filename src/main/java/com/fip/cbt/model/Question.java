package com.fip.cbt.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.OpenAPI31;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Accessors(chain = true)
public class Question {
    @Id
    @SequenceGenerator(name = "question_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "question_sequence")
    private long id;

    @ManyToOne
    @JoinColumn(name="exam_id", nullable=false, referencedColumnName = "id")
    @JsonIgnore
    private Exam exam;

    @NotBlank(message = "Question text cannot be blank!")
    @Column(columnDefinition = "TEXT")
    private String text;

    @Min(value = 1, message = "minimum value for point is 1")
    private double point;

    @NotEmpty(message = "Options cannot be empty!")
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(columnDefinition = "TEXT")
    private List<@NotBlank(message = "No blank options allowed!") String> options;

    @NotBlank(message = "Answer cannot be blank!")
    @Column(columnDefinition = "TEXT")
    private String answer;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Question question = (Question) o;
        return id > 0 && Objects.equals(id, question.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
