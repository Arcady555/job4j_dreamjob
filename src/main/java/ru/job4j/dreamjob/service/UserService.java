package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;

import ru.job4j.dreamjob.store.UserDbStore;
import ru.job4j.dreamjob.store.model.User;

import java.util.Optional;

@Service
@ThreadSafe
public class UserService {
    private final UserDbStore userStore;

    public UserService(UserDbStore userStore) {
        this.userStore = userStore;
    }

    public Optional<User> add(User user) {
        return userStore.add(user);
    }

    public Optional<User> findUserByEmailAndPwd(String email, String password) {
        return userStore.findUserByEmailAndPwd(email, password);
    }
}
