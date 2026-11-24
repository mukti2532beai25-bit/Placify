package com.placement.service;

import com.placement.exception.InvalidInputException;
import com.placement.exception.RecordNotFoundException;
import com.placement.model.Student;
import com.placement.repository.DataStore;
import com.placement.util.StudentCgpaComparator;
import com.placement.util.ValidationUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class StudentService {

    public Student getStudentById(int studentId) throws RecordNotFoundException {
        for (Student s : DataStore.students) {
            if (s.getId() == studentId) {
                return s;
            }
        }
        throw new RecordNotFoundException("Student with ID " + studentId + " not found.");
    }

    public void updateAcademicProfile(int studentId, double newCgpa, int newBacklogs)
            throws RecordNotFoundException, InvalidInputException {
        ValidationUtils.validateCgpa(newCgpa);
        ValidationUtils.validateBacklogs(newBacklogs);

        Student s = getStudentById(studentId);
        s.setCgpa(newCgpa);
        s.setBacklogs(newBacklogs);
    }

    public void addStudentPhone(int studentId, String newPhone)
            throws RecordNotFoundException, InvalidInputException {
        ValidationUtils.validatePhone(newPhone);
        Student s = getStudentById(studentId);
        for (String existing : s.getPhones()) {
            if (existing.equals(newPhone.trim())) {
                throw new InvalidInputException("Phone number already registered for this student.");
            }
        }
        s.addPhone(newPhone.trim());
    }

    public List<Student> getStudentsSortedByCgpa() {
        List<Student> sorted = new ArrayList<>(DataStore.students);
        Collections.sort(sorted, new StudentCgpaComparator());
        return sorted;
    }
    public List<Student> filterEligibleStudents(String branchName, double minCgpa, int maxBacklogs) {
        List<Student> result = new ArrayList<>(DataStore.students);
        Iterator<Student> it = result.iterator();
        while (it.hasNext()) {
            Student s = it.next();
            boolean branchMatch = s.getBranch().getBranchName().equalsIgnoreCase(branchName);
            if (!branchMatch || s.getCgpa() < minCgpa || s.getBacklogs() > maxBacklogs) {
                it.remove();
            }
        }
        return result;
    }
}