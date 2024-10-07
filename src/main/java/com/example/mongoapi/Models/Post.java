package com.example.mongoapi.Models;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Post {

    private Long id;
    private Long userId;
    private String userPhoto;
    private String userName;
    private String media;
    private String title; // Em Java, o tipo `String` já aceita `null`, então não é necessário usar `Optional` aqui
    private String content;
    private int likes;
    private List<Comment> comments;

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
        sb.append(", likes=").append(likes);
        sb.append(", comments=").append(comments);
        sb.append('}');
        return sb.toString();
    }
}
