package com.local_dating.matching_service.infrastructure.repository;

import com.local_dating.matching_service.domain.entity.MatchingScheduleRequested;
import com.local_dating.matching_service.domain.vo.MatchingScheduleUserCountVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatchingScheduleRequestedRepository extends JpaRepository<MatchingScheduleRequested, Long> {

    List<MatchingScheduleRequested> findByMatchingIdAndMatchingScheduleRoundIdAndUserId(Long matchingId, Long matchingScheduleRoundId, Long userId);

    Optional<MatchingScheduleRequested> findByMatchingIdAndUserId(Long matchingId, Long userId);

    @Query("select m "
            + "from MatchingScheduleRequested m "
            + "where m.matchingId = :matchingId "
            + "and m.matchingScheduleRoundId = :matchingScheduleRoundId "
            + "and m.userId = :userId "
            + "and m.matchingDate = :matchingDate "
            + "and m.matchingTimeType = :matchingTimeType "
            + "and m.statusCd = :statusCd")
    Optional<MatchingScheduleRequested> findDuplicatedData(
            @Param("matchingId") Long matchingId,
            @Param("matchingScheduleRoundId") Long matchingScheduleRoundId,
            @Param("userId") Long userId,
            @Param("matchingDate") LocalDate matchingDate,
            @Param("matchingTimeType") String matchingTimeType,
            @Param("statusCd") String statusCd
    );

    Optional<MatchingScheduleRequested> findByMatchingIdAndMatchingScheduleRoundIdAndUserIdAndMatchingDateAndMatchingTimeTypeAndStatusCd(Long matchingId, Long matchingScheduleRoundId
            , Long userId, LocalDate matchingDate, String matchingTimeType, String statusCd
    );

    @Query("select new com.local_dating.matching_service.domain.vo.MatchingScheduleUserCountVO(m.userId, count(m)) "
    + "from MatchingScheduleRequested m "
    + "where m.matchingId = :matchingId and m.statusCd = :statusCd "
    + "group by m.userId")
    List<MatchingScheduleUserCountVO> matchingScheduleUserCount(
            @Param("matchingId") Long matchingId,
            @Param("statusCd") String statusCd
    );

    @Query("select m from MatchingScheduleRequested m where m.matchingId = :matchingId and m.statusCd = :statusCd and m.userId = :userId")
    List<MatchingScheduleRequested> findByMatchingIdAndStatusCdAndUserId(
            @Param("matchingId") Long matchingId,
            @Param("statusCd") String statusCd,
            @Param("userId") Long userId
    );

    @Query("select m "
            + "from MatchingScheduleRequested m "
            + "where m.matchingId = :matchingId "
            + "and m.matchingScheduleRoundId = :matchingScheduleRoundId "
            + "and m.matchingDate = :matchingDate "
            + "and m.matchingTimeType = :matchingTimeType ")
    Optional<MatchingScheduleRequested> findMatchingScheduleRequestedData(
            @Param("matchingId") Long matchingId,
            @Param("matchingScheduleRoundId") Long matchingScheduleRoundId,
            @Param("matchingDate") LocalDate matchingDate,
            @Param("matchingTimeType") String matchingTimeType
    );
}
