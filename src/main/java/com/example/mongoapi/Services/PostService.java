package com.example.mongoapi.Services;

import com.example.mongoapi.Models.Post;
import com.example.mongoapi.Repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PostService {

    private final PostRepository repository;

    public PostService(PostRepository repository){
        this.repository = repository;
    }

    public Post inserirPost(Post post){
        post.setCreatedAt(new Date());
        post.setDeletedAt(null);
        return repository.save(post);
    }

    public List<Post> buscarPosts(){
        return repository.findAll();
    }

    public Post buscarPost(Long id){
        return repository.findById(id);
    }

    public void excluirPost(Long id){
        repository.deleteById(id);
    }

    public List<Post> buscarPostsPorUsuario(Long userId){
        return repository.findByUserId(userId);
    }
}
