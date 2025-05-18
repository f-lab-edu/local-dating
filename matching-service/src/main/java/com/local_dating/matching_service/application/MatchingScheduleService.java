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
import java.util.stream.Collectors;

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
    public List<MatchingScheduleVO> saveSchedule(final Long id, final Long userId, final List<MatchingScheduleVO> matchingScheduleVOs) {

        List<MatchingSchedule> matchingScheduleList = matchingScheduleVOs.stream()
                .map(el -> matchingScheduleRepository
                        .findMatchingScheduleByMatchingIdAndUserIdAndMatchingDateAndMatchingTime(id, userId, el.matchingDate(), el.matchingTime())
                        .orElseGet(() -> matchingScheduleRepository.save(matchingScheduleMapper.INSTANCE.matchingScheduleVOToMatchingSchedule(el))
                        )
                ).collect(Collectors.toUnmodifiableList());

        checkMatchingSchedule(id);

        return matchingScheduleMapper.INSTANCE.matchingSchedulesToMatchingScheduleVOs(matchingScheduleList);

        /*
        matchingScheduleVOs.stream().forEach(el -> {
            matchingScheduleRepository.findMatchingScheduleByMatchingIdAndUserIdAndMatchingDateAndMatchingTime(id, userId, el.matchingDate(), el.matchingTime())
                    .map(el2 -> {
                        return el2;
                    })
                    .orElseGet(() -> {
                        return matchingScheduleRepository.save(matchingScheduleMapper.INSTANCE.matchingScheduleVOToMatchingSchedule(el));
                    });
        });
         */
    }

    /*@Transactional
    public void saveSchedule(final Long id, final Long userId, final List<MatchingScheduleVO> matchingScheduleVOs) {

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
    }*/

    @Transactional
    public MatchingScheduleVO updateSchedule(final Long id, final Long userId, final MatchingScheduleVO matchingScheduleVO) {
        MatchingSchedule matchingSchedule = matchingScheduleRepository.findMatchingScheduleByIdAndUserId(id, userId).map(el -> {
            el.setMatchingDate(matchingScheduleVO.matchingDate());
            el.setMatchingTime(matchingScheduleVO.matchingTime());
            return el;
        }).orElseThrow(() -> new BusinessException(MessageCode.MATCHING_SCHEDULE_NOT_FOUND));

        checkMatchingSchedule(id);

        return matchingScheduleMapper.INSTANCE.matchingScheduleToMatchingScheduleVO(matchingSchedule);
    }

    @Transactional(propagation = Propagation.NESTED)
    public void checkMatchingSchedule(final Long id) {

        Optional<MatchingVO> matchingVO = matchingService.getMatchingInfo(id);

        List<MatchingSchedule> scheduleVO1;
        scheduleVO1 = matchingScheduleRepository.findMatchingSchedulesByMatchingIdAndUserId(id, matchingVO.get().requId()); //매칭요청자가 보낸 스케줄
        List<MatchingSchedule> scheduleVO2;
        scheduleVO2 = matchingScheduleRepository.findMatchingSchedulesByMatchingIdAndUserId(id, matchingVO.get().recvId()); //매칭수락자가 보낸 스케줄

        List<LocalDate> matchingDateList1 = this.getMatchingSchedule(id, matchingVO.get().requId(), scheduleVO1);
        List<LocalDate> matchingDateList2 = this.getMatchingSchedule(id, matchingVO.get().recvId(), scheduleVO2);

        matchingDateList1.retainAll(matchingDateList2);

        if (!matchingDateList1.isEmpty()) {
            scheduleVO1.stream().filter(el -> matchingDateList1.contains(el.getMatchingDate()))
                    .forEach(el -> el.setScheduleStatus(MatchingType.SAME.getCode()));
            scheduleVO2.stream().filter(el -> matchingDateList1.contains(el.getMatchingDate()))
                    .forEach(el -> el.setScheduleStatus(MatchingType.SAME.getCode()));
        }

    }

    /*@Transactional(propagation = Propagation.NESTED)
    public void checkMatchingSchedule(final Long id) {

        List<MatchingSchedule> scheduleVO1;
        List<MatchingSchedule> scheduleVO2;

        List<LocalDate> matchingDateList1 = new ArrayList<>();
        List<LocalDate> matchingDateList2 = new ArrayList<>();

        Optional<MatchingVO> matchingVO = matchingService.getMatchingInfo(id);

        scheduleVO1 = matchingScheduleRepository.findMatchingSchedulesByMatchingIdAndUserId(id, matchingVO.get().requId()); //매칭요청자가 보낸 스케줄
        scheduleVO2 = matchingScheduleRepository.findMatchingSchedulesByMatchingIdAndUserId(id, matchingVO.get().recvId()); //매칭수락자가 보낸 스케줄
        *//*scheduleVO1 = matchingScheduleRepository.findMatchingSchedulesByIdAndUserId(id, matchingVO.get().requId());
        scheduleVO2 = matchingScheduleRepository.findMatchingSchedulesByIdAndUserId(id, matchingVO.get().requId());*//*

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

    }*/

    private List<LocalDate> getMatchingSchedule(final Long id, final Long userId, final List<MatchingSchedule> scheduleVO) {
    //private List<LocalDate> getMatchingSchedule(final Long id, final Long userId) {

        //List<MatchingSchedule> scheduleVO;
        //scheduleVO = matchingScheduleRepository.findMatchingSchedulesByMatchingIdAndUserId(id, userId);

        List<LocalDate> matchingDateList = new ArrayList<>();
        scheduleVO.stream().forEach(el -> matchingDateList.add(el.getMatchingDate()));

        return matchingDateList;
    }

    @Transactional
    public MatchingScheduleVO requestMatchingSchedule(final Long id, final Long scheduleId) {
        return this.setScheduleStatus(id, scheduleId, MatchingType.SAME, MatchingType.REQUEST);
        /*matchingScheduleRepository.findMatchingScheduleByIdAndScheduleIdAndScheduleStatus(id, scheduleId, MatchingType.SAME.getCode())
                //matchingScheduleRepository.findMatchingScheduleByIdAndScheduleId(id, scheduleId)
                .map(el -> {
                    el.setScheduleStatus(MatchingType.REQUEST.getCode());
                    return el;
                }).orElseThrow(() -> new BusinessException(MessageCode.MATCHING_SCHEDULE_NOT_FOUND));*/
    }

    @Transactional
    public MatchingScheduleVO acceptMatchingSchedule(final Long id, final Long scheduleId) {
        return this.setScheduleStatus(id, scheduleId, MatchingType.REQUEST, MatchingType.MATCHED);
        /*matchingScheduleRepository.findMatchingScheduleByIdAndScheduleIdAndScheduleStatus(id, scheduleId, MatchingType.REQUEST.getCode())
                .map(el -> {
                    el.setScheduleStatus(MatchingType.MATCHED.getCode());
                    return el;
                }).orElseThrow(() -> new BusinessException(MessageCode.MATCHING_SCHEDULE_NOT_FOUND));*/
    }

    @Transactional
    public MatchingScheduleVO rejectMatchingSchedule(final Long id, final Long scheduleId) {
    //public void rejectMatchingSchedule(final Long id, final Long scheduleId) {
        return this.setScheduleStatus(id, scheduleId, MatchingType.SAME, MatchingType.END);
        /*matchingScheduleRepository.findMatchingScheduleByIdAndScheduleIdAndScheduleStatus(id, scheduleId, MatchingType.SAME.getCode())
                .map(el -> {
                    el.setScheduleStatus(MatchingType.END.getCode());
                    return el;
                }).orElseThrow(() -> new BusinessException(MessageCode.MATCHING_SCHEDULE_NOT_FOUND));*/
    }

    private MatchingScheduleVO setScheduleStatus(final Long id, final Long scheduleId, final MatchingType checkType, final MatchingType setType) {
        return matchingScheduleRepository.findMatchingScheduleByIdAndScheduleIdAndScheduleStatus(id, scheduleId, checkType.getCode())
                .map(el -> {
                    el.setScheduleStatus(setType.getCode());
                    return matchingScheduleMapper.INSTANCE.matchingScheduleToMatchingScheduleVO(el);
                }).orElseThrow(() -> new BusinessException(MessageCode.MATCHING_SCHEDULE_NOT_FOUND));
    }
    /*private void setScheduleStatus(final Long id, final Long scheduleId, final MatchingType checkType, final MatchingType setType) {
        matchingScheduleRepository.findMatchingScheduleByIdAndScheduleIdAndScheduleStatus(id, scheduleId, checkType.getCode())
                .map(el -> {
                    el.setScheduleStatus(setType.getCode());
                    return el;
                }).orElseThrow(() -> new BusinessException(MessageCode.MATCHING_SCHEDULE_NOT_FOUND));
    }*/
}
