package ro.unibuc.careerquest.exception;

public class FieldNotFoundException extends RuntimeException {

    private static final String fieldNotFoundTemplate = "Field %s was not found";

    public FieldNotFoundException(String entity) {
        super(String.format(fieldNotFoundTemplate, entity));
    }
}
