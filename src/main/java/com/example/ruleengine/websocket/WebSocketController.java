package com.example.ruleengine.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/createRule")
    @SendTo("/topic/ruleUpdates")
    public String sendCreateRuleUpdates(String message) throws Exception {
        return "New Rule Created: " + message;
    }

    @MessageMapping("/combineRules")
    @SendTo("/topic/ruleUpdates")
    public String sendCombineRuleUpdates(String message) throws Exception {
        return "Rules Combined: " + message;
    }

    @MessageMapping("/modifyRule")
    @SendTo("/topic/ruleUpdates")
    public String sendModifyRuleUpdates(String message) throws Exception {
        return "Rule Modified: " + message;
    }

    @MessageMapping("/evaluateRule")
    @SendTo("/topic/evaluationUpdates")
    public String sendEvaluationUpdates(String message) throws Exception {
        return "Rule Evaluation Result: " + message;
    }
}
