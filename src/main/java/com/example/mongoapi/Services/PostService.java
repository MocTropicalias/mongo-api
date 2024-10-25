package com.example.mongoapi.Services;

import com.example.mongoapi.Models.Comment;
import com.example.mongoapi.Models.Post;
import com.example.mongoapi.Repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<Post> buscarPostsPaginados(Pageable pageable) {
        return repository.findAll(pageable);
    }



    public Post buscarPost(Long id){

        Post post = repository.findById(id);

        for(Comment comment : post.getComments()) {
            if(comment.getDeletedAt() != null) {
                post.getComments().remove(comment);
            }
        }

        return post;
    }

    public void excluirPost(Long id){
        repository.deleteById(id);
    }

    public Page<Post> buscarPostsPaginadosPorUsuario(Pageable pageable, Long userId){
        return repository.findByUserId(pageable, userId);
    }

    public List<Post> buscarPostsPorUsuario(Long userId){
        return repository.findByUserId(userId);
    }

    public void adicionarComentario(Long idPost, Comment comment){
        repository.addComment(idPost, comment);
    }

    public int liked(Long idPost, Long userId){
        Post post = repository.findById(idPost);

        int retorno = 0;

        if(post.getLikes().contains(userId)){
            post.getLikes().remove(userId);
        } else {
            post.getLikes().add(userId);
            retorno = 1;
        }

        repository.alterLikes(post);

        return retorno;

    }

    public Post excluirComentario(Long idPost, int commentId){
        return repository.removeComment(idPost, commentId);
    }
}
