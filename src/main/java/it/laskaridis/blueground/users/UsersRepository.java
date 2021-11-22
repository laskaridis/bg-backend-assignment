package it.laskaridis.blueground.users;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface UsersRepository extends PagingAndSortingRepository<User, Long> {
}
