package br.com.ghostdev.todolist.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.ghostdev.todolist.repository.UserRepository;
import br.com.ghostdev.todolist.entity.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public ResponseEntity getAllUsers(){
        var getUsers =  userRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(getUsers);
    }

    @GetMapping("/{id}")
    public ResponseEntity getIdUser(@PathVariable UUID id){
        var getUserId = userRepository.findById(id).orElse(null);

        if(getUserId == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário nao encontrado");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(getUserId);
        }
    }

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
