package ro.unibuc.careerquest.data;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CVRepository extends MongoRepository<CVEntity, String> {
    
    List<CVEntity> findByUserId(String username);
}
