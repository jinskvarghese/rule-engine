package com.example.ruleengine.services;

import com.example.ruleengine.models.Node;
import org.springframework.stereotype.Service;

@Service
public class RuleService {

    // Method to create an AST from a rule string
    public Node createRule(String ruleString) {
        // Trim spaces from the input rule
        ruleString = ruleString.trim();

        // If it's a simple condition, return an operand node (e.g., "age > 30")
        if (ruleString.contains("AND") || ruleString.contains("OR")) {
            // Parse complex rule with AND/OR operators
            return parseComplexRule(ruleString);
        } else {
            // It's a simple operand rule
            return new Node("operand", ruleString);
        }
    }

    // Helper function to parse rules with AND/OR operators
    private Node parseComplexRule(String ruleString) {
        String operator;
        if (ruleString.contains("AND")) {
            operator = "AND";
        } else {
            operator = "OR";
        }

        // Split the rule by the operator
        String[] parts = ruleString.split(operator);

        // Recursively parse the left and right sides of the rule
        Node leftNode = createRule(parts[0].trim());
        Node rightNode = createRule(parts[1].trim());

        // Return the operator node combining the two sides
        return new Node("operator", leftNode, rightNode, operator);
    }

    // Method to combine multiple rules into a single AST
    public Node combineRules(Node rule1, Node rule2) {
        return new Node("operator", rule1, rule2, "AND"); // Combines rule1 and rule2 using AND
    }

    // Method to evaluate a rule against user data
    public boolean evaluateRule(Node rule, Object userData) {
        // TODO: Add logic to evaluate rule against userData
        return true; // Dummy implementation for now
    }
}
