package com.akojimsg.students.controllers;

import com.akojimsg.students.data.dto.StudentDTO;
import com.akojimsg.students.data.entities.Student;
import com.akojimsg.students.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/students")
public class StudentController {
    @Autowired
    private StudentService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Student> getStudents(){
        return service.getAllStudents();
    }

    @GetMapping(path = "{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Student student = service.findStudentById(id);
        return ResponseEntity.ok(student);
    }

    @GetMapping(path = "query")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Student>> searchByName(@RequestParam("name") String name) {
        List<Student> student = service.findStudentsByName(name);
        return ResponseEntity.ok(student);
    }

    @PostMapping
    public ResponseEntity<Student> registerNewStudent(@RequestBody Student student){
        Student entity = service.addNewStudent(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(entity);
    }

    @PutMapping(path = "{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void editStudentRecord(@PathVariable Long id, @RequestBody StudentDTO student) {
        service.updateStudent(id, student);
    }

    @PatchMapping(path = "{id}")
    public ResponseEntity<Student> editStudentDetails(
            @PathVariable Long id,
            @RequestParam(required = false, name = "name") String name,
            @RequestParam(required = false, name = "email") String email,
            @RequestParam(required = false, name = "dob") LocalDate dob
    ) {
        Student entity = service.updateStudentDetails(id, name, email, dob);
        return ResponseEntity.ok(entity);
    }

    @DeleteMapping(path = "{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteStudentById(@PathVariable Long id) {
        service.deleteStudent(id);
    }

}
