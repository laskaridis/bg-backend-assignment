package it.laskaridis.blueground.users;

import io.swagger.v3.oas.annotations.tags.Tag;
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

    private final UsersRepository repository;

    @Autowired
    public UsersController(UsersRepository userRepository) {
        this.repository = userRepository;
    }

    @PostMapping("/users")
    public ResponseEntity<Void> createUser(@RequestBody CreateUserView view) {
        User newUser = this.repository.save(view.toModel());
        URI uri = createResourceUriFor(newUser);
        return ResponseEntity.created(uri).build();
    }

    private URI createResourceUriFor(User user) {
        return UriComponentsBuilder.newInstance()
                .pathSegment("users", "{uuid}")
                .buildAndExpand(user.getUuid())
                .toUri();
    }

    @GetMapping("/users")
    public List<ShowUserView> getAllUsers(Pageable pageable) {
        return this.repository.findAll(pageable)
                .stream()
                .map(ShowUserView::fromModel)
                .collect(toList());
    }
}
