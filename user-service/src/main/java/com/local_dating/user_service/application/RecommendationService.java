package com.local_dating.user_service.application;

import com.local_dating.user_service.domain.entity.UserPreference;
import com.local_dating.user_service.domain.entity.UserPreferenceCore;
import com.local_dating.user_service.domain.entity.UserProfile;
import com.local_dating.user_service.domain.entity.UserProfileCore;
import com.local_dating.user_service.infrastructure.repository.UserPreferenceCoreRepository;
import com.local_dating.user_service.infrastructure.repository.UserPreferenceRepository;
import com.local_dating.user_service.infrastructure.repository.UserProfileCoreRepository;
import com.local_dating.user_service.infrastructure.repository.UserProfileRepository;
import com.local_dating.user_service.util.MessageCode;
import com.local_dating.user_service.util.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final UserPreferenceCoreRepository userPreferenceCoreRepository;
    private final UserProfileCoreRepository userProfileCoreRepository;
    private final UserPreferenceRepository userPreferenceRepository;
    private final UserProfileRepository userProfileRepository;

    /*public UserPreferenceCore recommend(final Long id) {
        return userPreferenceCoreRepository.findByUserId(id).orElseThrow(() -> new BusinessException(MessageCode.DATA_NOT_FOUND_EXCEPTION));
    }

    public List<UserProfileCore> searchNextUsers(final Long id, final UserPreferenceCore pref, final int limit) {
        return userProfileCoreRepository.searchNextUsers(id, pref, limit);
    }*/

    @Transactional
    public Map<Long, Integer> getRecommendation(final Long id) {
        //List<Map<Long, Long>> scoreList = new ArrayList<>();
        Map<Long, Integer> weightMap = new HashMap<>();

        UserPreferenceCore userPreferenceCore = userPreferenceCoreRepository.findByUserId(id)
                .orElseThrow(() -> new BusinessException(MessageCode.DATA_NOT_FOUND_EXCEPTION));

        List<UserProfileCore> userProfileCoreList = userProfileCoreRepository.selectCoreMatched(id, userPreferenceCore); //코어조건만족 프로필코어 리스트

        List<UserPreference> userPreferenceList = userPreferenceRepository.findByUserId(id); // 내 선호

        List<Long> coreMatchedId = userProfileCoreList.stream().map(el -> el.getUserId()).collect(Collectors.toUnmodifiableList()); // 코어만족 리스트id

        List<UserProfile> userProfileList = userProfileRepository.findByUserIdIn(coreMatchedId);// 코어만족 리스트id의 프로필

        Map<Long, List<UserProfile>> userProfileMap = userProfileList.stream()
                .collect(Collectors.groupingBy(UserProfile::getUserId));

        AtomicInteger weight = new AtomicInteger();
        userProfileMap.forEach((userId, profiles) -> {
            weight.set(0);
            profiles.forEach(profile -> {
                userPreferenceList.stream().forEach(el -> {
                    if (el.getPrefCd().equals(profile.getInfoCd()) && el.getPrefVal().equals(profile.getInfoVal())) {
                        this.calcWeight(weight, el.getPrior());
                        /*if (el.getPrior() == 1) {
                            weight.addAndGet(50);
                        } else if (el.getPrior() == 2) {
                            weight.addAndGet(40);
                        } else if (el.getPrior() == 3) {
                            weight.addAndGet(30);
                        } else {
                            weight.addAndGet(10);
                        }*/
                    }
                });

            });
            weightMap.put(userId, weight.get()); // 유저 가중치 완성

        });

        return weightMap;
    }

    private void calcWeight(AtomicInteger weight, int prior) {
        if (prior == 1) {
            weight.addAndGet(50);
        } else if (prior == 2) {
            weight.addAndGet(40);
        } else if (prior == 3) {
            weight.addAndGet(30);
        } else {
            weight.addAndGet(10);
        }
    }

}
