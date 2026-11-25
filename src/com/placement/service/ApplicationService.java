package com.placement.service;

import com.placement.enums.ApplicationStage;
import com.placement.enums.DriveStatus;
import com.placement.enums.ResultStatus;
import com.placement.exception.DuplicateApplicationException;
import com.placement.exception.InvalidInputException;
import com.placement.exception.InvalidStageTransitionException;
import com.placement.exception.NotEligibleException;
import com.placement.exception.RecordNotFoundException;
import com.placement.model.Application;
import com.placement.model.Drive;
import com.placement.model.Student;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Validates deadlines and eligibility, prevents duplicates, enforces sequential stages,
 * and keeps a FIFO processing queue of applications.
 */
public class ApplicationService implements IApplicationService {

    private final DriveService driveService;
    private final Function<Integer, Student> studentFinder;   // returns null if not found

    private final Map<Integer, Application> applications = new HashMap<>();
    private final LinkedList<Application> processingQueue = new LinkedList<>();
    private int nextApplicationId = 1;

    public ApplicationService(DriveService driveService, Function<Integer, Student> studentFinder) {
        this.driveService = driveService;
        this.studentFinder = studentFinder;
    }

    // ---------------- apply

    @Override
    public Application apply(int studentId, int driveId)
            throws RecordNotFoundException, NotEligibleException, DuplicateApplicationException {

        // 1. student and drive must exist
        Student student = studentFinder.apply(studentId);
        if (student == null) {
            throw new RecordNotFoundException("Student with ID " + studentId + " was not found.");
        }
        Drive drive = driveService.findDrive(driveId);

        // 2. deadline / status
        if (drive.getDriveStatus() == DriveStatus.CLOSED
                || LocalDate.now().isAfter(drive.getApplicationDeadline())) {
            throw new NotEligibleException("Applications for drive #" + driveId
                    + " closed on " + drive.getApplicationDeadline() + ".");
        }

        // 3. duplicate
        for (Application a : applications.values()) {
            if (a.getStudentId() == studentId && a.getDriveId() == driveId) {
                throw new DuplicateApplicationException("Student " + studentId
                        + " has already applied to drive #" + driveId + ".");
            }
        }

        // 4. branch / CGPA / backlogs (throws NotEligibleException with the reason)
        driveService.checkEligibility(student, drive);

        // 5. every check passed: only now create and store the application
        Application app = new Application(nextApplicationId++, studentId, driveId, LocalDate.now());
        applications.put(app.getApplicationId(), app);
        processingQueue.add(app);          // tail of the FIFO queue
        return app;
    }

    // ---------------- stage transitions

    @Override
    public Application advanceStage(int appId)
            throws RecordNotFoundException, InvalidStageTransitionException {
        Application app = findApplication(appId);
        ApplicationStage current = app.getStage();

        if (current == ApplicationStage.RESULT_DECLARED) {
            throw new InvalidStageTransitionException(
                    "Application #" + appId + " is already at RESULT_DECLARED; nothing comes after it.");
        }
        if (current == ApplicationStage.INTERVIEW_COMPLETED) {
            throw new InvalidStageTransitionException(
                    "Application #" + appId + " is at INTERVIEW_COMPLETED; use declareResult to finish it.");
        }
        app.setStage(ApplicationStage.values()[current.ordinal() + 1]);
        return app;
    }

    @Override
    public Application moveToStage(int appId, ApplicationStage target)
            throws RecordNotFoundException, InvalidStageTransitionException {
        Application app = findApplication(appId);
        requireNextStage(app, target);
        app.setStage(target);
        return app;
    }

    /** The one place where the sequential rule lives. */
    private void requireNextStage(Application app, ApplicationStage target)
            throws InvalidStageTransitionException {
        ApplicationStage current = app.getStage();
        if (target == null) {
            throw new InvalidStageTransitionException("Target stage is required.");
        }
        if (target == ApplicationStage.RESULT_DECLARED) {
            throw new InvalidStageTransitionException(
                    "RESULT_DECLARED can only be reached through declareResult.");
        }
        if (target.ordinal() != current.ordinal() + 1) {
            throw new InvalidStageTransitionException("Cannot move from " + current + " to " + target
                    + ": stages must be sequential and cannot go backwards.");
        }
    }

    // ---------------- result

    @Override
    public Application declareResult(int appId, ResultStatus result, String offerType)
            throws RecordNotFoundException, InvalidStageTransitionException, InvalidInputException {
        Application app = findApplication(appId);

        if (result == null || result == ResultStatus.PENDING) {
            throw new InvalidInputException("Result must be OFFER or REJECT.");
        }
        if (app.getStage() != ApplicationStage.INTERVIEW_COMPLETED) {
            throw new InvalidStageTransitionException("A result can only be declared at INTERVIEW_COMPLETED, "
                    + "but application #" + appId + " is at " + app.getStage() + ".");
        }
        if (result == ResultStatus.OFFER && (offerType == null || offerType.isBlank())) {
            throw new InvalidInputException("An offer type (e.g. FTE, Internship) is required for an OFFER.");
        }

        app.setStage(ApplicationStage.RESULT_DECLARED);
        app.setResult(result);
        app.setOfferType(result == ResultStatus.OFFER ? offerType.trim() : null);
        return app;
    }

    // ---------------- FIFO processing queue

    /** Removes and returns the oldest waiting application, or null if the queue is empty. */
    public Application processNextApplication() {
        return processingQueue.poll();
    }

    public int getQueueSize() {
        return processingQueue.size();
    }

    /** Copy, so callers cannot modify the real queue. */
    public List<Application> getPendingQueue() {
        return new ArrayList<>(processingQueue);
    }

    // ---------------- lookups (used by Member 3)

    public Application findApplication(int appId) throws RecordNotFoundException {
        Application app = applications.get(appId);
        if (app == null) {
            throw new RecordNotFoundException("Application with ID " + appId + " was not found.");
        }
        return app;
    }

    public List<Application> getApplicationsForStudent(int studentId) {
        List<Application> out = new ArrayList<>();
        for (Application a : applications.values()) {
            if (a.getStudentId() == studentId) {
                out.add(a);
            }
        }
        return out;
    }

    public List<Application> getApplicationsForDrive(int driveId) {
        List<Application> out = new ArrayList<>();
        for (Application a : applications.values()) {
            if (a.getDriveId() == driveId) {
                out.add(a);
            }
        }
        return out;
    }

    public List<Application> getAllApplications() {
        return new ArrayList<>(applications.values());
    }
}