package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.store.model.City;
import ru.job4j.dreamjob.store.model.Post;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PostDbStore {
    private final BasicDataSource pool;

    public PostDbStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Post> findAll() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM post")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    Post post = sqlGetPost(it);
                    posts.add(post);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    public Post add(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("INSERT INTO post(name, visible, description,"
                     + " city_id, created) VALUES (?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            sqlSetTable(ps, post);
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    public void update(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("UPDATE post SET name = ?, visible = ?, description = ?,"
                     + " city_id = ?, created = ? WHERE id = ?;", PreparedStatement.RETURN_GENERATED_KEYS)) {
            sqlSetTable(ps, post);
            ps.setInt(6, post.getId());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Post findById(int id) {
        Post post = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM post WHERE id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    post = sqlGetPost(it);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    private void sqlSetTable(PreparedStatement ps, Post post) throws SQLException {
        ps.setString(1, post.getName());
        ps.setBoolean(2, post.isVisible());
        ps.setString(3, post.getDescription());
        ps.setInt(4, post.getCity().getId());
        ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
    }

    private Post sqlGetPost(ResultSet it) throws SQLException {
        Post post = new Post(
                it.getInt("id"),
                it.getString("name"),
                it.getString("description"),
                it.getTimestamp("created").toLocalDateTime());
        post.setVisible(it.getBoolean("visible"));
        post.setCity(new City(it.getInt("city_id"), ""));
        return post;
    }
}