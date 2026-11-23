package com.placement.analytics;

public class FunnelMatrix {

    private int[][] matrix;

    public FunnelMatrix(int numberOfDrives) {
        matrix = new int[numberOfDrives][4];
    }

    public void setCount(int driveIndex, int stageIndex, int count) {

        if (driveIndex < 0 || driveIndex >= matrix.length) {
            System.out.println("Invalid drive index!");
            return;
        }

        if (stageIndex < 0 || stageIndex >= 4) {
            System.out.println("Invalid stage index!");
            return;
        }

        if (count < 0) {
            System.out.println("Count cannot be negative!");
            return;
        }

        matrix[driveIndex][stageIndex] = count;
    }

    public int getCount(int driveIndex, int stageIndex) {

        if (driveIndex < 0 || driveIndex >= matrix.length) {
            System.out.println("Invalid drive index!");
            return -1;
        }

        if (stageIndex < 0 || stageIndex >= 4) {
            System.out.println("Invalid stage index!");
            return -1;
        }

        return matrix[driveIndex][stageIndex];
    }

    public int calculateDropOff(int driveIndex, int stageIndex) {

        if (driveIndex < 0 || driveIndex >= matrix.length) {
            System.out.println("Invalid drive index!");
            return -1;
        }

        if (stageIndex < 0 || stageIndex >= 3) {
            System.out.println("Invalid stage index!");
            return -1;
        }

        int currentStage = matrix[driveIndex][stageIndex];

        int nextStage = matrix[driveIndex][stageIndex + 1];

        return currentStage - nextStage;
    }

    public void displayMatrix() {

        String[] stages = {
                "APPLICATION",
                "DOCUMENTATION",
                "INTERVIEW",
                "OFFER"
        };

        System.out.println("\nPLACEMENT FUNNEL MATRIX");

        System.out.print("Drive\t");

        for (String stage : stages) {
            System.out.print(stage + "\t");
        }

        System.out.println();

        for (int i = 0; i < matrix.length; i++) {

            System.out.print("Drive " + (i + 1) + "\t");

            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + "\t");
            }

            System.out.println();
        }
    }
}