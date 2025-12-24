package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
public class AssetLifecycleEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String eventType;

    @NotBlank
    private String eventDescription;

    @NotNull
    private LocalDate eventDate;

    @ManyToOne(optional = false)
    private Asset asset;

    // getters & setters
}
