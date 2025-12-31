package org.spring.prophet.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/ai/v1")
public class OllamaChatController {
    private final ChatClient chatClient;
    public OllamaChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/ollama/redis/chat")
    public String chat(@RequestParam String userId, @RequestParam String input) {
        log.info("/ollama/redis/chat  input:  [{}]", input);
        String text = chatClient.prompt()
                .user(input)
                .advisors(spec -> spec.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, userId)
                        .param(AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
                .call()
                .content();
        System.out.println( text);
        return text;
    }
}