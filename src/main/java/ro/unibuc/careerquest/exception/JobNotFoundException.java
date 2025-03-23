package ro.unibuc.careerquest.exception;

public class JobNotFoundException extends RuntimeException {

    private static final String jobNotFoundTemplate = "Job: %s was not found";

    public JobNotFoundException(String job) {
        super(String.format(jobNotFoundTemplate, job));
    }
}
