package com.stopworkorder.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "company_record")
@Getter
@Setter
public class CompanyRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String companyNumber;

    @Column(nullable = false)
    private LocalDate dateAdded;
} 