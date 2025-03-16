package ro.unibuc.careerquest.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.unibuc.careerquest.data.UserRepository;
import ro.unibuc.careerquest.data.CVEntity;
import ro.unibuc.careerquest.data.CVRepository;
import ro.unibuc.careerquest.data.UserEntity;
import ro.unibuc.careerquest.dto.CV;
import ro.unibuc.careerquest.dto.CVCreation;
import ro.unibuc.careerquest.dto.User;
import ro.unibuc.careerquest.dto.UserCreation;
import ro.unibuc.careerquest.exception.InvalidEmailException;
import ro.unibuc.careerquest.exception.UserNotFoundException;
import ro.unibuc.careerquest.exception.UsernameTakenException;

@Component
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CVRepository cvRepository;

    private final AtomicLong counter = new AtomicLong();

    private static final String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    public List<User> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream()
                .map(user -> new User(user.getUsername(), user.getDescription(), user.getFirstName(), user.getLastName(), 
                    user.getBirthdate(), user.getEmail(), user.getPhone()))
                .collect(Collectors.toList());
    }

    public List<User> getAllUsersByName(String name) {
        List<UserEntity> users = userRepository.findByFullNameContaining(name);
        return users.stream()
                .map(user -> new User(user.getUsername(), user.getDescription(), user.getFirstName(), user.getLastName(), 
                    user.getBirthdate(), user.getEmail(), user.getPhone()))
                .collect(Collectors.toList());
    }

    public User getUser(String username) throws UserNotFoundException {
        Optional<UserEntity> optionalUser = userRepository.findById(username);
        UserEntity user = optionalUser.orElseThrow(() -> new UserNotFoundException(username));
        return new User(user.getUsername(), user.getDescription(), user.getFirstName(), user.getLastName(),
                        user.getBirthdate(), user.getEmail(), user.getPhone());
    }

    public User createUser(UserCreation credentials) throws UsernameTakenException, InvalidEmailException {
        //check if user not already taken
        Optional<UserEntity> optionalUser = userRepository.findById(credentials.getUsername());
        optionalUser.ifPresent(user -> {throw new UsernameTakenException(user.getUsername());});

        //check if email is valid
        boolean validEmail = Pattern.compile(emailRegex)
                                    .matcher(credentials.getEmail())
                                    .matches();
        if(!validEmail) {
            throw new InvalidEmailException(credentials.getEmail());
        }

        //create user in database
        UserEntity user = new UserEntity(credentials.getUsername(), credentials.getPassword(), credentials.getEmail());
        userRepository.save(user);
        
        return new User(user.getUsername(), user.getEmail());
    }

    public User updateCredentials(String username, UserCreation credentials) throws UserNotFoundException, InvalidEmailException {
        Optional<UserEntity> optionalUser = userRepository.findById(username);
        UserEntity user = optionalUser.orElseThrow(() -> new UserNotFoundException(username));

        if(credentials.getEmail() != null) {
            boolean validEmail = Pattern.compile(emailRegex)
                                        .matcher(credentials.getEmail())
                                        .matches();
            if(!validEmail) {
                throw new InvalidEmailException(credentials.getEmail());
            }

            user.setEmail(credentials.getEmail());
        }

        if(credentials.getPassword() != null)
            user.setPassword(credentials.getPassword());

        userRepository.save(user);

        return new User(user.getUsername(), user.getDescription(), user.getFirstName(), user.getLastName(),
            user.getBirthdate(), user.getEmail(), user.getPhone());
    }

    public User updateUser(String username, User userData) throws UserNotFoundException {
        Optional<UserEntity> optionalUser = userRepository.findById(username);
        UserEntity user = optionalUser.orElseThrow(() -> new UserNotFoundException(username));

        if(userData.getDescription() != null)
            user.setDescription(userData.getDescription());
        if(userData.getFirstName() != null)
            user.setFirstName(userData.getFirstName());
        if(userData.getLastName() != null)
            user.setLastName(userData.getLastName());
        if(userData.getBirthdate() != null)
            user.setBirthdate(userData.getBirthdate());
        if(userData.getPhone() != null)
            user.setPhone(userData.getPhone());

        userRepository.save(user);

        return new User(user.getUsername(), user.getDescription(), user.getFirstName(), user.getLastName(),
            user.getBirthdate(), user.getEmail(), user.getPhone());
    }  

    public void deleteUser(String username) throws UserNotFoundException {
        Optional<UserEntity> optionalUser = userRepository.findById(username);
        UserEntity user = optionalUser.orElseThrow(() -> new UserNotFoundException(username));

        userRepository.delete(user);
    }

    public List<CV> getCVs(String username) throws UserNotFoundException {
        //first check if user exists
        Optional<UserEntity> optionalUser = userRepository.findById(username);
        UserEntity user = optionalUser.orElseThrow(() -> new UserNotFoundException(username));
        
        List<CVEntity> cvs = cvRepository.findByUserId(username);

        return cvs.stream()
                .map(cv -> new CV(cv.getId(), cv.getUserId(), cv.getDescription(), cv.getAchievements(), cv.getEducation(), cv.getExperience(),
                            cv.getExtracurricular(), cv.getProjects(), cv.getSkills(), cv.getTools(), cv.getLanguages()))
                .collect(Collectors.toList());
    }

    public CV addCV(String username, CVCreation cvData) throws UserNotFoundException {
        Optional<UserEntity> optionalUser = userRepository.findById(username);
        UserEntity user = optionalUser.orElseThrow(() -> new UserNotFoundException(username));

        CVEntity cv = new CVEntity(Long.toString(counter.incrementAndGet()), username, cvData.getDescription(), cvData.getAchievements());
        cvRepository.save(cv);

        return new CV(cv.getId(), username, cv.getDescription(), cv.getAchievements());
    }
}
