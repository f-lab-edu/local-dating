package com.local_dating.user_service.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.local_dating.user_service.domain.entity.UserPreference;
import com.local_dating.user_service.domain.mapper.UserPreferenceMapper;
import com.local_dating.user_service.domain.vo.UserPreferenceVO;
import com.local_dating.user_service.infrastructure.repository.UserPreferenceRepository;
import com.local_dating.user_service.util.MessageCode;
import com.local_dating.user_service.util.exception.DataNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserPreferenceService {

    private final UserPreferenceRepository userPreferenceRepository;
    private final UserPreferenceMapper userPreferenceMapper;
    private final ObjectMapper objectMapper;

    /*public UserPreferenceService(UserPreferenceRepository userPreferenceRepository, UserPreferenceMapper userPreferenceMapper, ObjectMapper objectMapper) {
        this.userPreferenceRepository = userPreferenceRepository;
        this.userPreferenceMapper = userPreferenceMapper;
        this.objectMapper = objectMapper;
    }*/

    @Transactional
    @CacheEvict(value = "preference", key = "#userId")
    public List<UserPreference> savePreferences(final String userId, final List<UserPreferenceVO> userPreferenceVOList) {
        //public void savePreferences(String user_id, List<UserPreferenceVO> userPreferenceVOList) {

        /*return userPreferenceVOList.stream()
                .map(el -> userPreferenceRepository.findByUserIdAndPrefCd(userId, el.prefCd())
                        .map(el2 -> {
                            throw new DataAlreadyExistsException(MessageCode.DATA_ALREADY_EXISTS_EXCEPTION.getMessage() + " / " + userId + " / " + el2.getPrefCd() + " / " );
                            //return e
                        })
                        .orElseGet(() -> userPreferenceRepository.save(new UserPreference(el)))
                )
                .collect(Collectors.toUnmodifiableList());*/

        return userPreferenceVOList.stream()
                .filter(el -> userPreferenceRepository.findByUserIdAndPrefCd(userId, el.prefCd()).isEmpty())
                .map(el -> userPreferenceRepository.save(new UserPreference(userId, el)))
                .collect(Collectors.toUnmodifiableList());
        //return userPreferenceVOList.stream().map(el->userPreferenceRepository.findByUserIdAndPrefCd(user_id, el.prefCd())).filter(el2->el2.isEmpty()).map(()->userPreferenceRepository.save(el)).collect(Collectors.toUnmodifiableList());//  .stream().collect(Collectors.toUnmodifiableList()));// .orElseGet(()->userPreferenceRepository.save(new UserPreference(el))));
        //userPreferenceVOList.stream().map(el->userPreferenceRepository.findByUserIdAndPrefCd(user_id, el.prefCd()).orElseGet(()->userPreferenceRepository.save(new UserPreference(el)))); //void리턴 시
        //userPreferenceVOList.stream().map(el->userPreferenceRepository.findByUserIdAndPrefCd(user_id, el.prefCd())).filter(el2-> !el2.isEmpty()).map(el3->userPreferenceRepository.save(new UserPreference(el3)))
    }

    @Transactional
    @CacheEvict(value = "preference", key = "#userId")
    public void updatePreferences(final String userId, final List<UserPreferenceVO> userPreferenceVOList) {
        userPreferenceVOList.stream().map(el -> userPreferenceRepository.findByUserIdAndPrefCd(userId, el.prefCd())
                .map(el2 -> {
                    el2.setPrefCd(el.prefCd());
                    el2.setPrefVal(el.prefVal());
                    el2.setPrior(el.prior());
                    return el2;
                })
                .orElseThrow(() -> {
                    throw new DataNotFoundException(MessageCode.DATA_NOT_FOUND_EXCEPTION.getMessage());
                })).collect(Collectors.toUnmodifiableList());
        //.forEach(el3->{System.out.println("el3: " + el3);});

        //userPreferenceRepository.saveAll()
    }

    @Transactional
    @CacheEvict(value = "preference", key = "#userId")
    public void updatePreferencesPriority(final String userId, final List<UserPreferenceVO> userPreferenceVOList) {
        userPreferenceVOList.stream().map(el -> userPreferenceRepository.findByUserIdAndPrefCd(userId, el.prefCd())
                .map(el2 -> {
                    el2.setPrefCd(el.prefCd());
                    el2.setPrior(el.prior());
                    return el2;
                })
                .orElseThrow(() -> {
                    throw new DataNotFoundException(MessageCode.DATA_NOT_FOUND_EXCEPTION.getMessage());
                })).collect(Collectors.toUnmodifiableList());
    }

    @Cacheable(value = "preference", key = "#userId", cacheManager = "jsonCacheManager")
    public String viewPreference(final String userId) {
    /*@Cacheable(value = "preference", key = "#userId")
    public List<UserPreferenceVO> viewPreference(final String userId) {*/
    //public List<UserPreferenceVO> viewPreference(final String userId) throws Exception {
        return userPreferenceRepository.findByUserId(userId)
                .stream()
                .map(userPreferenceMapper.INSTANCE::toUserPreferenceVO)
                .collect(Collectors.collectingAndThen(Collectors.toUnmodifiableList(), list -> {
                    if (list.isEmpty()) {
                        throw new DataNotFoundException(MessageCode.DATA_NOT_FOUND_EXCEPTION.getMessage());
                    }
                    try {
                        return objectMapper.writeValueAsString(list);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    //return list;
                }));
        //return userPreferenceRepository.findByUserId(userId).stream().map(userPreferenceMapper.INSTANCE::toUserPreferenceVO).collect(Collectors.toUnmodifiableList());
    }

    /*
    @Cacheable(value = "preference", key = "#userId")
    public List<UserPreferenceVO> viewPreference(final String userId) {
        //public List<UserPreferenceVO> viewPreference(final String userId) throws Exception {
        return userPreferenceRepository.findByUserId(userId)
                .stream()
                .map(userPreferenceMapper.INSTANCE::toUserPreferenceVO)
                .collect(Collectors.collectingAndThen(Collectors.toUnmodifiableList(), list -> {
                    if (list.isEmpty()) {
                        throw new DataNotFoundException(MessageCode.DATA_NOT_FOUND_EXCEPTION.getMessage());
                    }
                    return list;
                }));
        //return userPreferenceRepository.findByUserId(userId).stream().map(userPreferenceMapper.INSTANCE::toUserPreferenceVO).collect(Collectors.toUnmodifiableList());
    }
    */

}
