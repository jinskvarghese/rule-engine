package com.example.ruleengine.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/createRule")
    @SendTo("/topic/ruleUpdates")
    public String sendRuleUpdates(String message) throws Exception {
        return "New Rule Created: " + message;
    }

    @MessageMapping("/evaluateRule")
    @SendTo("/topic/evaluationUpdates")
    public String sendEvaluationUpdates(String message) throws Exception {
        return "Rule Evaluation Result: " + message;
    }
}
