package ru.job4j.dreamjob.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import ru.job4j.dreamjob.service.CityService;
import ru.job4j.dreamjob.service.PostService;
import ru.job4j.dreamjob.store.model.Post;

@Controller
@ThreadSafe
public class PostController {
    private final PostService postService;
    private final CityService cityService;

    public PostController(PostService postService, CityService cityService) {
        this.postService = postService;
        this.cityService = cityService;
    }

    @GetMapping("/posts")
    public String posts(Model model) {
        model.addAttribute("posts", postService.findAll());
        return "posts";
    }

    @GetMapping("/formAddPost")
    public String addPostGet(Model model) {
        model.addAttribute("post", new Post(0, "Заполните поле", "Заполните поле"));
        model.addAttribute("cities", cityService.getAllCities());
        return "addPost";
    }

    @PostMapping("/formAddPost")
    public String addPostPost(@ModelAttribute Post post) {
        post.getCity().setName(cityService.findById(post.getCity().getId()).getName());
        postService.add(post);
        return "redirect:/posts";
    }

    @GetMapping("/formUpdatePost/{postId}")
    public String updatePostGet(Model model, @PathVariable("postId") int id) {
        model.addAttribute("post", postService.findById(id));
        model.addAttribute("cities", cityService.getAllCities());
        return "updatePost";
    }

    @PostMapping("/updatePost")
    public String updatePostPost(@ModelAttribute Post post) {
        post.getCity().setName(cityService.findById(post.getCity().getId()).getName());
        postService.update(post);
        return "redirect:/posts";
    }
}