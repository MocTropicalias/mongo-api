package com.example.mongoapi.Repository;


import com.example.mongoapi.Models.Comment;
import com.example.mongoapi.Models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostRepository {
    MongoTemplate mongoTemplate;
    public PostRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Post save(Post post) {
        mongoTemplate.save(post);
        return post;
    }

    public Page<Post> findAll(Pageable pageable) {

        Query query = new Query().with(pageable);
        query.addCriteria(Criteria.where("deletedAt").is(null));

        // Busca os posts da página específica
        List<Post> posts = mongoTemplate.find(query, Post.class);

        // Conta total de posts para saber o número total de páginas
        long total = mongoTemplate.count(new Query(), Post.class);

        // Retorna um Page<Post> contendo a lista paginada e os metadados
        return new PageImpl<>(posts, pageable, total);
    }

    public Post findById(Long id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query, Post.class);
    }

    public void deleteById(Long id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));

        Post post = mongoTemplate.findOne(query, Post.class);
        post.setDeletedAt(new java.util.Date());

        mongoTemplate.save(post);
    }

    public List<Post> findByUserId(Long userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        return mongoTemplate.find(query, Post.class);
    }

    public void addComment(Long idPost, Comment comment){
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(idPost));
        Post post = mongoTemplate.findOne(query, Post.class);
        post.getComments().add(comment);
        mongoTemplate.save(post);
    }

    public void alterLikes(Post post){
        mongoTemplate.save(post);
    }
}
