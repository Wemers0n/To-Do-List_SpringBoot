package br.com.ghostdev.todolist.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.ghostdev.todolist.repository.UserRepository;
import br.com.ghostdev.todolist.entity.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel users){
        var user = this.userRepository.findByUsername(users.getUsername());

        if(user != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario ja existe");
        } else {

            var passwordHashred = BCrypt.withDefaults()
                    .hashToString(12, users.getPassword().toCharArray());
            users.setPassword(passwordHashred);

            var userCreated = this.userRepository.save(users);
            return ResponseEntity.status(HttpStatus.OK).body(userCreated);
        }
    }
}
