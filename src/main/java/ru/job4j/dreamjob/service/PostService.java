package ru.job4j.dreamjob.service;

import ru.job4j.dreamjob.store.PostStore;
import ru.job4j.dreamjob.store.model.Post;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PostService {
    private static final PostStore INST = PostStore.instOf();
   private final PostStore store = PostStore.instOf();
    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private final AtomicInteger id = new AtomicInteger(3);

    private PostService() {
    }

    public static PostStore instOf() {
        return INST;
    }

    public Collection<Post> findAll() {
        return store.findAll();
    }

    public void add(Post post) {
        post.setId(id.incrementAndGet());
        post.setCreated(new Date());
        posts.put(post.getId(), post);
    }

    public Post findById(int id) {
        return posts.get(id);
    }

    public void update(Post post) {
        post.setCreated(new Date());
        posts.replace(post.getId(), post);
    }
}