package com.project.ottshareservice.domain;

public enum ContentType {

    VIDEO("영상"), MUSIC("음악"), GAME("게임"), ETC("기타");

    private final String description;

    public String getDescription() {
        return description;
    }

    ContentType(String description) {
        this.description = description;
    }
}
