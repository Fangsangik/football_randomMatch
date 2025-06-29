package com.side.football_project.domain.sms;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsRepository extends JpaRepository<SmsLog, Long> {
}
