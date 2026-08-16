package com.skillduel.backend.exception;

public class ErrorMessages {
    private ErrorMessages() {}
    public static final String INVALID_CREDENTIALS="Invalid credentials.";
    public static final String NO_TASKS_FOUND_BY_DIFFICULTY="No tasks found for this difficulty.";
    public static final String NO_DUEL_FOUND="There is no valid Duel.";
    public static final String USER_ALREADY_IN_DUEL="You're already joined into the duel.";
    public static final String DUEL_ALREADY_STARTED="Duel already started.";
    public static final String USER_NOT_FOUND="User not found";
}
