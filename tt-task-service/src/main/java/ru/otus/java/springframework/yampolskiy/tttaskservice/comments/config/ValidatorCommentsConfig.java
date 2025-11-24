package ru.otus.java.springframework.yampolskiy.tttaskservice.comments.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.java.springframework.yampolskiy.tttaskservice.comments.entities.Comment;
import ru.otus.java.springframework.yampolskiy.tttaskservice.comments.validators.CommentValidator;
import ru.otus.java.springframework.yampolskiy.tttaskservice.common.DomainValidator;

@Configuration
public class ValidatorCommentsConfig {

    @Bean(name = "commentValidator")
    public DomainValidator<Comment> commentValidator() {
        return new CommentValidator();
    }

}
