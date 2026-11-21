package com.placement.util;

import com.placement.model.Student;
import java.util.Comparator;

public class StudentCgpaComparator implements Comparator<Student> {
    @Override
    public int compare(Student s1, Student s2) {
        // Descending order: highest CGPA first
        int cmp = Double.compare(s2.getCgpa(), s1.getCgpa());
        if (cmp != 0) {
            return cmp;
        }
        return s1.getFullName().compareToIgnoreCase(s2.getFullName());
    }
}