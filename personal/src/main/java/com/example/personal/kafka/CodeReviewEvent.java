package com.example.personal.kafka;

public class CodeReviewEvent {

    private String userId;
    private String code;
    private String language;

    public CodeReviewEvent() {
    }

    public CodeReviewEvent(String userId, String code, String language) {
        this.userId = userId;
        this.code = code;
        this.language = language;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "CodeReviewEvent{" +
                "userId='" + userId + '\'' +
                ", code='" + code + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}