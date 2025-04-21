package com.local_dating.matching_service.application;

import com.local_dating.matching_service.domain.entity.MatchingSchedule;
import com.local_dating.matching_service.domain.mapper.MatchingScheduleMapper;
import com.local_dating.matching_service.domain.vo.MatchingScheduleVO;
import com.local_dating.matching_service.infrastructure.repository.MatchingScheduleRepository;
import com.local_dating.matching_service.util.MessageCode;
import com.local_dating.matching_service.util.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MatchingScheduleService {

    private final MatchingScheduleRepository matchingScheduleRepository;
    private final MatchingScheduleMapper matchingScheduleMapper;

    public List<MatchingScheduleVO> viewSchedule(final Long id, final Long userId) {
        return matchingScheduleMapper.INSTANCE.matchingSchedulesToMatchingScheduleVOs(matchingScheduleRepository.findMatchingSchedulesByIdAndUserId(id, userId));
    }

    @Transactional
    public void saveSchedule(final Long id, final Long userId, final List<MatchingScheduleVO> matchingScheduleVOs) {
        int i = 0;
        for (MatchingScheduleVO vo : matchingScheduleVOs) {
            log.info("i: " + i);
            Optional<MatchingSchedule> matchingSchedule = matchingScheduleRepository.findMatchingScheduleByMatchingIdAndUserIdAndMatchingDateAndMatchingTime(id, userId, vo.matchingDate(), vo.matchingTime());
            if (matchingSchedule.isEmpty()) {
                log.info("없음");
                matchingScheduleRepository.save(matchingScheduleMapper.matchingScheduleVOToMatchingSchedule(vo));
            } else {
                log.info("있음");
            }
        }

        /*log.info("zzz1");
        matchingScheduleVOs.stream().map(el -> {
            log.info("zzz2");
            matchingScheduleRepository.findMatchingScheduleByMatchingIdAndUserIdAndMatchingDateAndMatchingTime(id, userId, el.matchingDate(), el.matchingTime())
                    .map(el2 -> {
                        log.info("zzz3");
                        return el2;
                    })
                    .orElseGet(() -> {
                        log.info("zzzXXXX");
                        return matchingScheduleRepository.save(matchingScheduleMapper.INSTANCE.matchingScheduleVOToMatchingSchedule(el));
                    });
            return el;
        });*/
    }

    @Transactional
    public void updateSchedule(final Long id, final Long userId, final MatchingScheduleVO matchingScheduleVO) {
        matchingScheduleRepository.findMatchingScheduleByIdAndUserId(id, userId).map(el -> {
            el.setMatchingDate(matchingScheduleVO.matchingDate());
            el.setMatchingTime(matchingScheduleVO.matchingTime());
            return el;
        }).orElseThrow(() -> new BusinessException(MessageCode.MATCHING_SCHEDULE_NOT_FOUND));
    }
}
