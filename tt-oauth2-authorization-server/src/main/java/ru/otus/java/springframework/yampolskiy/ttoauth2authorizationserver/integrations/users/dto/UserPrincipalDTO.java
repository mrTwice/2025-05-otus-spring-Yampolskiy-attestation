package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.integrations.users.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPrincipalDTO implements UserDetails {

    private UUID id;

    private String username;

    private String email;

    private String password;

    private boolean emailVerified;

    private boolean active;

    private boolean locked;

    private Instant credentialsExpireAt;

    private Instant accountExpireAt;

    private String firstName;

    private String middleName;

    private String lastName;

    private String nickname;

    private String pictureUrl;

    private String profile;

    private String website;

    private String locale;

    private String gender;

    private LocalDate birthdate;

    private String zoneinfo;

    private String phoneNumber;

    private boolean phoneNumberVerified;

    private String oidcSubject;

    private String oidcProvider;

    private Instant updatedAtOidc;

    private Map<String, Object> address;

    private Set<String> roles;

    private Set<String> permissions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}