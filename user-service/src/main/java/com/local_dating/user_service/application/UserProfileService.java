package com.local_dating.user_service.application;

import com.local_dating.user_service.domain.entity.UserProfile;
import com.local_dating.user_service.domain.mapper.UserProfileMapper;
import com.local_dating.user_service.domain.vo.UserProfileVO;
import com.local_dating.user_service.infrastructure.repository.UserProfileRepository;
import com.local_dating.user_service.presentation.dto.UserProfileDTO;
import com.local_dating.user_service.util.MessageCode;
import com.local_dating.user_service.util.exception.DataAlreadyExistsException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserProfileService {
//public class UserProfileService implements UserProfileService_ {

    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;

    public UserProfileService(UserProfileRepository userProfileRepository, UserProfileMapper userProfileMapper) {
        this.userProfileRepository = userProfileRepository;
        this.userProfileMapper = userProfileMapper;
    }

    @Cacheable(value = "profile", key = "#userId")
    public List<UserProfileVO> viewProfile(final String userId) throws Exception {
    //public Optional<List<UserProfileVO>> viewProfile(final String userId) throws Exception {
    //public Optional<UserProfileVO> viewProfile(final String userId) throws Exception {

        return userProfileRepository.findByUserId(userId).stream().map(userProfileMapper.INSTANCE::toUserProfileVO).collect(Collectors.toUnmodifiableList());

        /*userProfileRepository.findByUserId(userId).ifPresentOrElse(el -> el.stream().forEach(el2 -> {
                    userProfileVOS.add(userProfileMapper.INSTANCE.toUserProfileVO(el2));
                    System.out.println("있음");
                }), () -> {
                    System.out.println("빈값");
                }
        );
        return Optional.ofNullable(userProfileVOS.isEmpty() ? null : userProfileVOS);*/

        //return userProfileRepository.findByUserId(userId).map(el->userProfileMapper.INSTANCE.toUserProfileVO(el));
        //return userProfileRepository.findByUserId(userId).map(el->userProfileMapper.INSTANCE.toUserProfileVO(el)).orElseGet(()->"zzz");
        //return userProfileRepository.findByUserId(userId);
    }

    @Transactional
    @CacheEvict(value = "profile", key = "#userId")
    public int saveProfile(final String userId, final List<UserProfileDTO> userProfileDTO) throws Exception {
    //public int saveProfile(String UserId, UserProfileDTO userProfileDTO) {

        List<UserProfile> userProfileList = new ArrayList<>();

        userProfileDTO.stream().forEach(el -> {
            userProfileRepository.findByUserIdAndInfoCd(userId, el.infoCd()).ifPresentOrElse(el2 -> {
                throw new DataAlreadyExistsException(MessageCode.DATA_ALREADY_EXISTS_EXCEPTION.getMessage() + " / " + el.userId() + " / " + el.infoCd() + " / " + el.infoVal());
            }, () -> {
                userProfileRepository.save(new UserProfile(userId, userProfileMapper.INSTANCE.toUserProfileVO(el))); // 데이터 없으면 저장
            });
        });

        /*userProfileDTO.stream().forEach(el -> {
            userProfileList.add(new UserProfile(userId, userProfileMapper.INSTANCE.toUserProfileVO(el)));
        });*/
        //List<UserProfileDTO> userProfileDTOList = new ArrayList<>();
        //userProfileDTO.stream().forEach(userProfileDTOList::add);
        //userProfileDTO.stream().forEach(el -> {userProfileDTOList.add(el);});

        List<UserProfile> userProfiles = userProfileRepository.saveAll(userProfileList);
        //userProfileDTO.stream().forEach(el -> userProfileRepository.save(new UserProfile(userId, userProfileMapper.INSTANCE.toUserProfileVO(el))));
        //userProfileRepository.save(new UserProfile(userProfileMapper.INSTANCE.toUserProfileVO(userProfileDTO)));

        return userProfiles.size();
    }

    @Transactional
    @CacheEvict(value = "profile", key = "#userId")
    public void updateProfile(final String userId, final List<UserProfileDTO> userProfileDTO) throws Exception {
    //public int updateProfile(final String userId, final List<UserProfileDTO> userProfileDTO) {

        userProfileDTO.stream().forEach(el -> {
            userProfileRepository.findByUserIdAndInfoCd(userId, el.infoCd()).ifPresentOrElse(el2 -> {
                el2.setInfoVal(el.infoVal()); // 데이터 있으면 업데이트
            }, () -> {
                userProfileRepository.save(new UserProfile(userId, userProfileMapper.INSTANCE.toUserProfileVO(el))); // 데이터 없으면 저장
            });
        });


        /*
        List<UserProfile> userProfileList = new ArrayList<>();
        userProfileDTO.stream().forEach(el -> {
            userProfileList.add(new UserProfile(userId, userProfileMapper.INSTANCE.toUserProfileVO(el)));
        });
        Optional<UserProfile> userProfileListUpdate = userProfileRepository.findByUserIdAndInfoCdIn(userId, userProfileList);//.stream().forEach(el);

        for (Optional<UserProfile> userProfile : userProfileListUpdate) {
            userProfile.setId(userProfileListUpdate.get().getId());
            //userProfile.setId(userProfileListUpdate.get().getId());
        }
        */

        //userProfileDTO.stream().forEach(el->userProfileRepository.save(new UserProfile(userId, userProfileMapper.INSTANCE.toUserProfileVO(el))));
        //userProfileRepository.save(new UserProfile(userProfileMapper.INSTANCE.toUserProfileVO(userProfileDTO)));

        //return 100;
    }
}
