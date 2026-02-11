package com.local_dating.matching_service.util;

import com.local_dating.matching_service.domain.vo.UserProfileCoreVO;
import com.local_dating.matching_service.presentation.dto.UserCoinDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
    public List<UserProfileCoreVO> searchNext(Long id, String authentication) {
        log.error("error searchNext");
        return null;
    }
}
