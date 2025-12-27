package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class DepreciationRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String ruleName;

    private String method;
    private Integer usefulLifeYears;
    private Double salvageValue;

    private LocalDateTime createdAt;

    public Long getId() { return id; }

    public String getRuleName() { return ruleName; }

    public void setRuleName(String ruleName) { this.ruleName = ruleName; }

    public String getMethod() { return method; }

    public void setMethod(String method) { this.method = method; }

    public Integer getUsefulLifeYears() { return usefulLifeYears; }

    public void setUsefulLifeYears(Integer usefulLifeYears) {
        this.usefulLifeYears = usefulLifeYears;
    }

    public Double getSalvageValue() { return salvageValue; }

    public void setSalvageValue(Double salvageValue) {
        this.salvageValue = salvageValue;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
