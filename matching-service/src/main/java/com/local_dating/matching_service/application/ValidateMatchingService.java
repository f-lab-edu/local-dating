package com.local_dating.matching_service.application;

import com.local_dating.matching_service.domain.type.MatchingScheduleRequestedType;
import com.local_dating.matching_service.domain.type.MatchingScheduleRoundType;
import com.local_dating.matching_service.domain.type.MatchingTimeType;
import com.local_dating.matching_service.infrastructure.repository.MatchingRepository;
import com.local_dating.matching_service.infrastructure.repository.MatchingScheduleRequestedRepository;
import com.local_dating.matching_service.infrastructure.repository.MatchingScheduleRoundRepository;
import com.local_dating.matching_service.presentation.dto.MatchingScheduleRequestDTO;
import com.local_dating.matching_service.presentation.dto.MatchingScheduleRequestListDTO;
import com.local_dating.matching_service.util.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.local_dating.matching_service.util.MessageCode.*;

@Service
@RequiredArgsConstructor
public class ValidateMatchingService {

    private final MatchingRepository matchingRepository;
    private final MatchingScheduleRoundRepository matchingScheduleRoundRepository;
    private final MatchingScheduleRequestedRepository matchingScheduleRequestedRepository;

    public void validationMatchingSchduleV2(final Long id, final List<MatchingScheduleRequestDTO> list) {
        List<Map<String, String>> invalidData = new ArrayList<>();
        list.stream().forEach(el -> {
            validateMatchingId(id, el.matchingId(), invalidData);
            validateMatchingRoundId(el.matchingScheduleRoundId(), el.matchingId(), invalidData);
            validateMatchingDate(el.matchingDate(), el.matchingTimeType(), invalidData);
            if (el.statusCd().equals(MatchingScheduleRequestedType.SUBMITTED.getCode())) {
                invalidData.add(Map.of(VALIDATION_EXCEPTION_MATCHING_SCHEDULE_REQUEST.getMessage(), el.statusCd()));
            }
        });
        if (!invalidData.isEmpty()) {
            throw new BusinessException(VALIDATION_EXCEPTION_MATCHING_SCHEDULE_REQUEST, invalidData.toString());
        }
    }

    public void validateMatchingId(final Long userId, final Long matchingId, List<Map<String, String>> invalidData) {
        Boolean isValid = matchingRepository.findByIdAndRequIdOrRecvId(matchingId, userId, userId)
                .isPresent();
        if (!isValid) {
            invalidData.add(Map.of(VALIDATION_EXCEPTION_MATCHING_ID.getMessage(), matchingId.toString()));
        }
    }

    public void validateMatchingRoundId(final Long matchingScheduleRoundId, final Long matchingId, List<Map<String, String>> invalidData) {
        Boolean isValid = matchingScheduleRoundRepository.findByIdAndMatchingIdAndStatusCd(matchingScheduleRoundId, matchingId, MatchingScheduleRoundType.OPEN.getCode())
                .isPresent();
        if (!isValid) {
            invalidData.add(Map.of(VALIDATION_EXCEPTION_MATCHING_SCHEDULE_ROUND.getMessage(), matchingId.toString()));
        }
    }

    public void validateMatchingDate(final LocalDate matchingDate, final String matchingTimeType, List<Map<String, String>> invalidData) {
        if (!matchingDate.isAfter(LocalDate.now())
                || (matchingTimeType.equals(MatchingTimeType.AFTERNOON.getCode()) || matchingTimeType.equals(MatchingTimeType.EVENING.getCode()))
        ) {
            invalidData.add(Map.of(VALIDATION_EXCEPTION_MATCHING_DATE.getMessage(), matchingDate + " " + matchingTimeType));
        }
    }

    public void validationMatchingSchedule(final Long id, final List<MatchingScheduleRequestDTO> list) { // 매칭검증
        List<Long> invalidData = new ArrayList<>();
        list.stream().forEach(el -> {
            matchingRepository.findByIdAndRequIdOrRecvId(el.matchingId(), id, id)
                    .orElseGet(() -> {
                        invalidData.add(el.matchingId());
                        return null;
                    });
        });
        if (!invalidData.isEmpty()) {
            throw new BusinessException(VALIDATION_EXCEPTION_MATCHING, invalidData);
        }
    }

    public void validationMatchingScheduleList(final Long id, final List<MatchingScheduleRequestDTO> list) {
    //public List<Long> validationMatchingScheduleList(final Long id, final List<MatchingScheduleRequestDTO> list) {
        List<Long> invalidData = new ArrayList<>();
        //Map<String, String> invalidData = new HashMap<>();
        list.stream().forEach(el -> {
            matchingScheduleRequestedRepository.findByMatchingIdAndUserId(el.matchingId(), id)
                    .orElseGet(() -> {
                        invalidData.add(el.matchingId());
                        return null;
                        //return invalidData.put()//return null;
                    });
        });
        if (!invalidData.isEmpty()) {
            throw new BusinessException(VALIDATION_EXCEPTION_MATCHING_SCHEDULE_REQUEST, invalidData);
            //return null;
        }
        //return invalidData;
    }

    public void validationMatchingScheduleList(final Long id, final MatchingScheduleRequestListDTO list) {
        list.getMatchingScheduleRequestDTOs().stream().forEach(el -> {
            //matchingScheduleRequestedRepository.findBy
        });
    }
}
