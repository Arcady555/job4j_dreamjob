package ru.job4j.dreamjob.utilit;

import org.springframework.ui.Model;
import ru.job4j.dreamjob.store.model.User;

import javax.servlet.http.HttpSession;

public class Utilit {

    public static void userGet(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        } else {
            user.setName(user.getEmail());
        }
        model.addAttribute("user", user);
    }
}
