package de.bsi.openai.chatgpt;


import org.springframework.stereotype.Component;

@Component
public class Prompt {
    private String request;

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
