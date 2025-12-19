package com.interactivetool.wilfred.interactivetool.repository;

import com.interactivetool.wilfred.interactivetool.entity.UserJourney;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJourneyRepository extends JpaRepository<UserJourney, Long> {
}
