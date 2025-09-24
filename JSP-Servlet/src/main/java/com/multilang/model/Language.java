package com.multilang.model;

public class Language {
    private String languageId;
    private String language;

    // Constructors
    public Language() {}

    public Language(String languageId, String language) {
        this.languageId = languageId;
        this.language = language;
    }

    // Getters and Setters
    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "Language{" +
                "languageId='" + languageId + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}