package com.example.ruleengine.models;

public class Node {
    private String type;   // "operator" (AND/OR) or "operand" (condition)
    private Node left;     // Left child for operator nodes
    private Node right;    // Right child for operator nodes
    private String value;  // Condition for operand nodes (e.g., "age > 30")
    private String operator; // Operator for operator nodes ("AND" or "OR")

    // Constructor for operator nodes
    public Node(String type, Node left, Node right, String operator) {
        this.type = type;
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    // Constructor for operand nodes
    public Node(String type, String value) {
        this.type = type;
        this.value = value;
    }

    // Getters and Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
