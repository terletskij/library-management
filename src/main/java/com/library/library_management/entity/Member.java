package com.library.library_management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @Column(nullable = false, updatable = false)
    private LocalDate membershipDate;

    public Member() {}

    public Member(String name) {
        this.name = name;
        membershipDate = LocalDate.now();
    }


    @PrePersist
    protected void onCreate() {
        if (this.membershipDate == null) {
            this.membershipDate = LocalDate.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getMembershipDate() {
        return membershipDate;
    }

}
