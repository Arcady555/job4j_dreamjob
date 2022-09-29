package ru.job4j.dreamjob.store;

import net.jcip.annotations.ThreadSafe;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.store.model.Candidate;
import ru.job4j.dreamjob.store.model.City;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@ThreadSafe
public class CandidateDbStore {
    private static final Logger LOG = LogManager.getLogger(CandidateDbStore.class.getName());
    private static final String FIND_ALL = "SELECT * FROM candidate";
    private static final String ADD = "INSERT INTO candidate(photo, name, visible, description,"
            + " city_id, created) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE candidate SET photo = ?, name = ?, visible = ?,"
            + " description = ?, city_id = ?, created = ? WHERE id = ?;";
    private static final String FIND_BY_ID = "SELECT * FROM candidate WHERE id = ?";

    private final BasicDataSource pool;

    public CandidateDbStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Candidate> findAll() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(FIND_ALL)
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    Candidate candidate =  sqlGetCandidate(it);
                    candidates.add(candidate);
                }
            }
        } catch (SQLException e) {
            LOG.error("SQLException", e);
        }
        return candidates;
    }

    public Candidate add(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(ADD, PreparedStatement.RETURN_GENERATED_KEYS)) {
            sqlSetTable(ps, candidate);
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (SQLException e) {
            LOG.error("SQLException", e);
        }
        return candidate;
    }

    public void update(Candidate candidate) {

        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(UPDATE, PreparedStatement.RETURN_GENERATED_KEYS)) {
            sqlSetTable(ps, candidate);
            ps.setInt(7, candidate.getId());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (SQLException e) {
            LOG.error("SQLException", e);
        }
    }

    public Candidate findById(int id) {
        Candidate candidate = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(FIND_BY_ID)
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    candidate = sqlGetCandidate(it);
                }
            }
        } catch (SQLException e) {
            LOG.error("SQLException", e);
        }
        return candidate;
    }

    private void sqlSetTable(PreparedStatement ps, Candidate candidate) throws SQLException {
        ps.setBytes(1, candidate.getPhoto());
        ps.setString(2, candidate.getName());
        ps.setBoolean(3, candidate.isVisible());
        ps.setString(4, candidate.getDescription());
        ps.setInt(5, candidate.getCity().getId());
        ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
    }

    private Candidate sqlGetCandidate(ResultSet it) throws SQLException {
        Candidate candidate = new Candidate(
                it.getInt("id"),
                it.getString("name"),
                it.getString("description"),
                it.getTimestamp("created").toLocalDateTime());
        candidate.setPhoto(it.getBytes("photo"));
        candidate.setVisible(it.getBoolean("visible"));
        candidate.setCity(new City(it.getInt("city_id"), ""));
        return candidate;
    }
}