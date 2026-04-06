package com.local_dating.matching_service.application;

import com.local_dating.matching_service.domain.entity.MatchingScheduleRequested;
import com.local_dating.matching_service.infrastructure.repository.MatchingScheduleRequestedRepository;
import com.local_dating.matching_service.presentation.dto.MatchingScheduleRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchingScheduleRequestedService {

    private final MatchingScheduleRequestedRepository matchingScheduleRequestedRepository;

    public List<MatchingScheduleRequested> getMatchingScheduleRequested(final Long match, final Long id, final Long round, final String authorization) {
        return matchingScheduleRequestedRepository.findByMatchingIdAndMatchingScheduleRoundIdAndUserId(match, round, id);
    }

    public List<MatchingScheduleRequested> saveMatchingScheduleRequested(final Long id, final String authorization, final List<MatchingScheduleRequestDTO> matchingScheduleRequestDTOS) {

        return matchingScheduleRequestDTOS.stream()
                .filter(el -> matchingScheduleRequestedRepository
                        .findByMatchingIdAndMatchingScheduleRoundIdAndUserIdAndMatchingDateAndMatchingTimeTypeAndStatusCd(
                                el.matchingId()
                                , el.matchingScheduleRoundId()
                                , el.userId()
                                , el.matchingDate()
                                , el.matchingTimeType()
                                , el.statusCd()
                        ).isEmpty()) // 기존에 없는 것만 필터링
                .map(el -> matchingScheduleRequestedRepository.save(
                        new MatchingScheduleRequested(
                                el.matchingId(),
                                el.matchingScheduleRoundId(),
                                el.userId(),
                                el.matchingDate(),
                                el.matchingTimeType(),
                                el.statusCd()
                        )
                ))
                .collect(Collectors.toUnmodifiableList());

        /*matchingScheduleRequestDTOS.stream().filter(el -> matchingScheduleRequestedRepository)

        matchingScheduleRequestedRepository.save(new MatchingScheduleRequested(
                matchingScheduleRequestDTO.matchingId()
                , matchingScheduleRequestDTO.matchingScheduleRoundId()
                , matchingScheduleRequestDTO.userId()
                , matchingScheduleRequestDTO.matchingDate()
                , matchingScheduleRequestDTO.matchingTimeType()
                , matchingScheduleRequestDTO.statusCd()));*/
    }
}
