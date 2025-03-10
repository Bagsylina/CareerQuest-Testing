package ro.unibuc.careerquest.data;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * No need to implement this interface.
 * Spring Data MongoDB automatically creates a class it implementing the interface when you run the application.
 */
@Repository
public interface JobRepository extends MongoRepository<JobEntity, String> {

    JobEntity findByTitle(String title);

    List<JobEntity> findByDescription(String description);

    List<JobEntity> findByEmployer(String employer);

    List<JobEntity> findByCompany(String company);

    List<JobEntity> findByLocation(String location);

}
