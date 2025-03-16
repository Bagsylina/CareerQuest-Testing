package ro.unibuc.careerquest.exception;

public class IndexNotFoundException extends RuntimeException {

    private static final String indexNotFoundTemplate = "Index %s was not found";

    public IndexNotFoundException(String entity) {
        super(String.format(indexNotFoundTemplate, entity));
    }
}
