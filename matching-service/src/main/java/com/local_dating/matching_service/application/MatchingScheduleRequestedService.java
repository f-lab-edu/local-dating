package com.local_dating.matching_service.application;

import com.local_dating.matching_service.application.validation.MatchingScheduleRequestValidator;
import com.local_dating.matching_service.domain.entity.MatchingScheduleRequested;
import com.local_dating.matching_service.domain.entity.MatchingScheduleRound;
import com.local_dating.matching_service.domain.mapper.MatchingScheduleRequestedMapper;
import com.local_dating.matching_service.domain.type.MatchingScheduleRequestedType;
import com.local_dating.matching_service.domain.vo.MatchingScheduleUserCountVO;
import com.local_dating.matching_service.infrastructure.repository.MatchingScheduleRequestedRepository;
import com.local_dating.matching_service.infrastructure.repository.MatchingScheduleRoundRepository;
import com.local_dating.matching_service.presentation.dto.MatchingScheduleRequestDTO;
import com.local_dating.matching_service.util.MessageCode;
import com.local_dating.matching_service.util.exception.BusinessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchingScheduleRequestedService {

    private final MatchingScheduleRequestedRepository matchingScheduleRequestedRepository;
    private final MatchingScheduleRequestedMapper matchingScheduleRequestedMapper;

    private final MatchingScheduleRequestValidator matchingScheduleRequestValidator;

    private final MatchingScheduleRoundRepository matchingScheduleRoundRepository;

    public List<MatchingScheduleRequested> getMatchingScheduleRequested(final Long match, final Long id, final Long round, final String authorization) {
        return matchingScheduleRequestedRepository.findByMatchingIdAndMatchingScheduleRoundIdAndUserId(match, round, id);
    }

    @Transactional
    public List<MatchingScheduleRequested> saveMatchingScheduleRequested(final Long id, final String authorization, final List<MatchingScheduleRequestDTO> matchingScheduleRequestDTOS) {

        matchingScheduleRequestValidator.validationMatchingSchduleV3(id, matchingScheduleRequestDTOS); // 종합 수정
        //validateMatchingService.validationMatchingSchduleV2(id, matchingScheduleRequestDTOS); // 종합
        //validateMatchingService.validationMatchingSchedule(id, matchingScheduleRequestDTOS); // 1개만 보는거
        //validateMatchingService.validationMatchingScheduleList(id, matchingScheduleRequestDTOS);
        //validateMatchingScheduleList(id, matchingScheduleRequestDTOS);

        List duplicatedData = matchingScheduleRequestDTOS.stream().filter(el -> matchingScheduleRequestedRepository
                .findDuplicatedData(
                //.findByMatchingIdAndMatchingScheduleRoundIdAndUserIdAndMatchingDateAndMatchingTimeTypeAndStatusCd(
                        el.matchingId()
                        , el.matchingScheduleRoundId()
                        , el.userId()
                        , el.matchingDate()
                        , el.matchingTimeType()
                        , el.statusCd()
                ).isPresent()).collect(Collectors.toUnmodifiableList());
        if (!duplicatedData.isEmpty()) {
            throw new BusinessException(MessageCode.DATA_ALREADY_EXISTS_EXCEPTION);
        }
        return matchingScheduleRequestDTOS.stream()
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

        /*return matchingScheduleRequestDTOS.stream()
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
                .collect(Collectors.toUnmodifiableList());*/

        /*matchingScheduleRequestDTOS.stream().filter(el -> matchingScheduleRequestedRepository)

        matchingScheduleRequestedRepository.save(new MatchingScheduleRequested(
                matchingScheduleRequestDTO.matchingId()
                , matchingScheduleRequestDTO.matchingScheduleRoundId()
                , matchingScheduleRequestDTO.userId()
                , matchingScheduleRequestDTO.matchingDate()
                , matchingScheduleRequestDTO.matchingTimeType()
                , matchingScheduleRequestDTO.statusCd()));*/
    }

    @Transactional
    public void checkRoundSchedule(final MatchingScheduleRequestDTO matchingScheduleRequestDTO) {
    //public void checkRoundSchedule(final Long match, final Long id, final Long round, final String authorization) {
        List<MatchingScheduleUserCountVO> result = matchingScheduleRequestedRepository.matchingScheduleUserCount(matchingScheduleRequestDTO.matchingId(), MatchingScheduleRequestedType.SUBMITTED.getCode());
        List<MatchingScheduleRequested> roundScheduleMatched = new ArrayList<>();
        //List<MatchingScheduleRequestDTO> roundScheduleMatched;

        List<MatchingScheduleRequested> roundScheduleRequested1;
        List<MatchingScheduleRequested> roundScheduleRequested2;
        //List<MatchingScheduleRequested> roundScheduleMatched;
        if (result.size() == 2) { // 모두 스케줄 제출
            //계산
            roundScheduleRequested1 = matchingScheduleRequestedRepository
                    .findByMatchingIdAndStatusCdAndUserId(matchingScheduleRequestDTO.matchingId(), MatchingScheduleRequestedType.SUBMITTED.getCode(), result.get(0).userId());
            roundScheduleRequested2 = matchingScheduleRequestedRepository
                    .findByMatchingIdAndStatusCdAndUserId(matchingScheduleRequestDTO.matchingId(), MatchingScheduleRequestedType.SUBMITTED.getCode(), result.get(1).userId());

            for (MatchingScheduleRequested el1 : roundScheduleRequested1)  {
                for (MatchingScheduleRequested el2 : roundScheduleRequested2) {
                    if (isSameSchedule(el1, el2)) {
                        roundScheduleMatched.add(el1);
                        break;
                    }
                }
            }

            /*roundScheduleRequested1.stream().forEach(el -> {
                roundScheduleRequested2.stream().forEach(el2 -> {
                    if (isSameSchedule(el, el2)) {
                        roundScheduleMatched.add(el);
                    }
                });
            })*/;

            if (roundScheduleMatched.size() > 0) {
                // 1개 고르기
                MatchingScheduleRequested selectedRequestedDay = roundScheduleMatched.get(ThreadLocalRandom.current().nextInt(roundScheduleMatched.size()));

                // 라운드 교집합 저장
                MatchingScheduleRound selectedRoundDay = matchingScheduleRoundRepository.findByMatchingIdAndRound(selectedRequestedDay.getMatchingId(), selectedRequestedDay.getMatchingScheduleRoundId())
                        .orElseThrow(() -> new BusinessException(MessageCode.MATCHING_NOT_FOUND, "matchingId: " + selectedRequestedDay.getMatchingId() + "/ matchingScheduleRoundId: " + selectedRequestedDay.getMatchingScheduleRoundId()));
                selectedRoundDay.setMatchingDate(selectedRequestedDay.getMatchingDate());
                selectedRoundDay.setMatchingTimeType(selectedRequestedDay.getMatchingTimeType());

                // 기존 요청목록 상태변경
                roundScheduleRequested1.stream().filter(el -> el.getMatchingId().equals(selectedRequestedDay.getMatchingId())
                        && el.getMatchingScheduleRoundId().equals(selectedRequestedDay.getMatchingScheduleRoundId())
                                && el.getMatchingDate().equals(selectedRequestedDay.getMatchingDate())
                        && el.getMatchingTimeType().equals(selectedRequestedDay.getMatchingTimeType()))
                        .forEach(el -> el.setStatusCd(MatchingScheduleRequestedType.CONFIRMED.getCode()));

                roundScheduleRequested2.stream().filter(el -> el.getMatchingId().equals(selectedRequestedDay.getMatchingId())
                                && el.getMatchingScheduleRoundId().equals(selectedRequestedDay.getMatchingScheduleRoundId())
                                && el.getMatchingDate().equals(selectedRequestedDay.getMatchingDate())
                                && el.getMatchingTimeType().equals(selectedRequestedDay.getMatchingTimeType()))
                        .forEach(el -> el.setStatusCd(MatchingScheduleRequestedType.CONFIRMED.getCode()));
            }



            //roundScheduleMatched = this.calcRoundScheduleInter(matchingScheduleRequestDTO.matchingId(), MatchingScheduleRequestedType.SUBMITTED.getCode(), result.get(0).userId(), result.get(1).userId());

            /*roundScheduleMatched.stream().forEach(el -> {
                matchingScheduleRequestedRepository.findMatchingScheduleRequestedData(
                        el.matchingId()
                        , el.matchingScheduleRoundId()
                        , el.matchingDate()
                        , el.matchingTimeType()
                ).map(el2 -> el2.setStatusCd(MatchingScheduleRequestedType.CONFIRMED.getCode())
                return el2;)
                );
                //el.setStatusCd(MatchingScheduleRequestedType.CONFIRMED.getCode());
            });*/



        }
    }

    private Boolean isSameSchedule(final MatchingScheduleRequested val1, final MatchingScheduleRequested val2) {
        if (val1.getMatchingId().equals(val2.getMatchingId())
                && val1.getMatchingScheduleRoundId().equals(val2.getMatchingScheduleRoundId())
                && val1.getMatchingDate().equals(val2.getMatchingDate())
                && val1.getMatchingTimeType().equals(val2.getMatchingTimeType())) {
            return true;
        }
        return false;
    }

    /* 임시주석
    private List<MatchingScheduleRequestDTO> calcRoundScheduleInter(final Long matchingId, final String statusCd, final Long userId1, final Long userId2) {
    //private List<MatchingScheduleRequested> calcRoundScheduleInter(final Long matchingId, final String statusCd, final Long userId1, final Long userId2) {
    //private void calcRoundScheduleInter(final Long matchingId, final String statusCd, final Long userId1, final Long userId2) {

        List<MatchingScheduleRequestDTO> matchingScheduleRequested1 = matchingScheduleRequestedMapper
                .entityListToDtoList(matchingScheduleRequestedRepository.findByMatchingIdAndStatusCdAndUserId(matchingId, statusCd, userId1));
        List<MatchingScheduleRequestDTO> matchingScheduleRequested2 = matchingScheduleRequestedMapper
                .entityListToDtoList(matchingScheduleRequestedRepository.findByMatchingIdAndStatusCdAndUserId(matchingId, statusCd, userId2));
        //List<MatchingScheduleRequested> matchingScheduleRequested1 = matchingScheduleRequestedRepository.findByMatchingIdAndStatusCdAndUserId(matchingId, statusCd, userId1);
        //List<MatchingScheduleRequested> matchingScheduleRequested2 = matchingScheduleRequestedRepository.findByMatchingIdAndStatusCdAndUserId(matchingId, statusCd, userId2);

        return matchingScheduleRequested1.stream().filter(el -> matchingScheduleRequested2.stream()
                .anyMatch(el2 -> el2.getMatchingId().equals(el.getMatchingId())
                        && el2.getMatchingScheduleRoundId().equals(el.getMatchingScheduleRoundId())
                        && el2.getMatchingDate().equals(el.getMatchingDate())
                        && el2.getMatchingTimeType().equals(el.getMatchingTimeType())
                )
        ).collect(Collectors.toUnmodifiableList());
    }*/

}
