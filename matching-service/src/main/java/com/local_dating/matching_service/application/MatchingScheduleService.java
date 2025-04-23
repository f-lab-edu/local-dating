package com.local_dating.matching_service.application;

import com.local_dating.matching_service.domain.entity.MatchingSchedule;
import com.local_dating.matching_service.domain.mapper.MatchingScheduleMapper;
import com.local_dating.matching_service.domain.type.MatchingType;
import com.local_dating.matching_service.domain.vo.MatchingScheduleVO;
import com.local_dating.matching_service.domain.vo.MatchingVO;
import com.local_dating.matching_service.infrastructure.repository.MatchingScheduleRepository;
import com.local_dating.matching_service.util.MessageCode;
import com.local_dating.matching_service.util.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MatchingScheduleService {

    private final MatchingScheduleRepository matchingScheduleRepository;
    private final MatchingScheduleMapper matchingScheduleMapper;
    private final MatchingService matchingService;

    public List<MatchingScheduleVO> viewSchedule(final Long id, final Long userId) {
        return matchingScheduleMapper.INSTANCE.matchingSchedulesToMatchingScheduleVOs(matchingScheduleRepository.findMatchingSchedulesByMatchingIdAndUserId(id, userId));
    }

    public List<MatchingScheduleVO> viewScheduleSame(final Long matchingId, final Long userId) {
        return matchingScheduleMapper.INSTANCE.matchingSchedulesToMatchingScheduleVOs(matchingScheduleRepository.findMatchingSchedulesByMatchingIdAndScheduleStatus(matchingId, MatchingType.SAME.getCode()));
        //return matchingScheduleMapper.INSTANCE.matchingSchedulesToMatchingScheduleVOs(matchingScheduleRepository.findMatchingSchedulesByMatchingIdAndUserIdAndScheduleStatus(matchingId, userId, MatchingType.SAME.getCode()));
    }

    @Transactional
    public void saveSchedule(final Long id, final Long userId, final List<MatchingScheduleVO> matchingScheduleVOs) {
        /*int i = 0;
        for (MatchingScheduleVO vo : matchingScheduleVOs) {
            log.info("i: " + i);
            Optional<MatchingSchedule> matchingSchedule = matchingScheduleRepository.findMatchingScheduleByMatchingIdAndUserIdAndMatchingDateAndMatchingTime(id, userId, vo.matchingDate(), vo.matchingTime());
            if (matchingSchedule.isEmpty()) {
                log.info("없음");
                matchingScheduleRepository.save(matchingScheduleMapper.matchingScheduleVOToMatchingSchedule(vo));
            } else {
                log.info("있음");
            }
        }*/

        matchingScheduleVOs.stream().forEach(el -> {
            matchingScheduleRepository.findMatchingScheduleByMatchingIdAndUserIdAndMatchingDateAndMatchingTime(id, userId, el.matchingDate(), el.matchingTime())
                    .map(el2 -> {
                        return el2;
                    })
                    .orElseGet(() -> {
                        return matchingScheduleRepository.save(matchingScheduleMapper.INSTANCE.matchingScheduleVOToMatchingSchedule(el));
                    });
        });

        checkMatchingSchedule(id);
    }

    @Transactional
    public void updateSchedule(final Long id, final Long userId, final MatchingScheduleVO matchingScheduleVO) {
        matchingScheduleRepository.findMatchingScheduleByIdAndUserId(id, userId).map(el -> {
            el.setMatchingDate(matchingScheduleVO.matchingDate());
            el.setMatchingTime(matchingScheduleVO.matchingTime());
            return el;
        }).orElseThrow(() -> new BusinessException(MessageCode.MATCHING_SCHEDULE_NOT_FOUND));

        checkMatchingSchedule(id);
    }

    @Transactional(propagation = Propagation.NESTED)
    public void checkMatchingSchedule(final Long id) {

        List<MatchingSchedule> scheduleVO1;
        List<MatchingSchedule> scheduleVO2;

        List<LocalDate> matchingDateList1 = new ArrayList<>();
        List<LocalDate> matchingDateList2 = new ArrayList<>();

        Optional<MatchingVO> matchingVO = matchingService.getMatchingInfo(id);

        scheduleVO1 = matchingScheduleRepository.findMatchingSchedulesByMatchingIdAndUserId(id, matchingVO.get().requId()); //매칭요청자가 보낸 스케줄
        scheduleVO2 = matchingScheduleRepository.findMatchingSchedulesByMatchingIdAndUserId(id, matchingVO.get().recvId()); //매칭수락자가 보낸 스케줄
        /*scheduleVO1 = matchingScheduleRepository.findMatchingSchedulesByIdAndUserId(id, matchingVO.get().requId());
        scheduleVO2 = matchingScheduleRepository.findMatchingSchedulesByIdAndUserId(id, matchingVO.get().requId());*/

        scheduleVO1.stream().forEach(el -> matchingDateList1.add(el.getMatchingDate()));
        scheduleVO2.stream().forEach(el -> matchingDateList2.add(el.getMatchingDate()));

        matchingDateList1.retainAll(matchingDateList2);
        //scheduleVO1.retainAll(scheduleVO2);

        if (!matchingDateList1.isEmpty()) {
            scheduleVO1.stream().filter(el -> matchingDateList1.contains(el.getMatchingDate()))
                    .forEach(el -> el.setScheduleStatus(MatchingType.SAME.getCode()));
            scheduleVO2.stream().filter(el -> matchingDateList2.contains(el.getMatchingDate()))
                    .forEach(el -> el.setScheduleStatus(MatchingType.SAME.getCode()));
        }

        /*
        matchingService.getMatchingInfo(id).map(el->{
            scheduleVO1 = matchingScheduleRepository.findMatchingScheduleByIdAndUserId(id, el.requId());
            scheduleVO2 = matchingScheduleRepository.findMatchingScheduleByIdAndUserId(id, el.recvId());
            return el;
        });
        */
    }

    @Transactional
    public void requestMatchingSchedule(final Long id, final Long scheduleId) {
        matchingScheduleRepository.findMatchingScheduleByIdAndScheduleIdAndScheduleStatus(id, scheduleId, MatchingType.SAME.getCode())
                //matchingScheduleRepository.findMatchingScheduleByIdAndScheduleId(id, scheduleId)
                .map(el -> {
                    el.setScheduleStatus(MatchingType.REQUEST.getCode());
                    return el;
                }).orElseThrow(() -> new BusinessException(MessageCode.MATCHING_SCHEDULE_NOT_FOUND));
    }

    @Transactional
    public void acceptMatchingSchedule(final Long id, final Long scheduleId) {
        matchingScheduleRepository.findMatchingScheduleByIdAndScheduleIdAndScheduleStatus(id, scheduleId, MatchingType.REQUEST.getCode())
                .map(el -> {
                    el.setScheduleStatus(MatchingType.MATCHED.getCode());
                    return el;
                }).orElseThrow(() -> new BusinessException(MessageCode.MATCHING_SCHEDULE_NOT_FOUND));
        //변경
    }

    @Transactional
    public void rejectMatchingSchedule(final Long id, final Long scheduleId) {
        matchingScheduleRepository.findMatchingScheduleByIdAndScheduleIdAndScheduleStatus(id, scheduleId, MatchingType.SAME.getCode())
                .map(el -> {
                    el.setScheduleStatus(MatchingType.END.getCode());
                    return el;
                }).orElseThrow(() -> new BusinessException(MessageCode.MATCHING_SCHEDULE_NOT_FOUND));
    }
}
