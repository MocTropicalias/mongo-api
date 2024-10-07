package com.example.mongoapi.Controllers;

import com.example.mongoapi.Models.Post;
import com.example.mongoapi.Services.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class PostController {

    private final PostService service;


    public PostController(PostService service) {
        this.service = service;
    }


    @PostMapping("/")
    @Operation(summary = "Criar post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post criado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class))
            ),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<?> inserirPost(@RequestBody Post post){

        return new ResponseEntity<>(service.inserirPost(post), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post excluído",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class))
            ),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<?> excluirPost(@PathVariable Long id){

        return service.excluirPost(id);

    }

}
