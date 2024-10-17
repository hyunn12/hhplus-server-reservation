package io.hhplus.reserve.concert.domain;

import io.hhplus.reserve.concert.application.ConcertInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConcertDomainService {

    private final ConcertRepository concertRepository;

    public ConcertDomainService(ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }

    // 콘서트 목록 조회
    public List<ConcertInfo.ConcertDetail> getAvailableConcertList(String date) {
        List<Concert> concertList = concertRepository.getConcertList(date);
        return concertList.stream().map(ConcertInfo.ConcertDetail::of).toList();
    }

    // 콘서트 좌석 목록 조회
    public List<ConcertInfo.SeatDetail> getSeatListByConcertId(Long concertId) {
        List<ConcertSeat> seatList = concertRepository.getConcertSeatListByConcertId(concertId);
        return seatList.stream().map(ConcertInfo.SeatDetail::of).toList();
    }

    // 콘서트 상세조회
    public Concert getConcertDetail(Long concertId) {
        return concertRepository.getConcert(concertId);
    }

    // 콘서트 좌석 목록 조회 (Pessimistic Lock)
    public List<ConcertSeat> getSeatListWithLock(List<Long> seatIdList) {
        return concertRepository.getConcertSeatListWithLock(seatIdList);
    }

    // 콘서트 좌석 상태 조회
    public boolean hasInvalidSeat(List<ConcertSeat> seatList) {
        return seatList.stream().anyMatch(ConcertSeat::isInvalid);
    }

}
