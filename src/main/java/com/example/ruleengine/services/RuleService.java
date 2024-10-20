package com.example.ruleengine.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ruleengine.models.Node;
import com.example.ruleengine.repositories.RuleRepository;
@Service
public class RuleService {

    @Autowired
    private RuleRepository ruleRepository;

// Method to create an AST from a rule string
public Node createRule(String ruleString) {
    if (ruleString == null || ruleString.isEmpty()) {
        throw new IllegalArgumentException("Rule string cannot be null or empty.");
    }
    
    ruleString = ruleString.trim().replaceAll("^\"|\"$", "");
    
    if (ruleString.contains("AND") || ruleString.contains("OR")) {
        return parseComplexRule(ruleString);  // Create AST for complex rules
    } else {
        return validateAndCreateOperand(ruleString);  // Create AST for simple operand
    }
}

// // Separate method to save the rule
// public Rule saveRule(String ruleString) {
//     Rule rule = new Rule(ruleString);
//     return ruleRepository.save(rule);  // Save Rule object to the database
// }

    private Node validateAndCreateOperand(String condition) {
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

        return new Node("operand", condition);
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

        // Return an operator node combining the two sides
        return new Node("operator", leftNode, rightNode, operator);
    }

public Node combineRules(List<String> ruleStrings) {
    if (ruleStrings == null || ruleStrings.isEmpty()) {
        throw new IllegalArgumentException("No rules provided.");
    }

    List<Node> nodes = new ArrayList<>();
    for (String ruleString : ruleStrings) {
        if (ruleString == null || ruleString.trim().isEmpty()) {
            throw new IllegalArgumentException("One of the rule strings is null or empty.");
        }
        nodes.add(createRule(ruleString));  // Create AST for each rule
    }

    // Combine all nodes using AND operator as an example
    Node combinedNode = nodes.get(0);
    for (int i = 1; i < nodes.size(); i++) {
        // Create a new operator node combining the previous nodes
        combinedNode = new Node("operator", combinedNode, nodes.get(i), "AND");  // Combine with AND operator
    }

    return combinedNode;  // Return the combined AST
}


    // Method to evaluate a rule against user data (for later)
    public boolean evaluateRule(Node rule, Map<String, Object> userData) {
        if (rule.getType().equals("operand")) {
            return evaluateCondition(rule.getValue(), userData);
        } else if (rule.getType().equals("operator")) {
            boolean leftResult = evaluateRule(rule.getLeft(), userData);
            boolean rightResult = evaluateRule(rule.getRight(), userData);

            if (rule.getOperator().equals("AND")) {
                return leftResult && rightResult;
            } else if (rule.getOperator().equals("OR")) {
                return leftResult || rightResult;
            }
        }
        return false; // Default case, if the rule is not valid
    }

    private boolean evaluateCondition(String condition, Map<String, Object> userData) {
        String[] parts = condition.split(" ");
        String attribute = parts[0];
        String operator = parts[1];
        String value = parts[2];

        // Ensure the attribute exists in the user data
        if (!userData.containsKey(attribute)) {
            throw new IllegalArgumentException("Missing required attribute: " + attribute);
        }

        Object userValue = userData.get(attribute);

        // Validate and compare the values
        switch (operator) {
            case ">":
                return (Integer) userValue > Integer.parseInt(value);
            case "<":
                return (Integer) userValue < Integer.parseInt(value);
            case "=":
                return userValue.toString().equals(value.replaceAll("'", ""));
            default:
                throw new IllegalArgumentException("Unsupported operator: " + operator);
        }
    }

    public Node modifyRule(Node root, String oldCondition, String newCondition) {
        if (root == null) return null;

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

    // public List<String> getAllRules() {
    //     // This method should return a list of rule strings or ASTs
    //     return ruleRepository.findAll(); // If stored in a repository or database
    // }
    
}
