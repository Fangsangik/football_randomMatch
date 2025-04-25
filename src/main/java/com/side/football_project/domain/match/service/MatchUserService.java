package com.side.football_project.domain.match.service;


import com.side.football_project.domain.match.dto.*;
import com.side.football_project.domain.user.entity.User;

import java.util.List;

public interface MatchUserService {

    TeamMatchResponseDto teamMatch (TeamMatchRequestDto requestDto, User user);
    void completeMatch(Long matchId, User user);
    void submitMatchRatings(Long matchId, User user, List<MatchRatingRequestDto> ratings);
    MatchResponseDto createMatch (MatchRequestDto requestDto, User user);
}
