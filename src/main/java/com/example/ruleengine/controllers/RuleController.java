package com.example.ruleengine.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ruleengine.models.Node;
import com.example.ruleengine.services.RuleService;

@RestController
@RequestMapping("/api/rules")
public class RuleController {

    @Autowired
    private RuleService ruleService;

    // Endpoint to create a rule from a rule string
    @PostMapping("/create")
    public ResponseEntity<Node> createRule(@RequestBody String ruleString) {
        Node ast = ruleService.createRule(ruleString);
        return ResponseEntity.ok(ast);
    }

    // Endpoint to combine two rules
    @PostMapping("/combine")
    public ResponseEntity<Node> combineRules(@RequestBody Node rule1, @RequestBody Node rule2) {
        Node combinedAst = ruleService.combineRules(rule1, rule2);
        return ResponseEntity.ok(combinedAst);
    }

    // Endpoint to evaluate a rule against user data
    @PostMapping("/evaluate")
    public ResponseEntity<Boolean> evaluateRule(@RequestBody Node rule, @RequestBody Object userData) {
        boolean result = ruleService.evaluateRule(rule, userData);
        return ResponseEntity.ok(result);
    }
}
