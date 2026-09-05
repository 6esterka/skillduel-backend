package com.skillduel.backend.exception;

public class ErrorMessages {
    private ErrorMessages() {}
    public static final String INVALID_CREDENTIALS="Invalid credentials.";
    public static final String NO_TASKS_FOUND_BY_DIFFICULTY="No tasks found for this difficulty.";
    public static final String NO_DUEL_FOUND="There is no valid Duel.";
    public static final String USER_ALREADY_IN_DUEL="You're already joined into the duel.";
    public static final String DUEL_ALREADY_STARTED="Duel already started.";
    public static final String USER_NOT_FOUND="User not found";
    public static final String DUEL_NOT_FINISHED="Duel not finished.";
    public static final String ALREADY_VOTED="Already voted.";
    public static final String EMAIL_ALREADY_EXISTS = "Email already registered.";
    public static final String DUEL_NOT_STARTED="Duel is not active.";
    public static final String ONLY_SPECTATORS_CAN_VOTE="Only spectators can vote for winner of the duel.";
}
