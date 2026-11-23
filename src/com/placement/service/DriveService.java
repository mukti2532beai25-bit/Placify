package com.placement.service;

import com.placement.enums.DriveStatus;
import com.placement.exception.InvalidInputException;
import com.placement.exception.NotEligibleException;
import com.placement.exception.RecordNotFoundException;
import com.placement.model.Company;
import com.placement.model.Drive;
import com.placement.model.EligibilityCriteria;
import com.placement.model.Student;
import com.placement.util.DriveDeadlineComparator;
import java.time.LocalDate;
import java.util.*;

/** Companies, drives, eligibility criteria. Keeps upcoming drives in a TreeSet ordered by deadline. */
public class DriveService {

    private final Map<Integer, Company> companies = new HashMap<>();
    private final Map<Integer, Drive> drives = new HashMap<>();
    private final Map<Integer, List<EligibilityCriteria>> criteriaByDrive = new HashMap<>();
    private final TreeSet<Drive> upcomingDrives = new TreeSet<>(new DriveDeadlineComparator());

    private int nextCompanyId = 1;
    private int nextDriveId = 1;

    // ---------------- companies

    public Company addCompany(String name, String industry, String website, String headquartersLocation)
            throws InvalidInputException {
        if (name == null || name.isBlank()) {
            throw new InvalidInputException("Company name is required.");
        }
        for (Company c : companies.values()) {
            if (c.getName().equalsIgnoreCase(name.trim())) {
                throw new InvalidInputException("Company '" + name.trim() + "' already exists.");
            }
        }
        Company company = new Company(nextCompanyId++, name.trim(), industry, website, headquartersLocation);
        companies.put(company.getCompanyId(), company);
        return company;
    }

    public Company findCompany(int companyId) throws RecordNotFoundException {
        Company c = companies.get(companyId);
        if (c == null) {
            throw new RecordNotFoundException("Company with ID " + companyId + " was not found.");
        }
        return c;
    }

    public List<Company> getAllCompanies() {
        List<Company> list = new ArrayList<>(companies.values());
        list.sort(Comparator.comparingInt(Company::getCompanyId));
        return list;
    }

    // ---------------- drives

    public Drive createDrive(int companyId, int adminId, String role, double packageOffered,
                             LocalDate deadline, int numberOfRounds)
            throws RecordNotFoundException, InvalidInputException {
        Company company = findCompany(companyId);
        if (role == null || role.isBlank()) {
            throw new InvalidInputException("Role is required.");
        }
        if (packageOffered <= 0) {
            throw new InvalidInputException("Package must be positive.");
        }
        if (numberOfRounds < 1) {
            throw new InvalidInputException("A drive needs at least one round.");
        }
        if (deadline == null || deadline.isBefore(LocalDate.now())) {
            throw new InvalidInputException("Deadline must be today or a future date.");
        }
        Drive drive = new Drive(nextDriveId++, company, adminId, role.trim(), packageOffered,
                deadline, numberOfRounds, DriveStatus.UPCOMING);
        drives.put(drive.getDriveId(), drive);
        upcomingDrives.add(drive);
        return drive;
    }

    public Drive findDrive(int driveId) throws RecordNotFoundException {
        Drive d = drives.get(driveId);
        if (d == null) {
            throw new RecordNotFoundException("Drive with ID " + driveId + " was not found.");
        }
        return d;
    }

    // ---------------- eligibility criteria

    /** Adds the cut-off for one branch, or updates it if that branch already has one for this drive. */
    public EligibilityCriteria addEligibility(int driveId, int branchId, double minCgpa, int maxBacklogs)
            throws RecordNotFoundException, InvalidInputException {
        findDrive(driveId);
        if (minCgpa < 0 || minCgpa > 10) {
            throw new InvalidInputException("Minimum CGPA must be between 0 and 10.");
        }
        if (maxBacklogs < 0) {
            throw new InvalidInputException("Max backlogs cannot be negative.");
        }
        List<EligibilityCriteria> list = criteriaByDrive.computeIfAbsent(driveId, k -> new ArrayList<>());
        for (EligibilityCriteria ec : list) {
            if (ec.getBranchId() == branchId) {
                ec.setMinCGPA(minCgpa);
                ec.setMaxBacklogs(maxBacklogs);
                return ec;
            }
        }
        EligibilityCriteria ec = new EligibilityCriteria(driveId, branchId, minCgpa, maxBacklogs);
        list.add(ec);
        return ec;
    }

    public Optional<EligibilityCriteria> getCriteria(int driveId, int branchId) {
        for (EligibilityCriteria ec : criteriaByDrive.getOrDefault(driveId, Collections.emptyList())) {
            if (ec.getBranchId() == branchId) {
                return Optional.of(ec);
            }
        }
        return Optional.empty();
    }

    public List<EligibilityCriteria> getCriteriaFor(int driveId) {
        return new ArrayList<>(criteriaByDrive.getOrDefault(driveId, Collections.emptyList()));
    }

    /** Throws NotEligibleException with a specific reason if the student fails branch, CGPA or backlog rules. */
    public void checkEligibility(Student student, Drive drive) throws NotEligibleException {
        if (student.getBranch() == null) {
            throw new NotEligibleException("Student has no branch assigned.");
        }
        int branchId = student.getBranch().getBranchId();
        Optional<EligibilityCriteria> rule = getCriteria(drive.getDriveId(), branchId);
        if (rule.isEmpty()) {
            throw new NotEligibleException("Branch " + student.getBranch().getBranchName()
                    + " is not eligible for drive #" + drive.getDriveId() + ".");
        }
        EligibilityCriteria ec = rule.get();
        if (student.getCgpa() < ec.getMinCGPA()) {
            throw new NotEligibleException(String.format("CGPA %.2f is below the required %.2f.",
                    student.getCgpa(), ec.getMinCGPA()));
        }
        if (student.getBacklogs() > ec.getMaxBacklogs()) {
            throw new NotEligibleException("Backlogs " + student.getBacklogs()
                    + " exceed the allowed " + ec.getMaxBacklogs() + ".");
        }
    }

    public boolean isEligible(Student student, Drive drive) {
        try {
            checkEligibility(student, drive);
            return true;
        } catch (NotEligibleException e) {
            return false;
        }
    }

    // ---------------- filtering and queries

    /** Drives still open for applications that this student qualifies for, earliest deadline first. */
    public List<Drive> getEligibleDrives(Student student) {
        refreshStatuses();
        List<Drive> result = new ArrayList<>();
        for (Drive d : upcomingDrives) {
            if (isEligible(student, d)) {
                result.add(d);
            }
        }
        return result;
    }

    /** Read-only view, so outside code cannot corrupt the TreeSet. */
    public SortedSet<Drive> getUpcomingDrives() {
        refreshStatuses();
        return Collections.unmodifiableSortedSet(upcomingDrives);
    }

    /** All drives in the order the caller chooses, e.g. new PackageComparator() in Part 2. */
    public List<Drive> getAllDrives(Comparator<Drive> order) {
        List<Drive> list = new ArrayList<>(drives.values());
        list.sort(order);
        return list;
    }

    // ---------------- status handling

    /** Moves drives whose deadline has passed from UPCOMING to ONGOING and out of the TreeSet. */
    public void refreshStatuses() {
        LocalDate today = LocalDate.now();
        List<Drive> expired = new ArrayList<>();
        for (Drive d : upcomingDrives) {
            if (d.getApplicationDeadline().isBefore(today)) {
                expired.add(d);
            }
        }
        for (Drive d : expired) {
            upcomingDrives.remove(d);
            d.setDriveStatus(DriveStatus.ONGOING);
        }
    }

    public void closeDrive(int driveId) throws RecordNotFoundException {
        Drive d = findDrive(driveId);
        upcomingDrives.remove(d);
        d.setDriveStatus(DriveStatus.CLOSED);
    }
}