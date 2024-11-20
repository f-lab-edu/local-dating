package com.local_dating.user_service.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.local_dating.user_service.domain.entity.UserPreference;
import com.local_dating.user_service.domain.entity.UserRecomCard;
import com.local_dating.user_service.domain.mapper.UserPreferenceMapper;
import com.local_dating.user_service.domain.mapper.UserRecomCardMapper;
import com.local_dating.user_service.domain.vo.UserPreferenceCountVO2;
import com.local_dating.user_service.domain.vo.UserRecomCardVO;
import com.local_dating.user_service.infrastructure.repository.UserPreferenceRepositoryCustom;
import com.local_dating.user_service.infrastructure.repository.UserRecomCardRepository;
import com.local_dating.user_service.infrastructure.repository.UserRecomCardRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserCardService {

    private final UserRecomCardRepository recomCardRepository;
    private final UserPreferenceService userPreferenceService;
    private final UserPreferenceRepositoryCustom userPreferenceRepositoryCustom;
    private final ObjectMapper objectMapper;
    private final UserPreferenceMapper userPreferenceMapper;
    private final UserRecomCardMapper userRecomCardMapper;
    private final UserRecomCardRepositoryCustom userRecomCardRepositoryCustom;
    private final Random random = new Random();
    private final UserRecomCardRepository userRecomCardRepository;

    // 현재 사용자의 선호 정보를 바탕으로 사용자 정보를 가져와서 카드로 넣어서 리턴함

    public List<UserRecomCardVO> getCard(final String userId) {

        return userRecomCardMapper.toUserRecomCardVOs(userRecomCardRepositoryCustom.findValidCard(userId));
        //return userRecomCardMapper.toUserRecomCardVO(recomCardRepository.findByUserId(userId));
    }

    public List getCardDetail(final String userId, final String targetId) {
        return userRecomCardRepository.findUserRecomCardWithUserProfile(targetId);
    }

    public void setCard(final String userId) {

        String test = userPreferenceService.viewPreference(userId);
        System.out.println(test);

        List<UserPreference> userPreferenceList1, userPreferenceList2;

        userPreferenceList1 = userPreferenceRepositoryCustom.findMajorPrior(userId);
        userPreferenceList2 =  userPreferenceRepositoryCustom.findMinorPrior(userId);
        Collections.shuffle(userPreferenceList2);
        userPreferenceList2 = userPreferenceList2.subList(0, Math.min(3,userPreferenceList2.size()));

        List<UserPreferenceCountVO2> recommendUser = userPreferenceRepositoryCustom.findRecommendUser(userId, Stream.concat(userPreferenceList1.stream(), userPreferenceList2.stream()).toList());
        //List<UserPreferenceCountVO> recommendUser = userPreferenceRepositoryCustom.findRecommendUser(userId, Stream.concat(userPreferenceList1.stream(), userPreferenceList2.stream()).toList());

        recomCardRepository.save(new UserRecomCard(new UserRecomCardVO(userId, recommendUser.get(random.nextInt(recommendUser.size())).getUserId(), "Y")));


        /*for (UserPreferenceCountVO item : recommendUser) {
            System.out.println("item: " + item.userId() + "    count: " + item.count());
        }*/

        /*try {
            //userPreferenceList = objectMapper.readValue(test, new TypeReference<List<UserPreference>>() {});
            objectMapper.readValue(test, new TypeReference<List<UserPreference>>() {})
                    .stream().forEach(el->{
                        if (el.getPrior() == 1 || el.getPrior() == 2 || el.getPrior() == 3) {

                        }
                    });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }*/

    }

}
