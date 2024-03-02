package com.springboot.hello.controllers;

import com.springboot.hello.data.entity.Rocket;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class RocketsController {
    private List<Rocket> rockets;
    public  RocketsController(){
        rockets = new ArrayList<>();
        rockets.add(new Rocket(Long.valueOf(1),"Falcon 1", "First generation rocket"));
        rockets.add(new Rocket(Long.valueOf(2),"Falcon 2", "Second generation rocket"));
        rockets.add(new Rocket(Long.valueOf(3),"Super Heavy", "Heavy rocket"));
    }

    @GetMapping("/rockets")
    public List<Rocket> getRockets(){
        return rockets;
    }
    @GetMapping("/rockets/{id}")
    public Rocket getRockets(@PathVariable Long id) {
        Rocket rocket = null;
        for(int i = 0; i < rockets.size(); i++) {
            rocket = rockets.get(i);
            if(Objects.equals(rocket.id(), id)) return rocket;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,String.format("No rocket with id %s", id));
    }

    @PostMapping("/rockets")
    @ResponseStatus(HttpStatus.CREATED)
    public void createRocket(@RequestBody Rocket rocket) {
        rockets.add(rocket);
    }
}
