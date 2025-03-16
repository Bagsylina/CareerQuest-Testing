package ro.unibuc.careerquest.data;

import org.springframework.data.annotation.Id;

public class ApplicationEntity {
    
    @Id
    private String id;

    private String jobId;
    private String username;
    private String cvId;

    public ApplicationEntity() {}

    public ApplicationEntity(String id, String jobId, String username, String cvId) {
        this.id = id;
        this.jobId = jobId;
        this.username = username;
        this.cvId = cvId;
    }

    public String getId() {return id;}
    
    public String getJobId() {return jobId;}
    public String getUsername() {return username;}
    public String getCVId() {return cvId;}
}
