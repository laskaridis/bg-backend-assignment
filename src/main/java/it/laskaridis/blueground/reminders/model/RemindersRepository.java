package it.laskaridis.blueground.reminders.model;

import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RemindersRepository extends CrudRepository<Reminder, Long> {

    Optional<Reminder> findByUuid(String uuid);

    List<Reminder> findAllByScheduledAtBeforeAndSentAtIsNull(LocalDateTime date);
}
