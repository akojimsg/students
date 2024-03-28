package com.akojimsg.students.controllers;

import com.akojimsg.students.data.dtos.SignupRequest;
import com.akojimsg.students.data.entities.User;
import com.akojimsg.students.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class UserController {
    @Autowired
    private UserService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUsers(){
        return service.getAllUsers();
    }

    @GetMapping(path = "{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User student = service.findUserById(id);
        return ResponseEntity.ok(student);
    }

    @GetMapping(path = "query")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<User>> searchByName(@RequestParam("name") String name) {
        List<User> student = service.findUsersByFullName(name);
        return ResponseEntity.ok(student);
    }

    @PostMapping
    public ResponseEntity<User> registerNewUser(@RequestBody SignupRequest request){
        User entity = service.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(entity);
    }

//    @PutMapping(path = "{id}")
//    @ResponseStatus(HttpStatus.ACCEPTED)
//    public void editUserRecord(@PathVariable Long id, @RequestBody SignupRequest request) {
//        service.(id, request);
//    }

//    @PatchMapping(path = "{id}")
//    public ResponseEntity<User> editUserDetails(
//            @PathVariable Long id,
//            @RequestParam(required = false, name = "firstname") String firstname,
//            @RequestParam(required = false, name = "firstname") String lastname,
//            @RequestParam(required = false, name = "email") String email,
//            @RequestParam(required = false, name = "dob") LocalDate dob
//    ) {
//        User entity = service.(id, firstname, lastname, email, dob);
//        return ResponseEntity.ok(entity);
//    }

    @DeleteMapping(path = "{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteUserById(@PathVariable Long id) {
        service.deleteUser(id);
    }

}
