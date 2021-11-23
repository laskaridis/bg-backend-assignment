package it.laskaridis.blueground.users.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.laskaridis.blueground.users.model.Role;
import it.laskaridis.blueground.users.model.User;
import it.laskaridis.blueground.users.model.UsersRepository;
import it.laskaridis.blueground.users.view.CreateUserView;
import it.laskaridis.blueground.users.view.ShowUserView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.security.RolesAllowed;
import java.net.URI;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Tag(name = "User")
@RestController
@RequestMapping("/api/v1")
@RolesAllowed({ Role.ADMINISTRATOR })
public class UsersController {

    private final UsersRepository users;

    @Autowired
    public UsersController(UsersRepository userRepository) {
        this.users = userRepository;
    }

    @PostMapping("/users")
    public ResponseEntity<ShowUserView> createUser(@RequestBody CreateUserView form) {
        assertUserEmailNotTaken(form.getEmail());
        User user = this.users.save(form.toModel());
        URI uri = createResourceUriFor(user);
        ShowUserView view = ShowUserView.fromModel(user);
        return ResponseEntity.created(uri).body(view);
    }

    private void assertUserEmailNotTaken(String email) {
        this.users.findByEmail(email).ifPresent(user -> {
            throw new UserEmailAlreadyExistException(user.getEmail());
        });
    }

    private URI createResourceUriFor(User user) {
        return UriComponentsBuilder.newInstance()
                .pathSegment("users", "{uuid}")
                .buildAndExpand(user.getUuid())
                .toUri();
    }

    @GetMapping("/users")
    public List<ShowUserView> getAllUsers(Pageable pageable) {
        return this.users.findAll(pageable)
                .stream()
                .map(ShowUserView::fromModel)
                .collect(toList());
    }
}
