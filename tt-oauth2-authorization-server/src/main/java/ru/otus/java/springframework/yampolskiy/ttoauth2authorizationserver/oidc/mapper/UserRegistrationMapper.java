package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.oidc.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.integrations.users.dto.UserCreateDTO;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.oidc.dto.UserRegistrationRequest;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface UserRegistrationMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "oidcProvider", ignore = true)
    @Mapping(target = "oidcSubject", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "locked", constant = "false")
    @Mapping(target = "emailVerified", constant = "false")
    @Mapping(target = "phoneNumberVerified", constant = "false")
    UserCreateDTO toUserCreateDTO(UserRegistrationRequest request);
}
