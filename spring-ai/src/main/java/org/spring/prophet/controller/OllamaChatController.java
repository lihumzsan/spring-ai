package org.spring.prophet.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/ai/v1")
public class OllamaChatController {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    public OllamaChatController(ChatClient.Builder builder, ChatMemory chatMemory) {
        this.chatClient = builder
                .defaultSystem("只回答问题，不进行解释")
                .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory))
                .build();
        this.chatMemory = chatMemory;
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
        return text.split("</think>")[1].trim();
    }
}