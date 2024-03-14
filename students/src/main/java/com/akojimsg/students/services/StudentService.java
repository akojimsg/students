package com.akojimsg.students.services;

import com.akojimsg.students.data.dto.StudentDTO;
import com.akojimsg.students.data.entities.Student;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public interface StudentService {
    public List<Student> getAllStudents();
    public Student findStudentById(Long id);
    public List<Student> findStudentsByName(String name);
    public Student addNewStudent(Student student);
    public void updateStudent(Long id, StudentDTO student);
    public Student updateStudentDetails(Long id, String name, String email, LocalDate dob);
    public void deleteStudent(Long id);
}
