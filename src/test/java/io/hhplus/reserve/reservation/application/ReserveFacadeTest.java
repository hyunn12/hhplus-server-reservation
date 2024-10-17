package io.hhplus.reserve.reservation.application;

import io.hhplus.reserve.concert.domain.ConcertDomainService;
import io.hhplus.reserve.concert.domain.ConcertSeat;
import io.hhplus.reserve.reservation.domain.ReservationDomainService;
import io.hhplus.reserve.waiting.domain.Waiting;
import io.hhplus.reserve.waiting.domain.WaitingDomainService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ReserveFacadeTest {

    @Autowired
    private ConcertDomainService concertDomainService;
    @Autowired
    private ReservationDomainService reservationDomainService;
    @Autowired
    private WaitingDomainService waitingDomainService;

    private ReserveFacade reserveFacade;

    @BeforeEach
    void setUp() {
        reserveFacade = new ReserveFacade(concertDomainService, reservationDomainService, waitingDomainService);
    }

    @Test
    @DisplayName("정상 예약")
    void testReserveSuccess() {
        // given
        ReserveCommand.Reserve command = ReserveCommand.Reserve.builder()
                .token("testtokentokentoken")
                .userId(1L)
                .seatIdList(List.of(1L, 2L, 3L))
                .build();

        Waiting waiting = waitingDomainService.validateToken(command.getToken());

        List<ConcertSeat> seatList = concertDomainService.getSeatListWithLock(command.getSeatIdList());

        // when
        ReserveInfo.Reserve result = reserveFacade.reserve(command);

        // then
        assertNotNull(result);
        assertEquals(result.getStatus(), "SUCCESS");
    }

    @Test
    @DisplayName("토큰이 비어있을 경우")
    void testInvalidToken() {
        // given
        ReserveCommand.Reserve command = ReserveCommand.Reserve.builder()
                .token(null)
                .userId(1L)
                .seatIdList(List.of(1L, 2L, 3L))
                .build();

        // when / then
        assertThrows(IllegalStateException.class, () -> reserveFacade.reserve(command));
    }

    @Test
    @DisplayName("좌석 목록이 비어있을 경우")
    void testEmptySeatList() {
        // given
        ReserveCommand.Reserve command = ReserveCommand.Reserve.builder()
                .token("testtokentokentoken")
                .userId(1L)
                .seatIdList(List.of())
                .build();

        // when / then
        assertThrows(IllegalStateException.class, () -> reserveFacade.reserve(command));
    }

    @Test
    @DisplayName("예약 불가능한 좌석이 포함된 경우")
    void testHasReservedSeat() {
        // given
        ReserveCommand.Reserve command = ReserveCommand.Reserve.builder()
                .token("testtokentokentoken")
                .userId(1L)
                .seatIdList(List.of(4L, 2L, 3L))
                .build();

        Waiting waiting = waitingDomainService.validateToken(command.getToken());

        List<ConcertSeat> seatList = concertDomainService.getSeatListWithLock(command.getSeatIdList());

        // when / then
        assertThrows(IllegalStateException.class, () -> reserveFacade.reserve(command));
    }

}