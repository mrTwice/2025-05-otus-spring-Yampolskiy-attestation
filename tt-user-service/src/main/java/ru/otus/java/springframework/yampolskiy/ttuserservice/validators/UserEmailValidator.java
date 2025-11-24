package ru.otus.java.springframework.yampolskiy.ttuserservice.validators;

import ru.otus.java.springframework.yampolskiy.ttuserservice.entities.ValidationEmailPartError;
import ru.otus.java.springframework.yampolskiy.ttuserservice.exceptions.ValidationEmailException;

import java.util.ArrayList;
import java.util.List;


public class UserEmailValidator  implements Validator<String> {

    public void validate(String email) {
        List<ValidationEmailPartError> errors = new ArrayList<>();

        if (checkNullAndFormat(email, errors)) {
            throwIfErrors(errors);
            return;
        }

        String[] parts = extractEmailParts(email, errors);
        String localPart = parts[0];
        String domain = parts[1];

        validateEmailLocalPart(localPart, errors);
        validateEmailDomain(domain, errors);

        throwIfErrors(errors);
    }

    private boolean checkNullAndFormat(String email, List<ValidationEmailPartError> errors) {
        if (email == null || email.isEmpty()) {
            errors.add(new ValidationEmailPartError("email", "Email cannot be null or empty"));
            return true;
        }

        if (!email.contains("@")) {
            errors.add(new ValidationEmailPartError("email", "Email must contain the '@' symbol"));
            return true;
        }

        if (email.length() < 5 || email.length() > 254) {
            errors.add(new ValidationEmailPartError("email", "Email length must be between 5 and 254 characters"));
        }

        return false;
    }

    private String[] extractEmailParts(String email, List<ValidationEmailPartError> errors) {
        String[] parts = email.split("@", 2);
        if (parts.length != 2) {
            errors.add(new ValidationEmailPartError("email", "Email format is invalid"));
            throwIfErrors(errors);
        }
        return parts;
    }

    private void throwIfErrors(List<ValidationEmailPartError> errors) {
        if (!errors.isEmpty()) {
            throw new ValidationEmailException("EMAIL_VALIDATION_ERROR", "Не корректный email", errors);
        }
    }

    private void validateEmailLocalPart(String localPart, List<ValidationEmailPartError> errors) {
        String localPartRegex = "^[a-zA-Z0-9._-]+$";

        if (!localPart.matches(localPartRegex)) {
            errors.add(new ValidationEmailPartError("email", "Email local part contains invalid characters"));
        }

        if (localPart.startsWith(".") || localPart.endsWith(".")) {
            errors.add(new ValidationEmailPartError("email", "Email local part cannot start or end with a dot"));
        }

        if (localPart.contains("..")) {
            errors.add(new ValidationEmailPartError("email", "Email local part cannot contain consecutive dots"));
        }

        if (localPart.length() > 64) {
            errors.add(new ValidationEmailPartError("email", "Email local part cannot be longer than 64 characters"));
        }
    }

    private void validateEmailDomain(String domain, List<ValidationEmailPartError> errors) {
        String domainRegex = "^[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";

        if (!domain.matches(domainRegex)) {
            errors.add(new ValidationEmailPartError("domain", "Email domain is invalid"));
        }

        if (domain.length() > 253) {
            errors.add(new ValidationEmailPartError("domain", "Email domain cannot be longer than 253 characters"));
        }

        if (domain.startsWith(".") || domain.endsWith(".")) {
            errors.add(new ValidationEmailPartError("domain", "Email domain cannot start or end with a dot"));
        }

        if (domain.contains("..")) {
            errors.add(new ValidationEmailPartError("domain", "Email domain cannot contain consecutive dots"));
        }
    }
}
