package com.side.football_project.domain.user.repository;

import com.side.football_project.domain.user.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
