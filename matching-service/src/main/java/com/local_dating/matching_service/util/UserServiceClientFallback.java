package com.local_dating.matching_service.util;

import com.local_dating.matching_service.domain.vo.UserProfileCoreVO;
import com.local_dating.matching_service.presentation.dto.UserCoinDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@Slf4j
@Component
public class UserServiceClientFallback implements UserServiceClient{
    @Override
    public Long viewCoin(long id, String authentication) {
        log.error("error viewCoin");
        return 0L;
    }

    @Override
    public void saveCoin(long id, String authentication, UserCoinDTO userCoinDTO) {
        log.error("error saveCoin");
    }

    @Override
    public void updateCoin(long id, String authentication, UserCoinDTO userCoinDTO) {
        log.error("error updateCoin");
    }

    @Override
    public Long viewPricingPolicy(long id, String type, String authentication) {
        return 0L;
    }

    @Override
    public List<UserProfileCoreVO> searchNext(Long id, String authentication) {
        log.error("error searchNext");
        return null;
    }

    @Override
    public void validateUserId(final @PathVariable("id") Long id, final @RequestHeader("Authorization") String authentication) { log.error("error validateUserId"); }
}
 // 미사용