package com.fip.cbt.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;

@Document(collection = "candidates")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Accessors(chain = true)
public class Candidate {

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    @DBRef
    private List<Role> roles;

//    @ManyToMany
//    @JoinTable(
//            name="test",
//            joinColumns = @JoinColumn(name="test_id")
//    )
    @DBRef
    private Set<Test> testsTaken;
}
