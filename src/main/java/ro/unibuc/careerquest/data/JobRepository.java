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

    List<JobEntity> findByTitle(String title);

    //JobEntity findById(String id); -> already exists by default!!

    List<JobEntity> findByDescription(String description);

    List<JobEntity> findByEmployer(String employer);

    List<JobEntity> findByLocation(String location);


}
