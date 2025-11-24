package ru.otus.java.springframework.yampolskiy.tttaskservice.comments.security;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.otus.java.springframework.yampolskiy.tttaskservice.comments.exceptions.CommentAccessDeniedException;
import ru.otus.java.springframework.yampolskiy.tttaskservice.common.securiry.AbstractAccessPolicy;

import java.util.UUID;

@Slf4j
@Component("commentAccessPolicy")
public class CommentAccessPolicy extends AbstractAccessPolicy {

    public boolean canCreateComment(Authentication authentication) {
        return hasPermission(authentication, "comment:create");
    }

    public void checkCreateComment(Authentication authentication) {
        if (!canCreateComment(authentication)) {
            throw new CommentAccessDeniedException();
        }
    }

    public boolean canViewComment(Authentication authentication) {
        return hasPermission(authentication, "comment:view");
    }

    public void checkViewComment(Authentication authentication) {
        if (!canViewComment(authentication)) {
            throw new CommentAccessDeniedException();
        }
    }

    public boolean canUpdateComment(Authentication authentication) {
        return hasPermission(authentication, "comment:update");
    }

    public void checkUpdateComment(Authentication authentication) {
        if (!canUpdateComment(authentication)) {
            throw new CommentAccessDeniedException();
        }
    }

    public boolean canDeleteComment(Authentication authentication) {
        return hasPermission(authentication, "comment:delete");
    }

    public void checkDeleteComment(Authentication authentication) {
        if (!canDeleteComment(authentication)) {
            throw new CommentAccessDeniedException();
        }
    }

    public boolean canManageCommentOfUser(Authentication authentication, UUID authorId) {
        return hasPermission(authentication, "comment:update")
                || hasPermission(authentication, "comment:delete")
                || isSelf(authentication, authorId);
    }

    public void checkManageCommentOfUser(Authentication authentication, UUID authorId) {
        if (!canManageCommentOfUser(authentication, authorId)) {
            throw new CommentAccessDeniedException();
        }
    }
}

