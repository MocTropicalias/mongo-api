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

import java.util.List;

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

        service.excluirPost(id);
        return new ResponseEntity<>("Post excluído", HttpStatus.OK);

    }

    @GetMapping("/")
    @Operation(summary = "Buscar os posts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de posts retornada!",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class))
            ),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<Page<Post>> buscarPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postsPage = service.buscarPostsPaginados(pageable);

        return new ResponseEntity<>(postsPage, HttpStatus.OK);
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
    public ResponseEntity<?> buscarPost(@PathVariable("id") Long id){

        Post post = service.buscarPost(id);
        if(post == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @PatchMapping("/{idPost}")
    @Operation(summary = "Adicionar um comentário ao post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comentário adicionado!", content = @Content)
            ,
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<?> adicionarComentario(@PathVariable("idPost") Long idPost, @RequestBody Comment comment){

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
    public ResponseEntity<?> liked(@PathVariable("idPost") Long idPost, @PathVariable("userId") Long userId){

        service.liked(idPost, userId);
        return new ResponseEntity<>(service.buscarPost(idPost), HttpStatus.OK);
    }
}
