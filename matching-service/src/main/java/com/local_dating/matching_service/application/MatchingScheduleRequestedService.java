package com.local_dating.matching_service.application;

import com.local_dating.matching_service.domain.entity.MatchingScheduleRequested;
import com.local_dating.matching_service.domain.type.MatchingScheduleRequestedType;
import com.local_dating.matching_service.domain.vo.MatchingScheduleUserCountVO;
import com.local_dating.matching_service.infrastructure.repository.MatchingScheduleRequestedRepository;
import com.local_dating.matching_service.presentation.dto.MatchingScheduleRequestDTO;
import com.local_dating.matching_service.util.MessageCode;
import com.local_dating.matching_service.util.exception.BusinessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import com.local_dating.matching_service.domain.mapper.MatchingScheduleRequestedMapper;

@Service
@RequiredArgsConstructor
public class MatchingScheduleRequestedService {

    private final MatchingScheduleRequestedRepository matchingScheduleRequestedRepository;
    private final MatchingScheduleRequestedMapper matchingScheduleRequestedMapper;

    private final ValidateMatchingService validateMatchingService;

    public List<MatchingScheduleRequested> getMatchingScheduleRequested(final Long match, final Long id, final Long round, final String authorization) {
        return matchingScheduleRequestedRepository.findByMatchingIdAndMatchingScheduleRoundIdAndUserId(match, round, id);
    }

    @Transactional
    public List<MatchingScheduleRequested> saveMatchingScheduleRequested(final Long id, final String authorization, final List<MatchingScheduleRequestDTO> matchingScheduleRequestDTOS) {

        validateMatchingService.validationMatchingSchduleV2(id, matchingScheduleRequestDTOS); // 종합
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

    public void checkRoundSchedule(final MatchingScheduleRequestDTO matchingScheduleRequestDTO) {
    //public void checkRoundSchedule(final Long match, final Long id, final Long round, final String authorization) {
        List<MatchingScheduleUserCountVO> result = matchingScheduleRequestedRepository.matchingScheduleUserCount(matchingScheduleRequestDTO.matchingId(), MatchingScheduleRequestedType.SUBMITTED.getCode());
        List<MatchingScheduleRequestDTO> roundScheduleMatched;
        //List<MatchingScheduleRequested> roundScheduleMatched;
        if (result.size() >= 2) { // 모두 스케줄 제출
            //계산
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
