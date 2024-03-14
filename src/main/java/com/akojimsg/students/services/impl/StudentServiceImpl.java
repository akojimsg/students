package com.akojimsg.students.services.impl;

import com.akojimsg.students.data.dto.StudentDTO;
import com.akojimsg.students.data.entities.Student;
import com.akojimsg.students.data.repositories.StudentRepository;
import com.akojimsg.students.services.StudentService;
import com.akojimsg.students.utils.exceptions.ResourceConflictException;
import com.akojimsg.students.utils.exceptions.ResourceNotFoundException;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepository repository;

    public List<Student> getAllStudents(){
        return this.repository.findAll();
    }

    public Student findStudentById(Long id){
        Optional<Student> student = repository.findById(id);
        if(student.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Student not found with id %d", id));
        }
        return student.get();
    }

    public List<Student> findStudentsByName(String name){
        return repository.findByNameContains(name);
    }

    public Student addNewStudent(Student student) {
        Optional<Student> existingStudent = repository.findByEmail(student.getEmail());
        if (existingStudent.isPresent()) {
            throw new ResourceConflictException(String.format("A student with email %s already exists.", student.getEmail()));
        }
        return repository.save(student);
    }
    @Transactional
    public void updateStudent(Long id, StudentDTO student) {
        Student existingStudent = findStudentById(id);

        existingStudent.setEmail(student.getEmail());
        existingStudent.setName(student.getName());
        existingStudent.setDob(student.getDob());
    }

    @Transactional
    public Student updateStudentDetails(Long id, String name, String email, LocalDate dob) {
        Student student = findStudentById(id);

        if( !Strings.isNullOrEmpty(name) && !name.equals(student.getName())){
            student.setName(name);
        }

        if( !Strings.isNullOrEmpty(email) && !email.equals(student.getEmail())){
            student.setEmail(email);
        }

        if( dob != null) {
            student.setDob(dob);
        }

        return student;
    }

    public void deleteStudent(Long id){
        if(!repository.existsById(id))
            throw new ResourceNotFoundException(String.format("Student not found with id %d", id));
        repository.deleteById(id);
    }
}
