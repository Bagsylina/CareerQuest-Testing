package ro.unibuc.careerquest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import ro.unibuc.careerquest.data.CVRepository;
import ro.unibuc.careerquest.data.EmployerRepository;
import ro.unibuc.careerquest.data.UserEntity;
import ro.unibuc.careerquest.data.UserRepository;
import ro.unibuc.careerquest.data.JobEntity;
import ro.unibuc.careerquest.data.JobRepository;
import ro.unibuc.careerquest.data.ApplicationEntity;
import ro.unibuc.careerquest.data.ApplicationRepository;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = {UserRepository.class, CVRepository.class, JobRepository.class, EmployerRepository.class, ApplicationRepository.class})
public class CareerQuestApplication {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CVRepository cvRepository;
  
  	@Autowired
	private JobRepository jobDatabase;

	@Autowired
	private EmployerRepository employerRepository;

	@Autowired
	private ApplicationRepository applicationRepository;

	public static void main(String[] args) {
		SpringApplication.run(CareerQuestApplication.class, args);
	}

	@PostConstruct
	public void runAfterObjectCreated() {
		userRepository.deleteAll();
		cvRepository.deleteAll();
		jobDatabase.deleteAll();
		employerRepository.deleteAll();
		applicationRepository.deleteAll();
	}
}
