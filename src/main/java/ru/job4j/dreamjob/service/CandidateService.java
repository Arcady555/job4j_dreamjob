package ru.job4j.dreamjob.service;

import ru.job4j.dreamjob.store.CandidateStore;
import ru.job4j.dreamjob.store.model.Candidate;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class CandidateService {
    private static final CandidateStore INST = CandidateStore.instOf();
    private final CandidateStore store = CandidateStore.instOf();
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();
    private final AtomicInteger id = new AtomicInteger(1);

    private CandidateService() {
    }

    public static CandidateStore instOf() {
        return INST;
    }

    public Collection<Candidate> findAll() {
        return store.findAll();
    }

    public void add(Candidate candidate) {
        candidate.setId(id.incrementAndGet());
        candidate.setCreated(new Date());
        candidates.put(candidate.getId(), candidate);
    }

    public Candidate findById(int id) {
        return candidates.get(id);
    }

    public void update(Candidate candidate) {
        candidate.setCreated(new Date());
        candidates.replace(candidate.getId(), candidate);
    }
}