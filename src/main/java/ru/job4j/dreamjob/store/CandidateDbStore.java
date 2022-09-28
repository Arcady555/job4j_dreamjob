package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.store.model.Candidate;
import ru.job4j.dreamjob.store.model.City;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CandidateDbStore {
    private final BasicDataSource pool;

    public CandidateDbStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Candidate> findAll() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM candidate")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    Candidate candidate =  sqlGetCandidate(it);
                    candidates.add(candidate);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candidates;
    }

    public Candidate add(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("INSERT INTO candidate(photo, name, visible, description,"
                     + " city_id, created) VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            sqlSetTable(ps, candidate);
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candidate;
    }

    public void update(Candidate candidate) {

        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("UPDATE candidate SET photo = ?, name = ?, visible = ?,"
                     + " description = ?, city_id = ?, created = ? WHERE id = ?;",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            sqlSetTable(ps, candidate);
            ps.setInt(7, candidate.getId());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Candidate findById(int id) {
        Candidate candidate = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM candidate WHERE id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    candidate = sqlGetCandidate(it);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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