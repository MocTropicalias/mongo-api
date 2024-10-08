package com.example.mongoapi.Models;

import io.swagger.v3.oas.annotations.media.Schema;

public class Comment {
    @Schema(description = "Id do usuário que comentou o post")
    private Long userId;
    @Schema(description = "Url da imagem do usuário que comentou o post")
    private String userPhoto;
    @Schema(description = "Nome do usuário que comentou o post")
    private String userName;
    @Schema(description = "Conteúdo do comentário")
    private String content;
}
