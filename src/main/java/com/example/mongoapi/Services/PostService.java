package com.example.mongoapi.Services;

import com.example.mongoapi.Models.Post;
import com.example.mongoapi.Repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    private final PostRepository repository;

    public PostService(PostRepository repository){
        this.repository = repository;
    }

    public Post inserirPost(Post post){
        return repository.save(post);
    }

}
