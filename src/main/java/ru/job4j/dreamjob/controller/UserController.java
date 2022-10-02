package ru.job4j.dreamjob.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import ru.job4j.dreamjob.service.UserService;
import ru.job4j.dreamjob.store.model.User;

import java.util.Optional;

@Controller
@ThreadSafe
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String addUserGet(Model model) {
        model.addAttribute("user", new User(0, "Заполните поле", "Заполните поле"));
        return "addUser";
    }

    @PostMapping("/registration")
    public String addUserPost(@ModelAttribute User user) {
        Optional<User> regUser = userService.add(user);
        if (regUser.isEmpty()) {
            return "redirect:/fail";
        }
        return "redirect:/success";
    }

    @GetMapping("/success")
    public String success() {
        return "printSuccess";
    }

    @GetMapping("/fail")
    public String fail(@ModelAttribute String massage) {
        return "printFail";
    }
}
