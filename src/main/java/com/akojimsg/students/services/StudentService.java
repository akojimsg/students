package com.akojimsg.students.services;

import com.akojimsg.students.data.dtos.StudentDto;
import com.akojimsg.students.data.entities.Student;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface StudentService {
    public List<Student> getAllStudents();
    public Student findStudentById(Long id);
    public List<Student> findStudentsByName(String name);
    public Student addNewStudent(Student student);
    public void updateStudent(Long id, StudentDto student);
    public Student updateStudentDetails(Long id, String firstname, String lastname, String email, LocalDate dob);
    public void deleteStudent(Long id);
}
