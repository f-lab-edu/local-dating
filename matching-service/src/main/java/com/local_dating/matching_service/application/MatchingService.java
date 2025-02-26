package com.local_dating.matching_service.application;

import com.local_dating.matching_service.domain.entity.Matching;
import com.local_dating.matching_service.domain.mapper.MatchingMapper;
import com.local_dating.matching_service.domain.vo.MatchingVO;
import com.local_dating.matching_service.infrastructure.repository.MatchingeRepository;
import com.local_dating.matching_service.presentation.dto.UserCoinDTO;
import com.local_dating.matching_service.util.MessageCode;
import com.local_dating.matching_service.util.UserServiceClient;
import com.local_dating.matching_service.util.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchingService {

    private final MatchingeRepository matchingeRepository;
    private final MatchingMapper matchingMapper;
    private final UserServiceClient userServiceClient;

    @Transactional
    //public int requestMatching(final long userid, final String authentication, final MatchingVO matchingVO) {
    public void requestMatching(final long userid, final String authentication, final MatchingVO matchingVO) {
/*
        if (userServiceClient.viewCoin(userid, authentication) >= 10000L) {
            userServiceClient.saveCoin(userid, authentication, new UserCoinDTO(String.valueOf(userid), -10000L));

            Matching matching = matchingMapper.INSTANCE.matchingVOtoMatching(matchingVO, "000", userid
                    , LocalDateTime.now(), userid, LocalDateTime.now());
            matchingeRepository.save(matching);
            return 0;
        } else {
            return -1; //실패
        }*/

        Long coinz = userServiceClient.viewCoin(userid, authentication);
        log.info("viewCoin result: {}", coinz); // ✅ 코인 값 확인
        Optional.ofNullable(userServiceClient.viewCoin(userid, authentication))
                .filter(coin -> coin >= 10000L)
                .map(coin -> {
                    userServiceClient.saveCoin(userid, authentication, new UserCoinDTO(String.valueOf(userid), -10000L));

                    Matching matching = matchingMapper.INSTANCE.matchingVOtoMatching(matchingVO, "000", userid
                            , LocalDateTime.now(), userid, LocalDateTime.now());
                    return matchingeRepository.save(matching);
                })
                .orElseThrow(() -> new BusinessException(MessageCode.INSUFFICIENT_COIN));

/*        if (userServiceClient.viewCoin(userid, authentication) >= 10000L) {
            userServiceClient.saveCoin(userid, authentication, new UserCoinDTO(String.valueOf(userid), -10000L));

            Matching matching = matchingMapper.INSTANCE.matchingVOtoMatching(matchingVO, "000", userid
                    , LocalDateTime.now(), userid, LocalDateTime.now());
            matchingeRepository.save(matching);
        } else {
        }*/
    }

    @Transactional
    public void updateMatchingInfo(long userid, MatchingVO matchingVO) {

        //userServiceClient.saveCoin(userid);
        matchingeRepository.save(matchingMapper.INSTANCE.matchingVOtoMatching(matchingVO));
    }

}
