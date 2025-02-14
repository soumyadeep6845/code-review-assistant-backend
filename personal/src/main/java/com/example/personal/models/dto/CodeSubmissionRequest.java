package com.example.personal.models.dto;

public class CodeSubmissionRequest {
    private String userId;
    private String code;
    private String language;

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
        return "CodeSubmissionRequest{" +
                "userId='" + userId + '\'' +
                ", code='" + code + '\'' +
                ", language='" + language + '\'' +
                '}';
    }

    public CodeSubmissionRequest(String userId, String code, String language) {
        this.userId = userId;
        this.code = code;
        this.language = language;
    }
}
