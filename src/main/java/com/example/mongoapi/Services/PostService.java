package com.example.mongoapi.Services;

import com.example.mongoapi.Models.Post;
import com.example.mongoapi.Repository.PostRepository;
import com.mongodb.lang.Nullable;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

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

    public ResponseEntity<?> excluirPost(Long id){
        Optional<Post> post = repository.findById(id);

        if(post.isPresent()){
            repository.deleteById(id);
        }
        else{
            return new ResponseEntity<>("Post não encontrado!",HttpStatus.NOT_FOUND);
        }

        post = repository.findById(id);

        if (post.isEmpty()){
            return new ResponseEntity<>("Post removido com sucesso!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Erro interno do servidor", HttpStatus.INTERNAL_SERVER_ERROR);

    }

}
