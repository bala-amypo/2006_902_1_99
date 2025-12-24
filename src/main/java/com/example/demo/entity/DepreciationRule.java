package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(
    name = "depreciation_rules",
    uniqueConstraints = @UniqueConstraint(columnNames = "ruleName")
)
public class DepreciationRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String ruleName;

    @NotBlank
    private String method;

    @Positive
    private int usefulLifeYears;

    @PositiveOrZero
    private double salvageValue;

    // getters & setters
}
