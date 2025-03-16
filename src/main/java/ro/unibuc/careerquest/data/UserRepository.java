package ro.unibuc.careerquest.data;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {
    
    UserEntity findByUsername(String username);
    
    @Query("{ 'fullName': { $regex: ?0, $options: 'i' } }")
    List<UserEntity> findByFullNameContaining(String substring);
}
