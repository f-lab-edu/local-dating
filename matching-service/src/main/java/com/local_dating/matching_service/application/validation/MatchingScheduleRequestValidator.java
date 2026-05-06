package com.local_dating.matching_service.application.validation;

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
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.local_dating.matching_service.util.MessageCode.*;

@Component
@RequiredArgsConstructor
public class MatchingScheduleRequestValidator {

    private final MatchingRepository matchingRepository;
    private final MatchingScheduleRoundRepository matchingScheduleRoundRepository;
    private final MatchingScheduleRequestedRepository matchingScheduleRequestedRepository;

    public void validationMatchingSchduleV3(final Long id, final List<MatchingScheduleRequestDTO> list) {
        List<Map<String, String>> invalidData = new ArrayList<>();

        List<Long> matchingIds = list.stream().map(el -> el.matchingId())
                .distinct().collect(Collectors.toUnmodifiableList());
        List<Long> matchingScheduleRoundIds = list.stream().map(el -> el.matchingScheduleRoundId())
                .distinct().collect(Collectors.toUnmodifiableList());

        Set<Long> validMatchingIds = matchingRepository.findAllByIdsAndUserId(matchingIds, id)
                .stream().map(el -> el.getId())
                .collect(Collectors.toUnmodifiableSet());
        Set<String> validMatchingRoundKeys = matchingScheduleRoundRepository
                .findByIdInAndMatchingIdInAndStatusCd(matchingScheduleRoundIds, matchingIds, MatchingScheduleRoundType.OPEN.getCode())
                .stream().map(el -> getMatchingRoundKey(el.getId(), el.getMatchingId()))
                .collect(Collectors.toUnmodifiableSet());

        list.forEach(el -> {
            if (!validateMatchingIdV2(el.matchingId(), validMatchingIds)) {
                invalidData.add(Map.of(VALIDATION_EXCEPTION_MATCHING_ID.getMessage(), el.matchingId().toString()));
            }
            if (!validateMatchingRoundId(el.matchingScheduleRoundId(), el.matchingId(), validMatchingRoundKeys)) {
                invalidData.add(Map.of(VALIDATION_EXCEPTION_MATCHING_SCHEDULE_ROUND.getMessage(), el.matchingId().toString()));
            }
            if (!validateMatchingDateV2(el.matchingDate(), el.matchingTimeType())) {
                invalidData.add(Map.of(VALIDATION_EXCEPTION_MATCHING_DATE.getMessage(), el.matchingDate() + " " + el.matchingTimeType()));
            }
            if (MatchingScheduleRequestedType.SUBMITTED.getCode().equals(el.statusCd())) {
                invalidData.add(Map.of(VALIDATION_EXCEPTION_MATCHING_SCHEDULE_REQUEST.getMessage(), el.statusCd()));
            }
        });

        if (!invalidData.isEmpty()) {
            throw new BusinessException(VALIDATION_EXCEPTION_MATCHING_SCHEDULE_REQUEST, invalidData.toString());
        }

    }

    private String getMatchingRoundKey(final Long matchingScheduleRoundId, final Long matchingId) {
        return matchingScheduleRoundId + ":" + matchingId; // 문자열로 라운드id / 매칭id 쌍 저장
    }

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

    public Boolean validateMatchingIdV2(final Long matchingId, final Set<Long> validMatchingIds) {
        return validMatchingIds.contains(matchingId);
    }

    public void validateMatchingId(final Long userId, final Long matchingId, List<Map<String, String>> invalidData) {
        Boolean isValid = matchingRepository.findByIdAndRequIdOrRecvId(matchingId, userId, userId)
                .isPresent();
        if (!isValid) {
            invalidData.add(Map.of(VALIDATION_EXCEPTION_MATCHING_ID.getMessage(), matchingId.toString()));
        }
    }

    public Boolean validateMatchingRoundId(final Long matchingScheduleRoundId, final Long matchingId, final Set<String> validMatchingRoundKeys) {
        return validMatchingRoundKeys.contains(getMatchingRoundKey(matchingScheduleRoundId, matchingId));
    }

    public void validateMatchingRoundId(final Long matchingScheduleRoundId, final Long matchingId, List<Map<String, String>> invalidData) {
        Boolean isValid = matchingScheduleRoundRepository.findByIdAndMatchingIdAndStatusCd(matchingScheduleRoundId, matchingId, MatchingScheduleRoundType.OPEN.getCode())
                .isPresent();
        if (!isValid) {
            invalidData.add(Map.of(VALIDATION_EXCEPTION_MATCHING_SCHEDULE_ROUND.getMessage(), matchingId.toString()));
        }
    }

    public Boolean validateMatchingDateV2(
            final LocalDate matchingDate,
            final String matchingTimeType
    ) {
        return matchingDate.isAfter(LocalDate.now())
                && !(
                MatchingTimeType.AFTERNOON.getCode().equals(matchingTimeType)
                        || MatchingTimeType.EVENING.getCode().equals(matchingTimeType)
        );
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
