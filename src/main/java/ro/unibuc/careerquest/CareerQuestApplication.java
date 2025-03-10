package ro.unibuc.careerquest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import jakarta.annotation.PostConstruct;
import ro.unibuc.careerquest.data.JobEntity;
import ro.unibuc.careerquest.data.JobRepository;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = JobRepository.class)
public class CareerQuestApplication {

	@Autowired
	private JobRepository jobDatabase;

	public static void main(String[] args) {
		SpringApplication.run(CareerQuestApplication.class, args);
	}

	@PostConstruct
	public void runAfterObjectCreated() {
		jobDatabase.deleteAll();

		String[] abilities = {"Java", "Spring Boot", "Docker"};
        String[] domains = {"Backend Development", "Microservices"};
        String[] characteristics = {"Team player", "Problem solver"};
		jobDatabase.save(new JobEntity("789", "JobDefault", "Descriere", "companie", "employer", abilities, domains, characteristics, 5000, "Brasov")); // here add attributes for default values
	}

}
