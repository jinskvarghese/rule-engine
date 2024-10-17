package com.example.ruleengine.services;

import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.ruleengine.models.Node;



@Service
public class RuleEvaluationService {

    @Cacheable(value = "ruleEvaluations", key = "#data")
    public boolean evaluateRule(Node ruleAST, Map<String, Object> data) {
        // The logic to evaluate the rule against the data
        // If called with the same data, this will return the cached result
        return evaluateAST(ruleAST, data);
    }

    private boolean evaluateAST(Node ruleAST, Map<String, Object> data) {
        // Perform evaluation
        return true;  // Placeholder
    }
}
