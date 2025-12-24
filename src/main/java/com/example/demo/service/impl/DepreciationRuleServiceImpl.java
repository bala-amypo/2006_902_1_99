package com.example.demo.service.impl;

import com.example.demo.entity.DepreciationRule;
import com.example.demo.repository.DepreciationRuleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DepreciationRuleServiceImpl {

    private final DepreciationRuleRepository repository;

    public DepreciationRuleServiceImpl(DepreciationRuleRepository repository) {
        this.repository = repository;
    }

    public DepreciationRule createRule(DepreciationRule rule) {

        if (rule.getUsefulLifeYears() <= 0) {
            throw new IllegalArgumentException("Useful life must be > 0");
        }

        if (rule.getSalvageValue() < 0) {
            throw new IllegalArgumentException("Salvage value cannot be negative");
        }

        rule.setCreatedAt(LocalDateTime.now());
        return repository.save(rule);
    }
}
