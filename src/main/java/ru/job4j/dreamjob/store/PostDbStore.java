package ru.job4j.dreamjob.store;

import net.jcip.annotations.ThreadSafe;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.store.model.City;
import ru.job4j.dreamjob.store.model.Post;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@ThreadSafe
public class PostDbStore {
    private static final Logger LOG = LogManager.getLogger(CandidateDbStore.class.getName());
    private static final String FIND_ALL = "SELECT * FROM post";
    private static final String ADD = "INSERT INTO post(name, visible, description,"
            + " city_id, created) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE post SET name = ?, visible = ?, description = ?,"
            + " city_id = ?, created = ? WHERE id = ?;";
    private static final String FIND_BY_ID = "SELECT * FROM post WHERE id = ?";

    private final BasicDataSource pool;

    public PostDbStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Post> findAll() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(FIND_ALL);
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    Post post = sqlGetPost(it);
                    posts.add(post);
                }
            }
        } catch (SQLException e) {
            LOG.error("SQLException", e);
        }
        return posts;
    }

    public Post add(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(ADD, PreparedStatement.RETURN_GENERATED_KEYS)) {
            sqlSetTable(ps, post);
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (SQLException e) {
            LOG.error("SQLException", e);
        }
        return post;
    }

    public void update(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(UPDATE, PreparedStatement.RETURN_GENERATED_KEYS)) {
            sqlSetTable(ps, post);
            ps.setInt(6, post.getId());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (SQLException e) {
            LOG.error("SQLException", e);
        }
    }

    public Post findById(int id) {
        Post post = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(FIND_BY_ID)
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    post = sqlGetPost(it);
                }
            }
        } catch (SQLException e) {
            LOG.error("SQLException", e);
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