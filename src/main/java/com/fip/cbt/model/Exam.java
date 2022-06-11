package com.fip.cbt.model;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Exam {
    @Id
    @SequenceGenerator(name = "exam_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exam_sequence")
    private long id;

    @ManyToOne
    @JsonIncludeProperties({"id", "email", "name"})
    private User owner;

    @JsonProperty("exam_number")
    private String examNumber;

    private String name;

    @JsonProperty("pass_mark")
    private double passMark;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String instructions;

    private LocalDateTime start;

    @JsonProperty("registered_candidates")
    @JsonIncludeProperties({"id", "email", "name"})
    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    private Set<User> registeredCandidates;

    @JsonIncludeProperties({"id", "email", "name"})
    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    private Set<User> candidates;

    private int duration;

    @JsonProperty("timed")
    private boolean isTimed;

    @JsonProperty("open")
    private boolean isOpen;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<Question> questions;

    @CreationTimestamp
    private LocalDateTime created;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Exam exam = (Exam) o;
        return id > 0 && Objects.equals(id, exam.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
