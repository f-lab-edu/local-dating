package com.local_dating.matching_service.application;

import com.local_dating.matching_service.domain.entity.Matching;
import com.local_dating.matching_service.domain.mapper.MatchingMapper;
import com.local_dating.matching_service.domain.vo.MatchingVO;
import com.local_dating.matching_service.infrastructure.repository.MatchingeRepository;
import com.local_dating.matching_service.presentation.dto.UserCoinDTO;
import com.local_dating.matching_service.util.UserServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchingService {

    private final MatchingeRepository matchingeRepository;
    private final MatchingMapper matchingMapper;
    private final UserServiceClient userServiceClient;

    @Transactional
    public int requestMatching(final long userid, final String authentication, final MatchingVO matchingVO) {

        if (userServiceClient.viewCoin(userid, authentication) >= 10000L) {
            userServiceClient.saveCoin(userid, authentication, new UserCoinDTO(String.valueOf(userid), -10000L));

            Matching matching = matchingMapper.INSTANCE.matchingVOtoMatching(matchingVO, "000", userid
                    , LocalDateTime.now(), userid, LocalDateTime.now());
            matchingeRepository.save(matching);
            return 0;
        } else {
            return -1; //실패
        }

        /*Matching matching = matchingMapper.INSTANCE.matchingVOtoMatching(matchingVO, "000", userid
                , LocalDateTime.now(), userid, LocalDateTime.now());*/
        /*
        Matching matching = matchingMapper.INSTANCE.matchingVOtoMatching(matchingVO);
        matching.setStatusCd("000");
        matching.setInUser(userid);
        matching.setModUser(userid);
        matching.setInDate(LocalDateTime.now());
        matching.setModDate(LocalDateTime.now());
        */
        //matchingeRepository.save(matching);
        //matchingeRepository.save(matchingMapper.INSTANCE.matchingVOtoMatching(matchingVO));
    }

    @Transactional
    public void updateMatchingInfo(long userid, MatchingVO matchingVO) {

        //userServiceClient.saveCoin(userid);
        matchingeRepository.save(matchingMapper.INSTANCE.matchingVOtoMatching(matchingVO));
    }

}
