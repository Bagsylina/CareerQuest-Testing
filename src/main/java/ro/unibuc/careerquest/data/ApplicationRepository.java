package ro.unibuc.careerquest.data;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationRepository extends MongoRepository<ApplicationEntity, String> {
    
    List<ApplicationEntity> findByUsername(String username);
    List<ApplicationEntity> findByJobId(String jobId);

    @Query("{ 'jobId': ?0, 'username': ?1 }")
    Optional<ApplicationEntity> findByJobIdAndUsername(String jobId, String username);
}
