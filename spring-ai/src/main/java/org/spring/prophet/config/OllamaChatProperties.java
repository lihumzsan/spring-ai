package org.spring.prophet.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.ai.ollama.chat")
public class OllamaChatProperties {
    private String model = "llama2";
    private String systemMessage = "只回答问题，不进行解释";
    private int retrieveSize = 100;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSystemMessage() {
        return systemMessage;
    }

    public void setSystemMessage(String systemMessage) {
        this.systemMessage = systemMessage;
    }

    public int getRetrieveSize() {
        return retrieveSize;
    }

    public void setRetrieveSize(int retrieveSize) {
        this.retrieveSize = retrieveSize;
    }
}