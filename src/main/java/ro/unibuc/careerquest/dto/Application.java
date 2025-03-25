package ro.unibuc.careerquest.dto;

public class Application {

    private String id;

    private Job job;
    private User user;
    private CV cv;

    public Application() {}

    public Application(String id, Job job, User user, CV cv) {
        this.id = id;
        this.job = job;
        this.user = user;
        this.cv = cv;
    }

    public String getId() {return id;}

    public Job getJob() {return job;}
    public User getUser() {return user;}
    public CV getCV() {return cv;}
}
