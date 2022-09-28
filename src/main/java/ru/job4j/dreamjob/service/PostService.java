package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;

import ru.job4j.dreamjob.store.PostDbStore;
import ru.job4j.dreamjob.store.model.Post;

import java.util.Collection;

@Service
@ThreadSafe
public class PostService {
    private final PostDbStore postStore;
    private final CityService cityService;

    public PostService(PostDbStore postStore, CityService cityService) {
        this.postStore = postStore;
        this.cityService = cityService;
    }

    public Collection<Post> findAll() {
        Collection<Post> posts = postStore.findAll();
        posts.forEach(
                post -> post.setCity(
                        cityService.findById(post.getCity().getId())
                )
        );
        return posts;
    }

    public void add(Post post) {
        postStore.add(post);
    }

    public Post findById(int id) {
        return postStore.findById(id);
    }

    public void update(Post post) {
        postStore.update(post);
    }
}