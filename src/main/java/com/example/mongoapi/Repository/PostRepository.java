package com.example.mongoapi.Repository;


import com.example.mongoapi.Models.Post;
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

    public List<Post> findAll() {
        return mongoTemplate.findAll(Post.class);
    }

    public Post findById(Long id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query, Post.class);
    }

    public void deleteById(Long id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        mongoTemplate.remove(query, Post.class);
    }
}
