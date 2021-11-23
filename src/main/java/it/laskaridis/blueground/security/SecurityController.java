package it.laskaridis.blueground.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.laskaridis.blueground.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Tag(name = "Security")
@RestController
@RequestMapping("/api/v1")
public class SecurityController {

    public static final String X_AUTH_USERNAME_HEADER = "x-auth-username";
    public static final String X_AUTH_PASSWORD_HEADER = "x-auth-password";

    private final String authTokenSecret;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public SecurityController(
            @Autowired AuthenticationManager authenticationManager,
            @Value("${jwt.token.secret}") String secret) {

        this.authenticationManager = authenticationManager;
        this.authTokenSecret = secret;
    }

    @PostMapping("/auth/login")
    public ResponseEntity login(
            @RequestHeader(X_AUTH_USERNAME_HEADER) String username,
            @RequestHeader(X_AUTH_PASSWORD_HEADER) String password) {

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        try {
            Authentication auth = authenticationManager.authenticate(token);
            User user = (User) auth.getPrincipal();
            return ResponseEntity.noContent().header(HttpHeaders.AUTHORIZATION, createJwtTokenFor(user)).build();
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private static final long ONE_DAY_IN_MILLIS = 1 * 24 * 60 * 60 * 1000;

    private Date getJwtTokenExpirationPeriod() {
        return new Date(System.currentTimeMillis() + ONE_DAY_IN_MILLIS);
    }

    private String createJwtTokenFor(UserDetails user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(getJwtTokenExpirationPeriod())
                .sign(Algorithm.HMAC256("secret".getBytes()));
    }
}
