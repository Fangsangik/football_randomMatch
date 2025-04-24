package com.side.football_project.domain.match.service;

public interface RandomService {

    void joinMatch(Long userId);
    void fillEmptySpot(Long stadiumId);
}
