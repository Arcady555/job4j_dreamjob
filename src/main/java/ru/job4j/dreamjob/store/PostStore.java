package ru.job4j.dreamjob.store;

import ru.job4j.dreamjob.model.Post;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PostStore {

    private static final PostStore INST = new PostStore();
    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private Map<Integer, Post> newPosts = new ConcurrentHashMap<>();

    private PostStore() {
        posts.putAll(this.newPosts);
    }

    public static PostStore instOf() {
        return INST;
    }

    public Collection<Post> findAll() {
        return posts.values();
    }

    public void add(Post post) {
        newPosts.put(post.getId(), post);
    }
}