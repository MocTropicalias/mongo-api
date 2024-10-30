package com.example.mongoapi.Services;

import com.example.mongoapi.Models.Comment;
import com.example.mongoapi.Models.Post;
import com.example.mongoapi.Repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PostService {

    MongoTemplate mongoTemplate;
    private final PostRepository repository;

    public PostService(PostRepository repository, MongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
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



    public Post buscarPost(String id){

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

    public int liked(String idPost, Long userId){
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


    public List<Post> searchPosts(String text, Long userId, Boolean following, Boolean liked) throws IOException {
        List<AggregationOperation> operations = new ArrayList<>();

        // Condição para o campo `text` (procura por qualquer texto se estiver vazio)
        if (text != null && !text.isEmpty()) {
            operations.add(Aggregation.match(Criteria.where("content").regex(text, "i"))); // "i" para case-insensitive
        }

        // Condição para o campo `userId` (procura por qualquer usuário dentro de likes se for nulo)
        if (liked) {
            operations.add(Aggregation.match(Criteria.where("likes").in(userId)));
        }

        // Condição para o campo `following` (procura por qualquer id de usuário se for vazio ou nulo)
        if (following) {

            List<Long> followingIds = new ArrayList<>();

            String url = "https://tropicalias-api-dev.onrender.com/follow/getAllfollowing/" + userId;
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if(conn.getResponseCode() == 200){
                followingIds = (List<Long>) (Object) conn.getContent();
            } else {
                throw new IOException(conn.getResponseMessage());
            }

            operations.add(Aggregation.match(Criteria.where("userId").in(followingIds)));
        }

        return repository.searchPosts(Aggregation.newAggregation(operations));
    }

}
