package ro.unibuc.careerquest.exception;

public class InvalidPasswordException extends RuntimeException {
    private static final String invalidPasswordTemplate = "Password is not valid. The password must contain at least 8 characters, an uppercase letter, a lowercase letter, a digit and a special character.";

    public InvalidPasswordException() {
        super(invalidPasswordTemplate);
    }
}
