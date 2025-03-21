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
import java.util.List;
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
    public void updateMatchingInfo(final long userId, final String authentication, final MatchingVO matchingVO) {
    //public void updateMatchingInfo(final long userId, final MatchingVO matchingVO) {

        if (matchingVO.requId() == userId) { // 요청자 업데이트
            matchingeRepository.findByIdAndRequId(matchingVO.id(), userId)
                    .map(el -> {
                        if (el.getStatusCd().equals("010")) {
                            //el.setMatchPlace(matchingVO.matchPlace());
                        } else if (el.getStatusCd().equals("020")) {
                            //el.setMatchPlace(matchingVO.matchTime());
                        }
                        //el.setRequStatusCd(matchingVO.requStatusCd());
                        return el;
                    }).orElseThrow(() -> new BusinessException(MessageCode.MATCHING_NOT_FOUND));
        } else { // 수신자 업데이트
            matchingeRepository.findByIdAndRecvId(matchingVO.id(), userId)
                    .map(el -> {
                        if (el.getStatusCd().equals("000")) {
                            Optional.ofNullable(userServiceClient.viewCoin(userId, authentication))
                                    .filter(coin -> coin >= 10000L)
                                    .map(coin -> {
                                        userServiceClient.saveCoin(userId, authentication, new UserCoinDTO(String.valueOf(userId), -10000L));
                                        el.setRecvStatusCd("010");
                                        el.setStatusCd("010");
                                        return el;
                                    })
                                    .orElseThrow(() -> new BusinessException(MessageCode.INSUFFICIENT_COIN));
                        }
                        return el;
                    });
        }

        //userServiceClient.saveCoin(userid);
        //matchingeRepository.save(matchingMapper.INSTANCE.matchingVOtoMatching(matchingVO));
    }

    public List<MatchingVO> getMatchingInfos(final long userId) {
        return matchingMapper.INSTANCE.matchingstoMatchingVOs(matchingeRepository.findByRequIdOrRecvIdAndStatusCdNot(userId, userId,"090"));
        //return matchingMapper.INSTANCE.matchingstoMatchingVOs(matchingeRepository.findByRecvIdAndStatusCdNot(userId, "090"));
        //return matchingMapper.INSTANCE.matchingstoMatchingVOs(matchingeRepository.findByRecvIdAndStatusCdNot090(userId));
        //return matchingeRepository.findByRecvIdAndStatusCdNot090(userId);
    }

    public Optional<MatchingVO> getMatchingInfo(final long userId, final long matchId) {
        return Optional.ofNullable(matchingeRepository.findById(matchId).map(el -> {
            return matchingMapper.INSTANCE.matchingtoMatchingVO(el);
        }).orElseThrow(() -> new BusinessException(MessageCode.MATCHING_NOT_FOUND)));
        //return matchingMapper.INSTANCE.matchingtoMatchingVO(matchingeRepository.findById(matchId));
    }

}
