package com.interactivetool.wilfred.interactivetool.repository;

import com.interactivetool.wilfred.interactivetool.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findBySessionIdAndIsActive(String sessionId, Boolean isActive);

    Session findBySessionIdAndImsiAndIsActive(String sessionId, String imSi, Boolean isActive);
}
