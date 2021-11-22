package it.laskaridis.blueground.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
public class UsersController {

    private final UsersRepository repository;

    @Autowired
    public UsersController(UsersRepository userRepository) {
        this.repository = userRepository;
    }

    @PostMapping("/users")
    public ResponseEntity<Void> createUser(@RequestBody UserView view) {
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
    public List<UserView> getAllUsers(Pageable pageable) {
        return this.repository.findAll(pageable)
                .stream()
                .map(UserView::fromModel)
                .collect(toList());
    }
}
