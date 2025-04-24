package com.side.football_project.domain.match.service;

import com.side.football_project.domain.match.domain.Match;
import com.side.football_project.domain.match.domain.MatchUser;
import com.side.football_project.domain.match.repository.MatchRepository;
import com.side.football_project.domain.match.repository.MatchUserRepository;
import com.side.football_project.domain.stadium.entity.Stadium;
import com.side.football_project.domain.stadium.entity.StadiumStatus;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.domain.user.service.UserServiceImpl;
import com.side.football_project.domain.user.type.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MatchUserServiceImplTest {

    @InjectMocks
    private MatchUserServiceImpl matchUserService;

    @Mock
    private MatchUserRepository matchUserRepository;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private UserServiceImpl userService;

    private Match match;
    private MatchUser matchUser;
    private User user;
    private Stadium stadium;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("test")
                .age(10)
                .email("test@test.com")
                .password("1234")
                .role(UserRole.ADMIN)
                .build();

        stadium = Stadium.builder()
                .name("test stadium")
                .capacity(10)
                .currentReservationCount(9)
                .description("desc")
                .status(StadiumStatus.AVAILABLE)
                .build();

        match = Match.builder()
                .matchName("test match")
                .matchDate(LocalDateTime.now())
                .stadium(stadium)
                .isCompleted(false)
                .build();

        matchUser = MatchUser.builder()
                .match(match)
                .user(user)
                .build();
    }

    @Test
    void completeMatchTest() {
        // 필요한 mock 설정만 유지
        lenient().when(userService.findUserById(user.getId())).thenReturn(user);
        when(matchUserRepository.existsByMatchId(match.getId())).thenReturn(true);
        when(matchRepository.findById(match.getId())).thenReturn(Optional.of(match));
        when(matchUserRepository.findMatchById(match.getId())).thenReturn(java.util.List.of(matchUser));

        // 메서드 실행
        matchUserService.completeMatch(match.getId(), user);

        // 검증
        assertTrue(match.isCompleted(), "경기는 완료 상태여야 합니다.");
    }
}
