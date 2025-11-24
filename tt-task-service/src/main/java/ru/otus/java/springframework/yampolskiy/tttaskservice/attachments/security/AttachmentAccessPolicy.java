package ru.otus.java.springframework.yampolskiy.tttaskservice.attachments.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.otus.java.springframework.yampolskiy.tttaskservice.attachments.exceptions.AttachmentAccessDeniedException;
import ru.otus.java.springframework.yampolskiy.tttaskservice.common.securiry.AbstractAccessPolicy;

import java.util.UUID;

@Slf4j
@Component("attachmentAccessPolicy")
public class AttachmentAccessPolicy extends AbstractAccessPolicy {

    public boolean canCreateAttachment(Authentication authentication) {
        return hasPermission(authentication, "attachment:create");
    }

    public void checkCreateAttachment(Authentication authentication) {
        if (!canCreateAttachment(authentication)) {
            throw new AttachmentAccessDeniedException();
        }
    }

    public boolean canViewAttachment(Authentication authentication) {
        return hasPermission(authentication, "attachment:view");
    }

    public void checkViewAttachment(Authentication authentication) {
        if (!canViewAttachment(authentication)) {
            throw new AttachmentAccessDeniedException();
        }
    }

    public boolean canUpdateAttachment(Authentication authentication) {
        return hasPermission(authentication, "attachment:update");
    }

    public void checkUpdateAttachment(Authentication authentication) {
        if (!canUpdateAttachment(authentication)) {
            throw new AttachmentAccessDeniedException();
        }
    }

    public boolean canDeleteAttachment(Authentication authentication) {
        return hasPermission(authentication, "attachment:delete");
    }

    public void checkDeleteAttachment(Authentication authentication) {
        if (!canDeleteAttachment(authentication)) {
            throw new AttachmentAccessDeniedException();
        }
    }

    public boolean canViewAttachmentsOfUser(Authentication authentication, UUID userId) {
        return isSelf(authentication, userId) || canViewAttachment(authentication);
    }

    public void checkViewAttachmentsOfUser(Authentication authentication, UUID userId) {
        if (!canViewAttachmentsOfUser(authentication, userId)) {
            throw new AttachmentAccessDeniedException();
        }
    }
}

