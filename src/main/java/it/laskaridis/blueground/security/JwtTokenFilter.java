package it.laskaridis.blueground.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import it.laskaridis.blueground.users.User;
import it.laskaridis.blueground.users.UsersRepository;
import org.apache.commons.lang3.StringUtils;
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

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final UsersRepository users;
    private final String authTokenSecret;

    @Autowired
    public JwtTokenFilter(
            @Autowired UsersRepository users,
            @Value("${jwt.token.secret}") String authTokenSecret) {

        this.users = users;
        this.authTokenSecret = authTokenSecret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = getBearerTokenFromHeader(request);
        if (StringUtils.isEmpty(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }
        String subject = getSubjectFromJwtToken(jwt);
        if (StringUtils.isEmpty(subject)) {
            filterChain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken auth = getAuthenticationContextForSubject(subject);
        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthenticationContextForSubject(String subject) {
        Optional<User> user = this.users.findByEmail(subject);
        UsernamePasswordAuthenticationToken ctxt = null;
        if (user.isPresent()) {
            UserDetails details = user.get();
            return new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
        } else {
            return new UsernamePasswordAuthenticationToken(null, null, List.of());
        }
    }

    private String getSubjectFromJwtToken(String token) {
        return JWT.require(Algorithm.HMAC256(this.authTokenSecret.getBytes()))
                .build()
                .verify(token)
                .getSubject();
    }

    private String getBearerTokenFromHeader(HttpServletRequest request) {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.isEmpty(header) && header.startsWith("Bearer")) {
            return header.split(" ")[1].trim();
        } else {
            return null;
        }
    }

}
