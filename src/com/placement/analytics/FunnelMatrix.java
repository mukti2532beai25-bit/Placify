
package com.placement.analytics;

import com.placement.enums.ApplicationStage;

public class FunnelMatrix {

    private final int[][] matrix;

    private static final String[] STAGES = {
        "APPLIED",
        "SHORTLISTED",
        "INTERVIEW_SCHEDULED",
        "INTERVIEW_COMPLETED",
        "RESULT_DECLARED"
    };

    public FunnelMatrix(int numberOfDrives) {
        if (numberOfDrives < 0) {
            throw new IllegalArgumentException(
                    "Number of drives cannot be negative.");
        }

        matrix = new int[numberOfDrives][ApplicationStage.values().length];
    }

    // Store the number of applications at a particular stage.
    public void setCount(int driveIndex, int stageIndex, int count) {

        if (driveIndex < 0 || driveIndex >= matrix.length) {
            throw new IndexOutOfBoundsException("Invalid drive index.");
        }

        if (stageIndex < 0 || stageIndex >= STAGES.length) {
            throw new IndexOutOfBoundsException("Invalid stage index.");
        }

        if (count < 0) {
            throw new IllegalArgumentException(
                    "Count cannot be negative.");
        }

        matrix[driveIndex][stageIndex] = count;
    }

    public int getCount(int driveIndex, int stageIndex) {
        return matrix[driveIndex][stageIndex];
    }

    // Calculate how many applications were lost after a stage.
    public int calculateDropOff(int driveIndex, int stageIndex) {

        if (driveIndex < 0 || driveIndex >= matrix.length) {
            throw new IndexOutOfBoundsException("Invalid drive index.");
        }

        if (stageIndex < 0 || stageIndex >= STAGES.length) {
            throw new IndexOutOfBoundsException("Invalid stage index.");
        }

        // The final stage has no next stage to compare with.
        if (stageIndex == STAGES.length - 1) {
            return 0;
        }

        int currentCount = matrix[driveIndex][stageIndex];
        int nextCount = matrix[driveIndex][stageIndex + 1];

        return Math.max(0, currentCount - nextCount);
    }

    // Display the placement funnel for all drives.
    public void displayMatrix() {

        System.out.println("\n========== PLACEMENT FUNNEL ==========");

        System.out.printf("%-12s", "Drive");

        for (String stage : STAGES) {
            System.out.printf("%-24s", stage);
        }

        System.out.println();

        for (int i = 0; i < matrix.length; i++) {
            System.out.printf("%-12d", i + 1);

            for (int j = 0; j < STAGES.length; j++) {
                System.out.printf("%-24d", matrix[i][j]);
            }

            System.out.println();
        }

        System.out.println("\n========== DROP-OFF COUNTS ==========");

        for (int i = 0; i < matrix.length; i++) {
            System.out.println("\nDrive " + (i + 1));

            for (int j = 0; j < STAGES.length - 1; j++) {
                System.out.println(
                        STAGES[j] + " to " + STAGES[j + 1]
                        + ": " + calculateDropOff(i, j));
            }
        }
    }
}
