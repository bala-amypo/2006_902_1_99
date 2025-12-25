package com.example.demo.service.impl;

import com.example.demo.entity.DepreciationRule;
import com.example.demo.repository.DepreciationRuleRepository;
import com.example.demo.service.DepreciationRuleService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class DepreciationRuleServiceImpl implements DepreciationRuleService {

    private final DepreciationRuleRepository repo;

    public DepreciationRuleServiceImpl(DepreciationRuleRepository repo) {
        this.repo = repo;
    }

    @Override
    public DepreciationRule createRule(DepreciationRule rule) {
        if (rule.getUsefulLifeYears() <= 0) {
            throw new IllegalArgumentException("Invalid life");
        }
        return repo.save(rule);
    }

    @Override
    public List<DepreciationRule> getAllRules() {
        return repo.findAll();
    }
}
