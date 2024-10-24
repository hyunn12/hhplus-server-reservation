package io.hhplus.reserve.concert.domain;

import io.hhplus.reserve.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Table(name = "concert_seat")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ConcertSeat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Long seatId;

    @Column(name = "concert_id")
    private Long concertId;

    @Column(name = "seat_num")
    private int seatNum;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'AVAILABLE'")
    private SeatStatus status;

    @Column(name = "reserved_at")
    private LocalDateTime reservedAt;

    @Builder(builderMethodName = "createBuilder")
    public ConcertSeat(long concertId, int seatNum, LocalDateTime reservedAt) {
        this.concertId = concertId;
        this.seatNum = seatNum;
        this.reservedAt = reservedAt;
    }

    @Builder(builderMethodName = "updateBuilder")
    public ConcertSeat(long seatId, SeatStatus status, LocalDateTime reservedAt) {
        this.seatId = seatId;
        this.status = status;
        this.reservedAt = reservedAt;
    }

    // 좌석 선점
    public void reserveSeat() {
        boolean isReserved = this.reservedAt != null && reservedAt.plusMinutes(5).isAfter(LocalDateTime.now());
        boolean isConfirmed = this.status == SeatStatus.CONFIRMED;

        if (isReserved || isConfirmed) {
            throw new IllegalStateException("예약이 불가능한 좌석입니다.");
        }

        this.reservedAt = LocalDateTime.now();
    }

    // 좌석 선점 상태 확인
    public void checkSeatExpired() {
        boolean isExpired = this.reservedAt != null && this.reservedAt.plusMinutes(5).isBefore(LocalDateTime.now());
        if (isExpired) {
            throw new IllegalStateException("좌석 선점이 만료되었습니다.");
        }
    }

    // 좌석 확정
    public void confirm() {
        this.status = SeatStatus.CONFIRMED;
    }

}
