package com.placement.util;

import com.placement.model.Drive;
import java.util.Comparator;

/** Sorts drives chronologically by application deadline (ties broken by id so a TreeSet keeps every drive). */
public class DriveDeadlineComparator implements Comparator<Drive> {
    @Override
    public int compare(Drive a, Drive b) {
        int c = a.getApplicationDeadline().compareTo(b.getApplicationDeadline());
        return c != 0 ? c : Integer.compare(a.getDriveId(), b.getDriveId());
    }
}