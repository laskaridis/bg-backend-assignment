package it.laskaridis.blueground.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import it.laskaridis.blueground.users.model.User;
import it.laskaridis.blueground.users.model.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.auth0.jwt.algorithms.Algorithm.HMAC256;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Component
public class AuthenticationInterceptingFilter extends OncePerRequestFilter {

    public static final String BEARER_AUTH_SCHEME = "Bearer";

    private final UsersRepository users;
    private final String jwtSigningSecret;

    @Autowired
    public AuthenticationInterceptingFilter(
            @Autowired UsersRepository users,
            @Value("${jwt.token.secret}") String jwtSigningSecret) {

        this.users = users;
        this.jwtSigningSecret = jwtSigningSecret;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        Optional<String> jwt = getBearerAuthenticationTokenFrom(request);
        if (jwt.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }
        Optional<String> subject = getSubjectFrom(jwt.get());
        if (subject.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken auth = getAuthenticationContextFor(subject.get());
        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthenticationContextFor(String subject) {
        Optional<User> user = this.users.findByEmail(subject);
        if (user.isPresent()) {
            UserDetails details = user.get();
            return new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
        } else
            return new UsernamePasswordAuthenticationToken(null, null, List.of());
    }

    private Optional<String> getSubjectFrom(String jwt) {
        String subject = JWT.require(HMAC256(this.jwtSigningSecret.getBytes()))
                .build()
                .verify(jwt)
                .getSubject();
        return Optional.ofNullable(subject);
    }

    private Optional<String> getBearerAuthenticationTokenFrom(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isBearerTokenPresent(header)) {
            return Optional.ofNullable(getBearerTokenFrom(header));
        } else
            return Optional.empty();
    }

    private String getBearerTokenFrom(String header) {
        return header.split(" ")[1].trim();
    }

    private boolean isBearerTokenPresent(String header) {
        return !isEmpty(header) && header.startsWith(BEARER_AUTH_SCHEME);
    }
}
