package com.placement.repository;

import com.placement.enums.*;
import com.placement.model.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class DataStore {

    // Shared Collections across M1, M2, and M3
    public static final Map<Integer, User> usersById = new HashMap<>();
    public static final Map<String, User> usersByEmail = new HashMap<>();
    public static final List<Student> students = new ArrayList<>();
    public static final List<PlacementAdmin> admins = new ArrayList<>();
    public static final Map<Integer, Branch> branches = new HashMap<>();

    public static final Map<Integer, Company> companies = new HashMap<>();
    public static final List<Drive> drives = new ArrayList<>();
    public static final List<EligibilityCriteria> eligibilityList = new ArrayList<>();
    public static final List<Application> applications = new ArrayList<>();

    public static final List<InterviewExperience> experiences = new ArrayList<>();
    public static final Set<TopicTag> topicTags = new HashSet<>();

    // Static ID Counters
    private static int userIdSeq = 1;
    private static int branchIdSeq = 1;
    private static int companyIdSeq = 1;
    private static int driveIdSeq = 1;
    private static int applicationIdSeq = 1;
    private static int experienceIdSeq = 1;
    private static int tagIdSeq = 1;

    public static int nextUserId() { return userIdSeq++; }
    public static int nextBranchId() { return branchIdSeq++; }
    public static int nextCompanyId() { return companyIdSeq++; }
    public static int nextDriveId() { return driveIdSeq++; }
    public static int nextApplicationId() { return applicationIdSeq++; }
    public static int nextExperienceId() { return experienceIdSeq++; }
    public static int nextTagId() { return tagIdSeq++; }

    // Static initialization block loading sample records
    static {
        // 1. Branches
        Branch cse = new Branch(nextBranchId(), "CSE", "Computer Science");
        Branch it  = new Branch(nextBranchId(), "IT", "Computer Science");
        Branch ece = new Branch(nextBranchId(), "ECE", "Electronics");
        Branch me  = new Branch(nextBranchId(), "ME", "Mechanical");
        branches.put(cse.getBranchId(), cse);
        branches.put(it.getBranchId(), it);
        branches.put(ece.getBranchId(), ece);
        branches.put(me.getBranchId(), me);

        // 2. Students
        Student s1 = new Student(nextUserId(), "aarav.singh@college.edu", "pass123",
                "Aarav", "Singh", 8.70, 0, LocalDate.of(2005, 3, 14), 2027,
                cse, new String[]{"9812300001", "9812300011"});
        Student s2 = new Student(nextUserId(), "simran.kaur@college.edu", "pass123",
                "Simran", "Kaur", 8.20, 0, LocalDate.of(2004, 9, 18), 2027,
                it, new String[]{"9812300002"});
        Student s3 = new Student(nextUserId(), "harpreet.gill@college.edu", "pass123",
                "Harpreet", "Gill", 7.40, 1, LocalDate.of(2005, 7, 21), 2027,
                cse, new String[]{"9812300003"});
        Student s4 = new Student(nextUserId(), "priya.nair@college.edu", "pass123",
                "Priya", "Nair", 7.90, 0, LocalDate.of(2004, 11, 2), 2027,
                ece, new String[]{"9812300004"});
        Student s5 = new Student(nextUserId(), "rahul.verma@college.edu", "pass123",
                "Rahul", "Verma", 6.80, 2, LocalDate.of(2005, 1, 30), 2027,
                me, new String[]{"9812300005"});

        for (Student s : Arrays.asList(s1, s2, s3, s4, s5)) {
            students.add(s);
            usersById.put(s.getId(), s);
            usersByEmail.put(s.getEmail().toLowerCase(), s);
        }

        // 3. Admins
        PlacementAdmin a1 = new PlacementAdmin(nextUserId(), "kavita.malhotra@college.edu", "admin123",
                "Dr. Kavita Malhotra", "Placement Head", "9876500011", "101");
        PlacementAdmin a2 = new PlacementAdmin(nextUserId(), "rohit.sharma@college.edu", "admin123",
                "Rohit Sharma", "Placement Officer", "9876500012", null);

        for (PlacementAdmin a : Arrays.asList(a1, a2)) {
            admins.add(a);
            usersById.put(a.getId(), a);
            usersByEmail.put(a.getEmail().toLowerCase(), a);
        }

        // 4. Topic Tags
        topicTags.add(new TopicTag(nextTagId(), "DSA"));
        topicTags.add(new TopicTag(nextTagId(), "DBMS"));
        topicTags.add(new TopicTag(nextTagId(), "SQL"));
        topicTags.add(new TopicTag(nextTagId(), "Operating Systems"));
        topicTags.add(new TopicTag(nextTagId(), "HR and Behavioural"));
    }
}