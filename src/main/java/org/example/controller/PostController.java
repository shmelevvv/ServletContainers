package org.example.controller;

import com.google.gson.Gson;
import org.example.exception.NotFoundException;
import org.example.model.Post;
import org.example.service.PostService;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

@Controller
public class PostController {
    public static final String APPLICATION_JSON = "application/json";
    private final PostService service;
    private final Gson gson = new Gson();

    public PostController(PostService service) {
        this.service = service;
    }

    public void all(HttpServletResponse response) throws IOException {
        final var data = service.all();
        Post[] posts = data.toArray(new Post[0]);
        makeSuccessResponse(response, posts);
    }

    public void getById(long id, HttpServletResponse response) throws IOException{
        try {
            final var data = service.getById(id);
            makeSuccessResponse(response, data);
        } catch (NotFoundException e) {
            makeFailResponse(response);
        }
    }

    public void save(Reader body, HttpServletResponse response) throws IOException {
        try {
            final var post = gson.fromJson(body, Post.class);
            final var data = service.save(post);
            makeSuccessResponse(response, data);
        } catch (NotFoundException e) {
            makeFailResponse(response);
        }
    }

    public void removeById(long id, HttpServletResponse response) throws IOException {
        service.removeById(id);
        makeSuccessResponse(response, null);
    }

    private void makeSuccessResponse(HttpServletResponse response, Post... posts) throws IOException {
        response.setContentType(APPLICATION_JSON);
        response.setStatus(200);
        response.getWriter().print(gson.toJson(posts));
    }

    private void makeFailResponse(HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON);
        response.setStatus(400);
    }

}