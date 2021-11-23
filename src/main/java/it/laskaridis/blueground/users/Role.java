package it.laskaridis.blueground.users;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data @RequiredArgsConstructor @EqualsAndHashCode
public class Role implements GrantedAuthority {

    public static final String ADMINISTRATOR = "ROLE_ADMINISTRATOR";
    public static final String COLONIST = "ROLE_COLONIST";

    private final String role;

    @Override
    public String getAuthority() {
        return this.role;
    }
}
