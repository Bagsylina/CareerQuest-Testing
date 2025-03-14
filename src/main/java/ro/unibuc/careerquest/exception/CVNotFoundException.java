package ro.unibuc.careerquest.exception;

public class CVNotFoundException extends RuntimeException {

    private static final String cvNotFoundTemplate = "CV %s was not found";

    public CVNotFoundException(String entity) {
        super(String.format(cvNotFoundTemplate, entity));
    }
}
