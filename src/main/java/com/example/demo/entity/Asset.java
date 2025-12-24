package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(
    name = "assets",
    uniqueConstraints = @UniqueConstraint(columnNames = "assetTag")
)
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank
    private String assetTag;

    @NotBlank
    private String assetName;

    @NotNull
    @Positive
    private Double purchaseCost;

    @NotNull
    private LocalDate purchaseDate;

    @Column(nullable = false)
    private String status = "ACTIVE";

    @ManyToOne(optional = false)
    private Vendor vendor;

    @ManyToOne(optional = false)
    private DepreciationRule depreciationRule;

    // getters & setters
}
