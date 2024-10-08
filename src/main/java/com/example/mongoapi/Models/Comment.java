package com.example.mongoapi.Models;

import io.swagger.v3.oas.annotations.media.Schema;

public class Comment {
    @Schema(description = "Id do usuário que fez o comentário")
    private Long userId;
    @Schema(description = "Url da imagem do usuário")
    private String userPhoto;
    @Schema(description = "Nome do usuário")
    private String userName;
    @Schema(description = "texto do comentário")
    private String content;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
