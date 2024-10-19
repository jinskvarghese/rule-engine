package com.example.ruleengine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ruleengine.models.Rule;

@Repository
public interface RuleRepository extends JpaRepository<Rule, Long> {
    // Additional query methods if needed
}
