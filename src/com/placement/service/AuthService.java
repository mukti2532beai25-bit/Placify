package com.placement.service;

import com.placement.exception.InvalidInputException;
import com.placement.exception.RecordNotFoundException;
import com.placement.model.*;
import com.placement.repository.DataStore;
import com.placement.util.ValidationUtils;

public class AuthService {

    public Student registerStudent(String email, String password, String firstName, String lastName,
                                   double cgpa, int backlogs, int branchId, String phone)
            throws InvalidInputException, RecordNotFoundException {

        ValidationUtils.validateEmail(email);
        ValidationUtils.validatePassword(password);
        ValidationUtils.validateCgpa(cgpa);
        ValidationUtils.validateBacklogs(backlogs);
        ValidationUtils.validatePhone(phone);

        String normalizedEmail = email.trim().toLowerCase();
        if (DataStore.usersByEmail.containsKey(normalizedEmail)) {
            throw new InvalidInputException("An account with email '" + normalizedEmail + "' already exists.");
        }

        Branch branch = DataStore.branches.get(branchId);
        if (branch == null) {
            throw new RecordNotFoundException("Branch ID " + branchId + " does not exist.");
        }

        int newId = DataStore.nextUserId();
        Student student = new Student(newId, normalizedEmail, password,
                firstName.trim(), lastName.trim(), cgpa, backlogs, branch, phone.trim());

        DataStore.students.add(student);
        DataStore.usersById.put(newId, student);
        DataStore.usersByEmail.put(normalizedEmail, student);

        return student;
    }

    public User login(String email, String password) throws InvalidInputException, RecordNotFoundException {
        if (email == null || password == null) {
            throw new InvalidInputException("Email and password are required.");
        }
        User user = DataStore.usersByEmail.get(email.trim().toLowerCase());
        if (user == null) {
            throw new RecordNotFoundException("No account found for email: " + email);
        }
        if (!user.verifyPassword(password)) {
            throw new InvalidInputException("Incorrect password.");
        }
        return user;
    }
}