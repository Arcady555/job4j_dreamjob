package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.store.CandidateDbStore;
import ru.job4j.dreamjob.store.CandidateStore;
import ru.job4j.dreamjob.store.model.Candidate;
import ru.job4j.dreamjob.store.model.Post;

import java.util.Collection;

@Service
@ThreadSafe
public class CandidateService {
    private final CandidateDbStore candidateStore;
    private final CityService cityService;

    public CandidateService(CandidateDbStore candidateStore, CityService cityService) {
        this.candidateStore = candidateStore;
        this.cityService = cityService;
    }

    public Collection<Candidate> findAll() {
        Collection<Candidate> candidates = candidateStore.findAll();
        candidates.forEach(
                candidate -> candidate.setCity(
                        cityService.findById(candidate.getCity().getId())
                )
        );
        return candidates;
    }

    public void add(Candidate candidate) {
        candidateStore.add(candidate);
    }

    public Candidate findById(int id) {
        return candidateStore.findById(id);
    }

    public void update(Candidate candidate) {
        candidateStore.update(candidate);
    }
}