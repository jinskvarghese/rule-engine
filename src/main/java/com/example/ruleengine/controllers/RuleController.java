package com.example.ruleengine.controllers;

// import org.hibernate.mapping.Map;
import java.util.List;
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
    public Node combineRules(@RequestBody List<String> rules) {
        return ruleService.combineRules(rules);  // Pass the list of rule strings to the service
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

    public static class ModifyRequest {
        private String rule;          // Rule in string format (JSON or custom format)
        private String oldCondition;  // Old condition to be replaced
        private String newCondition;  // New condition to replace with

        // Getters and Setters
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

    @PostMapping("/modify")
    public Node modifyRule(@RequestBody ModifyRequest modifyRequest) {
        // Parse the rule string (e.g., "age > 30 AND department = 'Sales'") to create the root Node (AST)
        Node root = parseRuleToAST(modifyRequest.getRule());
    
        // Modify the rule AST
        return modifyRule(root, modifyRequest.getOldCondition(), modifyRequest.getNewCondition());
    }

    // Helper method to modify the AST
private Node modifyRule(Node root, String oldCondition, String newCondition) {
    if (root == null) {
        throw new IllegalArgumentException("Root node cannot be null.");
    }

    // Check if the node type is initialized
    if (root.getType() == null) {
        throw new IllegalStateException("Node type is not initialized.");
    }

    // If the current node is an operand and matches the old condition, replace it
    if (root.getType().equals("operand") && root.getValue().equals(oldCondition)) {
        root.setValue(newCondition);
        return root;
    }

    // Recursively check left and right nodes
    root.setLeft(modifyRule(root.getLeft(), oldCondition, newCondition));
    root.setRight(modifyRule(root.getRight(), oldCondition, newCondition));

    return root;
}
    
    private Node parseRuleToAST(String rule) {
        // Example parsing logic (you can enhance this to handle more complex rules)
        // This assumes the rule is simple, like "age > 30 AND department = 'Sales'"
        String[] parts = rule.split(" AND ");  // Simple case of AND operator
    
        // Create operand nodes for each condition
        Node leftOperand = validateAndCreateOperand(parts[0]);
        Node rightOperand = validateAndCreateOperand(parts[1]);
    
        // Return the root node for the rule
        return new Node("operator", leftOperand, rightOperand, "AND");
    }
    
    private Node validateAndCreateOperand(String condition) {
        // Your validation logic here, e.g., splitting the condition into parts
        String[] parts = condition.split(" ");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid condition format: " + condition);
        }
    
        String attribute = parts[0];
        String operator = parts[1];
        String value = parts[2];
    
        // Validate the operator
        if (!operator.matches(">|<|=")) {
            throw new IllegalArgumentException("Invalid operator in condition: " + operator);
        }
    
        return new Node("operand", condition);  // Create an operand node with the condition
    }
    

    // @GetMapping("/rules")
    // public List<String> getAllRules() {
    //     // Assuming you store the rules in a list or a database
    //     return ruleService.getAllRules();
    // }

}
