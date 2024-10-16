package com.example.ruleengine.controllers;

// import org.hibernate.mapping.Map;
import java.util.Map;

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
    public Node combineRules(@RequestBody CombineRuleRequest request) {
        Node rule1 = ruleService.createRule(request.getRule1());
        Node rule2 = ruleService.createRule(request.getRule2());
        return ruleService.combineRules(rule1, rule2);
    }

    public static class CombineRuleRequest {
        private String rule1;
        private String rule2;

        public String getRule1() {
            return rule1;
        }

        public void setRule1(String rule1) {
            this.rule1 = rule1;
        }

        public String getRule2() {
            return rule2;
        }

        public void setRule2(String rule2) {
            this.rule2 = rule2;
        }
    }

    // Endpoint to evaluate a rule against user data
    @PostMapping("/evaluate")
    public ResponseEntity<?> evaluateRule(@RequestBody EvaluationRequest request) {
        try {
            Node rule = ruleService.createRule(request.getRule());
            boolean result = ruleService.evaluateRule(rule, request.getUserData());
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            // Return a 400 Bad Request with the error message
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DTO for evaluation request
    public static class EvaluationRequest {
        private String rule;
        private Map<String, Object> userData;

        public String getRule() {
            return rule;
        }

        public void setRule(String rule) {
            this.rule = rule;
        }

        public Map<String, Object> getUserData() {
            return userData;
        }

        public void setUserData(Map<String, Object> userData) {
            this.userData = userData;
        }
    }

    @PostMapping("/modify")
    public Node modifyRule(@RequestBody ModifyRequest request) {
        Node rule = ruleService.createRule(request.getRule());
        return ruleService.modifyRule(rule, request.getOldCondition(), request.getNewCondition());
    }

    // DTO for modify request
    public static class ModifyRequest {
        private String rule;
        private String oldCondition;
        private String newCondition;

        public String getRule() {
            return rule;
        }

        public void setRule(String rule) {
            this.rule = rule;
        }

        public String getOldCondition() {
            return oldCondition;
        }

        public void setOldCondition(String oldCondition) {
            this.oldCondition = oldCondition;
        }

        public String getNewCondition() {
            return newCondition;
        }

        public void setNewCondition(String newCondition) {
            this.newCondition = newCondition;
        }
    }
}
