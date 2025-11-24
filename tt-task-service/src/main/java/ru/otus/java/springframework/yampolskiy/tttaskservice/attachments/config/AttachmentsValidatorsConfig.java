package ru.otus.java.springframework.yampolskiy.tttaskservice.attachments.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.java.springframework.yampolskiy.tttaskservice.attachments.entities.Attachment;
import ru.otus.java.springframework.yampolskiy.tttaskservice.attachments.validators.AttachmentValidator;
import ru.otus.java.springframework.yampolskiy.tttaskservice.common.DomainValidator;

@Configuration
public class AttachmentsValidatorsConfig {
    @Bean(name = "attachmentValidator")
    public DomainValidator<Attachment> attachmentValidator() {
        return new AttachmentValidator();
    }

}
