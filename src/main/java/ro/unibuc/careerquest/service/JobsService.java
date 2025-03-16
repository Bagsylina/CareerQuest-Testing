package ro.unibuc.careerquest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.unibuc.careerquest.data.JobContent;
import ro.unibuc.careerquest.data.JobEntity;
import ro.unibuc.careerquest.data.JobRepository;
import ro.unibuc.careerquest.dto.Job;
import ro.unibuc.careerquest.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class JobsService {

    @Autowired
    private JobRepository jobDatabase;

    private final AtomicLong counter = new AtomicLong();
    private static final String helloTemplate = "Hello, %s!";
    private static final String informationTemplate = "%s : %s!";


    public List<Job> getAllJobs() {
        List<JobEntity> entities = jobDatabase.findAll();
        return entities.stream()
                .map(entity -> new Job(entity))
                .collect(Collectors.toList());
    }

    public Job getJob(String id) throws EntityNotFoundException {
        Optional<JobEntity> optionalEntity = jobDatabase.findById(id);
        JobEntity entity = optionalEntity.orElseThrow(() -> new EntityNotFoundException(id));
        return new Job(entity); // implemented constructor for ease
    }

    public Job createJob(JobContent job) {
        JobEntity entity = new JobEntity(Long.toString(counter.incrementAndGet()), job); // implemented constructor for ease

        jobDatabase.save(entity);
        return new Job(entity); // implemented constructor for ease
    }

    
    public Job updateJob(String id, JobContent job) throws EntityNotFoundException {
        JobEntity entity = jobDatabase.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
        entity.setContents(job);
        jobDatabase.save(entity);
        return new Job(entity);
    }

    public void deleteJob(String id) throws EntityNotFoundException {
        JobEntity entity = jobDatabase.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
        jobDatabase.delete(entity);
    }

    public void deleteAllJobs() {
        jobDatabase.deleteAll();
    }
}

// public Job buildJob( String title, String description, String company, String employer, String[] abilities, String[] domains, String[] characteristics, Integer salary, String location) {
    //     return new Job(Long.toString(counter.incrementAndGet()), title, description, company, employer, abilities, domains, characteristics, salary, location);
    // }

    // public Job buildJobFromTitle(String title) throws EntityNotFoundException {
    //     JobEntity entity = jobDatabase.findByTitle(title);
    //     if (entity == null) {
    //         throw new EntityNotFoundException(title);
    //     }
    //     return new Job(Long.toString(counter.incrementAndGet()), entity); // implemented constructor for ease
    // }

// public List<Job> saveAll(List<Job> jobs) {
    //     List<JobEntity> entities = jobs.stream()
    //             .map(job -> {
    //                 JobEntity entity = new JobEntity(job);
    //                 return entity;
    //             })
    //             .collect(Collectors.toList());

    //     List<JobEntity> savedEntities = jobDatabase.saveAll(entities);

    //     return savedEntities.stream()
    //             .map(entity -> new Job(entity))
    //             .collect(Collectors.toList());
    // }

    /// ???