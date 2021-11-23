package it.laskaridis.blueground.security;

import it.laskaridis.blueground.users.model.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EntityAuditor implements AuditorAware<User> {

    @Override
    public Optional<User> getCurrentAuditor() {
        Authentication ctxt = SecurityContextHolder.getContext().getAuthentication();
        User user = null;
        if (ctxt != null && ctxt.isAuthenticated())
            user = (User) ctxt.getPrincipal();
        return Optional.ofNullable(user);
    }
}
