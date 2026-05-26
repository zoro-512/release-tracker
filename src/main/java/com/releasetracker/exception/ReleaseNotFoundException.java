package com.releasetracker.exception;

/**
 * Thrown when a requested release record is not found in the database.
 */
public class ReleaseNotFoundException extends RuntimeException {

    public ReleaseNotFoundException(Long id) {
        super("Release not found with ID: " + id);
    }

    public ReleaseNotFoundException(String message) {
        super(message);
    }
}
