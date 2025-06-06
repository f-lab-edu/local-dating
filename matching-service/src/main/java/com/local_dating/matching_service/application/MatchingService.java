package com.local_dating.matching_service.application;

import com.local_dating.matching_service.domain.entity.Matching;
import com.local_dating.matching_service.domain.mapper.MatchingMapper;
import com.local_dating.matching_service.domain.type.CoinActionType;
import com.local_dating.matching_service.domain.type.MatchingType;
import com.local_dating.matching_service.domain.vo.MatchingVO;
import com.local_dating.matching_service.infrastructure.repository.CoinPolicyRepositoryCustom;
import com.local_dating.matching_service.infrastructure.repository.MatchingeRepository;
import com.local_dating.matching_service.presentation.dto.UserCoinDTO;
import com.local_dating.matching_service.util.MessageCode;
import com.local_dating.matching_service.util.UserServiceClient;
import com.local_dating.matching_service.util.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    private final CoinPolicyRepositoryCustom coinPolicyRepository;

    @Transactional
    //public int requestMatching(final long userid, final String authentication, final MatchingVO matchingVO) {
    //public void requestMatching(final long userid, final String authentication, final MatchingVO matchingVO) {
    public MatchingVO requestMatching(final long userid, final String authentication, final MatchingVO matchingVO) {
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
        return Optional.ofNullable(userServiceClient.viewCoin(userid, authentication))
                .filter(coin -> coin >= Long.valueOf(coinPolicyRepository.getValidPrice(LocalDate.now()).getPrice()))
                .map(coin -> {
                    userServiceClient.saveCoin(userid, authentication, new UserCoinDTO(String.valueOf(userid), -Long.valueOf(coinPolicyRepository.getValidPrice(LocalDate.now()).getPrice()), CoinActionType.CONSUME));

                    Matching matching = matchingMapper.INSTANCE.matchingVOtoMatching(matchingVO, MatchingType.MATCHING_REQUEST.getCode(), userid
                            , LocalDateTime.now(), userid, LocalDateTime.now());
                    return matchingMapper.INSTANCE.matchingtoMatchingVO(matchingeRepository.save(matching));
                })
                .orElseThrow(() -> new BusinessException(MessageCode.INSUFFICIENT_COIN));
    }

    @Transactional
    public void updateMatchingInfo(final long userId, final String authentication, final MatchingVO matchingVO) {

        if (matchingVO.requId() == userId) { // 요청자 업데이트
            this.updateMatchingInfRequ(matchingVO, userId);
        } else { // 수신자 업데이트
            this.updateMatchingInfRecv(matchingVO, userId, authentication);
        }
    }

    /*@Transactional
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
                                        userServiceClient.saveCoin(userId, authentication, new UserCoinDTO(String.valueOf(userId), -10000L, CoinActionType.CONSUME));
                                        el.setRecvStatusCd("010");
                                        el.setStatusCd("010");
                                        return el;
                                    })
                                    .orElseThrow(() -> new BusinessException(MessageCode.INSUFFICIENT_COIN));
                        }
                        return el;
                    });
        }

    }*/

    public List<MatchingVO> getMatchingInfos(final long userId) {
        return matchingMapper.INSTANCE.matchingstoMatchingVOs(matchingeRepository.findByRequIdOrRecvIdAndStatusCdNot(userId, userId,MatchingType.END.getCode()));
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

    public Optional<MatchingVO> getMatchingInfo(final Long matchId) {
        return Optional.ofNullable(matchingeRepository.findById(matchId).map(el -> {
            return matchingMapper.INSTANCE.matchingtoMatchingVO(el);
        }).orElseThrow(() -> new BusinessException(MessageCode.MATCHING_NOT_FOUND)));
    }

    public List<MatchingVO> getReceivedMatches(final long userId) {
        return matchingMapper.INSTANCE.matchingstoMatchingVOs(matchingeRepository.findByRecvIdAndStatusCdNotIn(userId, List.of(MatchingType.END.getCode())));
        //return matchingMapper.INSTANCE.matchingstoMatchingVOs(matchingeRepository.findByRecvIdAndStatusCdNotIn(userId, List.of("CANCELED", "END")));
    }

    public List<MatchingVO> getSentMatches(final long userId) {
        return matchingMapper.INSTANCE.matchingstoMatchingVOs(matchingeRepository.findByRequIdAndStatusCdNotIn(userId, List.of(MatchingType.END.getCode())));
        //return matchingMapper.INSTANCE.matchingstoMatchingVOs(matchingeRepository.findByRequIdAndStatusCdNotIn(userId, List.of("CANCELED", "END")));
    }

    @Transactional
    public MatchingVO acceptMatching(final long userId, final String authentication, final MatchingVO matchingVO) {
        Matching matching = this.setMatchingStatus(userId, matchingVO, MatchingType.MATCHING_ACCEPT);
        minusCoin(userId, authentication);

        return matchingMapper.INSTANCE.matchingtoMatchingVO(matching);
        /*Matching matching = matchingeRepository.findByIdAndRecvId(matchingVO.id(), userId)
                .orElseThrow(() -> new BusinessException(MessageCode.MATCHING_NOT_FOUND));
        minusCoin(userId, authentication);
        matching.setStatusCd(MatchingType.MATCHED.getCode());*/
    }

    @Transactional
    public MatchingVO rejectMatching(final long userId, final String authentication, final MatchingVO matchingVO) {
        Matching matching = this.setMatchingStatus(userId, matchingVO, MatchingType.END);
        retoreCoin(userId, authentication, String.valueOf(matching.getRequId()));

        return matchingMapper.INSTANCE.matchingtoMatchingVO(matching);
        /*Matching matching = matchingeRepository.findByIdAndRecvId(matchingVO.id(), userId)
                .orElseThrow(() -> new BusinessException(MessageCode.MATCHING_NOT_FOUND));
        retoreCoin(userId, authentication, String.valueOf(matching.getRequId()));
        matching.setStatusCd(MatchingType.END.getCode());*/
    }

    /*public void AcceptMatching(final long userId, final String authentication, final MatchingVO matchingVO) {
        matchingeRepository.findByIdAndRecvId(matchingVO.id(), userId)
                .map(el -> {
                    Optional.ofNullable(userServiceClient.viewCoin(userId, authentication))
                            .filter(coin -> coin >= 10000L)
                            .map(coin -> {
                                el.setStatusCd("MATCHED");
                                userServiceClient.updateCoin(userId, authentication, new UserCoinDTO(String.valueOf(userId), -10000L, CoinActionType.CONSUME));
                                return coin;
                            })
                            .orElseThrow(() -> new BusinessException(MessageCode.INSUFFICIENT_COIN));
                    return el;
                })
                .orElseThrow(() -> new BusinessException(MessageCode.MATCHING_NOT_FOUND));
    }
    */

    /*@Transactional
    public void rejectMatching(final long userId, final String authentication, final MatchingVO matchingVO) {
        matchingeRepository.findByIdAndRecvId(matchingVO.id(), userId)
                .map(el -> {
                    el.setStatusCd("END");
                    userServiceClient.updateCoin(userId, authentication, new UserCoinDTO(String.valueOf(el.getRequId()), 10000L, CoinActionType.RESTORE)); // 코인 되돌려주기 requ사용자
                    return el;
                })
                .orElseThrow(() -> new BusinessException(MessageCode.MATCHING_NOT_FOUND));
    }
    */

    public void minusCoin(final long userId, final String authentication) {
        Optional.ofNullable(userServiceClient.viewCoin(userId, authentication))
                .filter(coin -> coin >= Long.valueOf(coinPolicyRepository.getValidPrice(LocalDate.now()).getPrice()))
                //.filter(coin -> coin >= 10000L)
                .map(coin -> {
                    userServiceClient.updateCoin(userId, authentication, new UserCoinDTO(String.valueOf(userId), -Long.valueOf(coinPolicyRepository.getValidPrice(LocalDate.now()).getPrice()), CoinActionType.CONSUME));
                    return coin;
                })
                .orElseThrow(() -> new BusinessException(MessageCode.INSUFFICIENT_COIN));
    }

    public void retoreCoin(final long userId, final String authentication, final String requId) {
        userServiceClient.updateCoin(userId, authentication, new UserCoinDTO(requId, Long.valueOf(coinPolicyRepository.getValidPrice(LocalDate.now()).getPrice()), CoinActionType.RESTORE));
    }

    public void viewSchedule(final long userId, final String authentication, final MatchingVO matchingVO) {

    }

    @Transactional
    public void updateSchedule(final long userId, final String authentication) {


    }

    private Matching setMatchingStatus(final long userId, final MatchingVO matchingVO, final MatchingType matchingType) {
        Matching matching = matchingeRepository.findByIdAndRecvId(matchingVO.id(), userId)
                .orElseThrow(() -> new BusinessException(MessageCode.MATCHING_NOT_FOUND));
        matching.setStatusCd(matchingType.getCode());
        return matching;
    }

    private void updateMatchingInfRequ(final MatchingVO matchingVO, final long userId) {
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
    }

    private void updateMatchingInfRecv(final MatchingVO matchingVO, final long userId, final String authentication) {
        matchingeRepository.findByIdAndRecvId(matchingVO.id(), userId)
                .map(el -> {
                    if (el.getStatusCd().equals(MatchingType.MATCHING_REQUEST.getCode())) {
                        Optional.ofNullable(userServiceClient.viewCoin(userId, authentication))
                                .filter(coin -> coin >= Long.valueOf(coinPolicyRepository.getValidPrice(LocalDate.now()).getPrice()))
                                .map(coin -> {
                                    userServiceClient.saveCoin(userId, authentication, new UserCoinDTO(String.valueOf(userId), -Long.valueOf(coinPolicyRepository.getValidPrice(LocalDate.now()).getPrice()), CoinActionType.CONSUME));
                                    el.setRecvStatusCd(MatchingType.MATCHING_ACCEPT.getCode());
                                    el.setStatusCd(MatchingType.MATCHING_ACCEPT.getCode());
                                    return el;
                                })
                                .orElseThrow(() -> new BusinessException(MessageCode.INSUFFICIENT_COIN));
                    }
                    return el;
                });
    }

}
