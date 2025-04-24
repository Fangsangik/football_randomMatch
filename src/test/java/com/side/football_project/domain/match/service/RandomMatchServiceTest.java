package com.side.football_project.domain.match.service;

import com.side.football_project.domain.reservation.repository.ReservationRepository;
import com.side.football_project.domain.reservation.service.ReservationService;
import com.side.football_project.domain.stadium.entity.Stadium;
import com.side.football_project.domain.stadium.entity.StadiumStatus;
import com.side.football_project.domain.stadium.service.StadiumService;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.domain.user.service.UserService;
import com.side.football_project.domain.user.type.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RandomMatchServiceTest {

    @InjectMocks
    private RandomMatchService randomMatchService;

    @Mock
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserService userService;

    @Mock
    private StadiumService stadiumService;

    private User user;
    private Stadium stadium;

    @BeforeEach
    void setUp(){
        user = User.builder()
                .name("test")
                .age(10)
                .email("test")
                .password("1234")
                .role(UserRole.ADMIN)
                .build();

        stadium = Stadium.builder()
                .name("test")
                .capacity(10)
                .currentReservationCount(9)
                .description("test")
                .status(StadiumStatus.AVAILABLE)
                .build();
    }

    @Test
    void fillEmptySpotTest() {

        when(stadiumService.findByIdWithLock(stadium.getId())).thenReturn(stadium);
        when(reservationRepository.findAllByStadiumId(stadium.getId())).thenReturn(List.of());
        when(userService.findUserById(user.getId())).thenReturn(user);

        randomMatchService.joinMatch(user.getId());
        randomMatchService.fillEmptySpot(stadium.getId());

        verify(reservationService, times(1)).createReservation(any(), user);

    }

    @Test
    void already_full() {

        stadium = Stadium.builder()
                .name("test")
                .capacity(10)
                .currentReservationCount(10)
                .description("test")
                .status(StadiumStatus.AVAILABLE)
                .build();
        when(stadiumService.findByIdWithLock(stadium.getId())).thenReturn(stadium);
        when(reservationRepository.findAllByStadiumId(stadium.getId())).thenReturn(List.of());
        when(userService.findUserById(user.getId())).thenReturn(user);

        randomMatchService.joinMatch(user.getId());
        randomMatchService.fillEmptySpot(stadium.getId());

        verify(reservationService, never()).createReservation(any(), user);
    }
}