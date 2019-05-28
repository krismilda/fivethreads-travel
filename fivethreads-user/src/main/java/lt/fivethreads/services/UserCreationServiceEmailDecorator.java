package lt.fivethreads.services;

import lt.fivethreads.entities.User;
import lt.fivethreads.entities.request.RegistrationForm;
import lt.fivethreads.exception.file.EmailAlreadyExists;
import lt.fivethreads.mapper.UserMapper;
import lt.fivethreads.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class UserCreationServiceEmailDecorator implements UserCreationService
{
    @Autowired
    MailService mailService;

    UserCreationService userCreationService;

    public UserCreationServiceEmailDecorator(UserCreationService userCreationService){
        this.userCreationService=userCreationService;
    }

    public User createNewUser(RegistrationForm registrationForm) {
        String subject = "You were added to the DevBridge Travel application.";
        String text = "You were added to the DevBridge Travel application. Your credentials:\nEmail: "+
                registrationForm.getEmail()+"\nPassword: "+registrationForm.getPassword() + "\nPlease log in to the system (url) and change the password.";
        mailService.sendSimpleMessage(registrationForm.getEmail(), subject, text);
        return userCreationService.createNewUser(registrationForm);
    }
}
