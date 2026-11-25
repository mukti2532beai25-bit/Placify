package com.placement.service;

import com.placement.enums.ApplicationStage;
import com.placement.enums.ResultStatus;
import com.placement.exception.*;
import com.placement.model.Application;

/** Contract for the application lifecycle: apply -> advance through stages -> declare result. */
public interface IApplicationService {

    Application apply(int studentId, int driveId)
            throws RecordNotFoundException, NotEligibleException, DuplicateApplicationException, InvalidInputException;

    /** Moves the application exactly one stage forward (cannot be used to reach RESULT_DECLARED). */
    Application advanceStage(int appId) throws RecordNotFoundException, InvalidStageTransitionException;

    /** Moves to a specific stage; skipping or going backwards throws InvalidStageTransitionException. */
    Application moveToStage(int appId, ApplicationStage target)
            throws RecordNotFoundException, InvalidStageTransitionException;

    /** Final step: INTERVIEW_COMPLETED -> RESULT_DECLARED with an OFFER or REJECT outcome. */
    Application declareResult(int appId, ResultStatus result, String offerType)
            throws RecordNotFoundException, InvalidStageTransitionException, InvalidInputException;
}
