package ro.unibuc.careerquest.exception;

public class InvalidEmailException extends RuntimeException {
    private static final String invalidEmailTemplate = "Email %s is not valid.";

    public InvalidEmailException(String entity) {
        super(String.format(invalidEmailTemplate, entity));
    }
}
