package com.example.mongoapi.Controllers;

import com.example.mongoapi.Models.Comment;
import com.example.mongoapi.Models.Post;
import com.example.mongoapi.Services.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/post")
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

    @GetMapping("/")
    @Operation(summary = "Buscar os posts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de posts retornada!",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class))
            ),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<List<Post>> buscarPosts(){

        return new ResponseEntity<>(service.buscarPosts(), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Buscar os posts de um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posts de um usuário retornados!",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class))
            ),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<List<Post>> buscarPostsPorUserId(@PathVariable("userId") Long userId){

        List<Post> posts = service.buscarPostsPorUsuario(userId);

        return new ResponseEntity<>(posts, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Buscar um post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post retornado!",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class))
            ),
            @ApiResponse(responseCode = "404", description = "Post não encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<?> buscarPost(@PathVariable("id") String id){

        Post post = service.buscarPost(id);
        if(post == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @GetMapping("/searchPosts")
    @Operation(summary = "Fazer uma pesquisa de posts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posts retornados!",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class))
            ),
            @ApiResponse(responseCode = "404", description = "Nenhum post encontrado! / Usuário não encontrado!", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<List<Post>> searchPosts(@RequestParam(value = "text", required = false, defaultValue = "") String text,
                                                  @RequestParam(value = "userId", required = false, defaultValue = "0") Long userId,
                                                  @RequestParam(value = "following", required = false, defaultValue = "false" ) Boolean following,
                                                  @RequestParam(value = "liked", required = false, defaultValue = "false" ) Boolean liked
    ) throws IOException {

        if(text.isEmpty() && !following && !liked){
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.BAD_REQUEST);
        }

        List<Post> response = service.searchPosts(text, userId, following, liked);
        if (response == null || response.isEmpty()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @PatchMapping("/{idPost}")
    @Operation(summary = "Adicionar um comentário ao post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comentário adicionado!", content = @Content)
            ,
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<?> adicionarComentario(@PathVariable("idPost") String idPost, @RequestBody Comment comment){

        service.adicionarComentario(idPost, comment);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PatchMapping("/{idPost}/{userId}")
    @Operation(summary = "Curtir um post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post curtido!", content = @Content)
            ,
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<?> liked(@PathVariable("idPost") String idPost, @PathVariable("userId") Long userId){

        service.liked(idPost, userId);
        return new ResponseEntity<>(service.buscarPost(idPost), HttpStatus.OK);
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

        service.excluirPost(id);
        return new ResponseEntity<>("Post excluído", HttpStatus.OK);

    }

    @DeleteMapping("/{idPost}/{commentPosition}")
    @Operation(summary = "Excluir um comentário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comentário excluído",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class))
            ),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<?> excluirComentario(@PathVariable("idPost") Long idPost, @PathVariable("commentPosition") int commentPosition){

        Post postAlterado = service.excluirComentario(idPost, commentPosition);
        return new ResponseEntity<>(postAlterado, HttpStatus.OK);
    }
}
