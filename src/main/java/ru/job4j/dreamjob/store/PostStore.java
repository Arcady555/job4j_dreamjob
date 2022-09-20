package ru.job4j.dreamjob.store;

import ru.job4j.dreamjob.model.Post;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PostStore {
    private static final PostStore INST = new PostStore();
    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private final AtomicInteger id = new AtomicInteger(3);

    private PostStore() {
        posts.put(1, new Post(1, "Junior Java Job", "no bad job!",
                new Date(2022, Calendar.SEPTEMBER, 10)));
        posts.put(2, new Post(2, "Middle Java Job", "good job!",
                new Date(2022, Calendar.SEPTEMBER, 11)));
        posts.put(3, new Post(3, "Senior Java Job", "excellent job!",
                new Date(2022, Calendar.SEPTEMBER, 1)));
    }

    public static PostStore instOf() {
        return INST;
    }

    public Collection<Post> findAll() {
        return posts.values();
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
        posts.put(post.getId(), post);
    }
}