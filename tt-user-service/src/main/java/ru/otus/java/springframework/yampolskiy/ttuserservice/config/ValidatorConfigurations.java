package ru.otus.java.springframework.yampolskiy.ttuserservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.java.springframework.yampolskiy.ttuserservice.entities.Permission;
import ru.otus.java.springframework.yampolskiy.ttuserservice.entities.Role;
import ru.otus.java.springframework.yampolskiy.ttuserservice.entities.User;
import ru.otus.java.springframework.yampolskiy.ttuserservice.repositories.RoleRepository;
import ru.otus.java.springframework.yampolskiy.ttuserservice.repositories.UserRepository;
import ru.otus.java.springframework.yampolskiy.ttuserservice.validators.Validator;
import ru.otus.java.springframework.yampolskiy.ttuserservice.validators.CommonRoleValidator;
import ru.otus.java.springframework.yampolskiy.ttuserservice.validators.CommonUserValidator;
import ru.otus.java.springframework.yampolskiy.ttuserservice.validators.UserEmailValidator;
import ru.otus.java.springframework.yampolskiy.ttuserservice.validators.RoleUniqueValidator;
import ru.otus.java.springframework.yampolskiy.ttuserservice.validators.PermissionValidator;
import ru.otus.java.springframework.yampolskiy.ttuserservice.validators.UserUniqueValidator;


@Configuration
public class ValidatorConfigurations {

    @Bean
    public Validator<Role> commonRoleValidator() {
        return new CommonRoleValidator();
    }

    @Bean
    public Validator<User> commonUserValidator() {
        return new CommonUserValidator();
    }

    @Bean
    public Validator<Role> roleUniqueValidator(RoleRepository roleRepository) {
        return new RoleUniqueValidator(roleRepository);
    }

    @Bean
    public Validator<String> userEmailValidator() {
        return new UserEmailValidator();
    }

    @Bean
    public Validator<User> userUniqueValidator(UserRepository userRepository) {
        return new UserUniqueValidator(userRepository);
    }

    @Bean
    public Validator<Permission> permissionValidator() {
        return new PermissionValidator();
    }

    @Bean
    public Validator<Permission> permissionUniqueValidator() {
        return new PermissionValidator();
    }

}


