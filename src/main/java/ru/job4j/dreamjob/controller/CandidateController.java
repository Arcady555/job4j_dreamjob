package ru.job4j.dreamjob.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.dreamjob.service.CandidateService;
import ru.job4j.dreamjob.service.CityService;
import ru.job4j.dreamjob.store.model.Candidate;

@Controller
@ThreadSafe
public class CandidateController {

    private final CandidateService candidateService;
    private final CityService cityService;

    public CandidateController(CandidateService candidateService, CityService cityService) {
        this.candidateService = candidateService;
        this.cityService = cityService;
    }

    @GetMapping("/candidates")
    public String candidates(Model model) {
        model.addAttribute("candidates", candidateService.findAll());
        return "candidates";
    }

    @GetMapping("/formAddCandidate")
    public String addCandidateGet(Model model) {
        model.addAttribute("candidate", new Candidate(0, "Заполните поле", "Заполните поле"));
        model.addAttribute("cities", cityService.getAllCities());
        return "addCandidate";
    }

    @PostMapping("/formAddCandidate")
    public String addCandidatePost(@ModelAttribute Candidate candidate) {
        candidate.getCity().setName(cityService.findById(candidate.getCity().getId()).getName());
        candidateService.add(candidate);
        return "redirect:/candidates";
    }

    @GetMapping("/formUpdateCandidate/{candidateId}")
    public String updateCandidateGet(Model model, @PathVariable("candidateId") int id) {
        model.addAttribute("candidate", candidateService.findById(id));
        model.addAttribute("cities", cityService.getAllCities());
        return "updateCandidate";
    }

    @PostMapping("/updateCandidate")
    public String updateCandidatePost(@ModelAttribute Candidate candidate) {
        candidate.getCity().setName(cityService.findById(candidate.getCity().getId()).getName());
        candidateService.update(candidate);
        return "redirect:/candidates";
    }
}
