package service;

import domain.MyUser;
import exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.MyUserRepository;

@Service
public class MyUserService {
    @Autowired
    MyUserRepository myUserRepository;

    public MyUser getUserByUsername(String username) {
        MyUser user = myUserRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }
}
