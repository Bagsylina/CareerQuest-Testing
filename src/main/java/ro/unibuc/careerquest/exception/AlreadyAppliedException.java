package ro.unibuc.careerquest.exception;

public class AlreadyAppliedException extends RuntimeException {
    private static final String alreadyAppliedTemplate = "User %s already applied to this job already taken";

    public AlreadyAppliedException(String entity) {
        super(String.format(alreadyAppliedTemplate, entity));
    }
}
