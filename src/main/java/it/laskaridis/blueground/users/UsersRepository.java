package it.laskaridis.blueground.users;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UsersRepository extends PagingAndSortingRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
