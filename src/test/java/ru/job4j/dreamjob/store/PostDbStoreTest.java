package ru.job4j.dreamjob.store;

import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.store.model.City;
import ru.job4j.dreamjob.store.model.Post;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PostDbStoreTest {
    @Test
    public void whenFindAllPost() {
        PostDbStore store = new PostDbStore(new Main().loadPool());
        Post post1 = new Post(0, "Java Job", "!!!");
        post1.setCity(new City(1, "Москва"));
        Post post2 = new Post(0, "No Java Job", "???");
        post2.setCity(new City(2, "Спб"));
        Post post3 = new Post(0, "Java No Job", "...");
        post3.setCity(new City(3, "Екб"));
        store.add(post1);
        store.add(post2);
        store.add(post3);
        Post postInDb1 = store.findById(post1.getId());
        Post postInDb2 = store.findById(post2.getId());
        Post postInDb3 = store.findById(post3.getId());
        assertThat(postInDb1.getName(), is(post1.getName()));
        assertThat(postInDb2.getDescription(), is(post2.getDescription()));
        assertThat(postInDb3.getCity(), is(post3.getCity()));
    }

    @Test
    public void whenAddPost() {
        PostDbStore store = new PostDbStore(new Main().loadPool());
        Post post = new Post(0, "Java Job", "!!!");
        post.setCity(new City(1, "Москва"));
        store.add(post);
        Post postInDb = store.findById(post.getId());
        assertThat(postInDb.getName(), is(post.getName()));
    }

    @Test
    public void whenUpdatePost() {
        PostDbStore store = new PostDbStore(new Main().loadPool());
        Post post = new Post(1, "Java Job", "!!!");
        post.setCity(new City(1, "Москва"));
        Post newPost = new Post(1, "Blow Job", "???");
        newPost.setCity(new City(3, "Екб"));
        store.add(post);
        Post postInDb = store.findById(post.getId());
        newPost.setId(postInDb.getId());
        store.update(newPost);
        postInDb = store.findById(post.getId());
        assertThat(postInDb.getName(), is(newPost.getName()));
    }
}