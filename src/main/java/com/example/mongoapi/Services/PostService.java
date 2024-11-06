package com.example.mongoapi.Services;

import com.example.mongoapi.Models.Comment;
import com.example.mongoapi.Models.Post;
import com.example.mongoapi.Repository.PostRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
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

    public void adicionarComentario(String idPost, Comment comment){
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

        // Condição para o campo `liked` (procura por posts que o usuário tenha curtido)
        if (liked != null && liked) {
            operations.add(Aggregation.match(Criteria.where("likes").is(userId)));
        }

        // Condição para o campo `following` (procura por posts de usuários seguidos pelo usuário atual)
        if (following != null && following) {
            List<Long> followingIds = new ArrayList<>();

            // Faz uma chamada para obter a lista de seguidores
            String url = "https://tropicalias-api-dev.onrender.com/follow/getAllfollowing/" + userId;
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == 200) {
                // Leitura da resposta JSON e conversão para uma lista de Long
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    ObjectMapper mapper = new ObjectMapper();
                    followingIds = mapper.readValue(reader, new TypeReference<List<Long>>() {});
                }
            } else {
                throw new IOException("Failed to fetch following IDs: " + conn.getResponseMessage());
            }

            System.out.println(followingIds);
            operations.add(Aggregation.match(Criteria.where("userId").in(followingIds)));
        }

        // Executa a agregação e retorna os resultados
        return repository.searchPosts(Aggregation.newAggregation(operations));
    }
}
