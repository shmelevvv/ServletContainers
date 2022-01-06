package org.example.repository;

import org.example.model.Post;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.*;
import java.util.Optional;

@Repository
public class PostRepository {
    private static final Map<Long, Post> POSTS = new HashMap<>();
    private static Long COUNTER = Long.valueOf(0L);

    public synchronized List<Post> all() {
        return new ArrayList(POSTS.values());
    }

    public synchronized Optional<Post> getById(long id) {
        return Optional.ofNullable(POSTS.get(id));
    }

    public synchronized Optional<Post> save(Post post) {
        long postId = post.getId();
        if (postId == 0) {
            postId = ++COUNTER;
            post.setId(COUNTER);
            POSTS.put(postId, post);
        } else if (POSTS.get(postId) != null) {
            POSTS.put(postId, post);
        } else
            post = null;
        return Optional.ofNullable(post);
    }

    public synchronized void removeById(long id) {
        if (POSTS.get(id) != null) {
            POSTS.remove(id);
        }
    }
}