package ru.len4ass.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.len4ass.api.models.session.Session;
import ru.len4ass.api.models.user.User;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Integer> {
    Optional<Session> findSessionByToken(String token);

    Optional<Session> findSessionByUser(User user);
}
