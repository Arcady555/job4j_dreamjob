package ru.job4j.dreamjob.store;

import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.store.model.Candidate;
import ru.job4j.dreamjob.store.model.City;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CandidateDbStoreTest {
    @Test
    public void whenFindAllCandidate() {
        CandidateDbStore store = new CandidateDbStore(new Main().loadPool());
        Candidate candidate1 = new Candidate(0, "Java Job", "!!!");
        candidate1.setCity(new City(1, "Москва"));
        Candidate candidate2 = new Candidate(0, "No Java Job", "???");
        candidate2.setCity(new City(2, "Спб"));
        Candidate candidate3 = new Candidate(0, "Java No Job", "...");
        candidate3.setCity(new City(3, "Екб"));
        store.add(candidate1);
        store.add(candidate2);
        store.add(candidate3);
        Candidate candidateInDb1 = store.findById(candidate1.getId());
        Candidate candidateInDb2 = store.findById(candidate2.getId());
        Candidate candidateInDb3 = store.findById(candidate3.getId());
        assertThat(candidateInDb1.getName(), is(candidate1.getName()));
        assertThat(candidateInDb2.getDescription(), is(candidate2.getDescription()));
        assertThat(candidateInDb3.getCity(), is(candidate3.getCity()));
    }

    @Test
    public void whenAddCandidate() {
        CandidateDbStore store = new CandidateDbStore(new Main().loadPool());
        Candidate candidate = new Candidate(0, "Java Job", "!!!");
        candidate.setCity(new City(1, "Москва"));
        store.add(candidate);
        Candidate candidateInDb = store.findById(candidate.getId());
        assertThat(candidateInDb.getName(), is(candidate.getName()));
    }

    @Test
    public void whenUpdateCandidate() {
        CandidateDbStore store = new CandidateDbStore(new Main().loadPool());
        Candidate candidate = new Candidate(1, "Java Job", "!!!");
        candidate.setCity(new City(1, "Москва"));
        Candidate newCandidate = new Candidate(1, "Blow Job", "???");
        newCandidate.setCity(new City(3, "Екб"));
        store.add(candidate);
        Candidate candidateInDb = store.findById(candidate.getId());
        newCandidate.setId(candidateInDb.getId());
        store.update(newCandidate);
        candidateInDb = store.findById(candidate.getId());
        assertThat(candidateInDb.getName(), is(newCandidate.getName()));
    }
}