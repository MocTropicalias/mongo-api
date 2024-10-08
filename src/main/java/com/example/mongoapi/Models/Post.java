package com.example.mongoapi.Models;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document
public class Post {

    @Schema(description = "Id do post")
    private Long id;
    @Schema(description = "Id do usuario")
    private Long userId;
    @Schema(description = "Url da imagem do usuario")
    private String userPhoto;
    @Schema(description = "Nome do usuario")
    private String userName;
    @Schema(description = "Url da imagem do post")
    private String media;
    @Schema(description = "Título do post")
    private String title;
    @Schema(description = "Conteúdo do post")
    private String content;
    @Schema(description = "Lista de usuários que curtiram o post")
    private List<Long> likes;
    private List<Comment> comments;
    private Date createdAt;
    private Date deletedAt;

    public Post(Long id, Long userId, String userPhoto, String userName, String media, String title, String content, List<Comment> comments, Date createdAt, Date deletedAt) {
        this.id = id;
        this.userId = userId;
        this.userPhoto = userPhoto;
        this.userName = userName;
        this.media = media;
        this.title = title;
        this.content = content;
        this.comments = comments;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;

        this.likes = new java.util.ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public List<Long> getLikes() {
        return likes;
    }

    public void setLikes(List<Long> likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Post{");
        sb.append("id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", userPhoto='").append(userPhoto).append('\'');
        sb.append(", userName='").append(userName).append('\'');
        sb.append(", media='").append(media).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", content='").append(content).append('\'');
        sb.append(", likes=").append(likes.size());
        sb.append(", listLiked=").append(likes);
        sb.append(", comments=").append(comments);
        sb.append(", createdAt=").append(createdAt);
        sb.append('}');
        return sb.toString();
    }
}
