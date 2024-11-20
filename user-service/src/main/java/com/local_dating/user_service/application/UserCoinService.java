package com.local_dating.user_service.application;

import com.local_dating.user_service.domain.entity.UserCoin;
import com.local_dating.user_service.domain.vo.UserCoinVO;
import com.local_dating.user_service.infrastructure.repository.UserCoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCoinService {

    private final UserCoinRepository userCoinRepository;

    public Long viewCoin(final String userId) {
        return userCoinRepository.findByUserId(userId).map(el->{
            return el.getBalance();
        }).orElseGet(()->{
            return 0L;
        });
    }

    @Transactional
    public void saveCoin(final String userId, final UserCoinVO userCoinVO) {
        userCoinRepository.findByUserId(userId).map(el -> {
            el.setBalance(el.getBalance() + userCoinVO.balance());
            return userCoinRepository.save(el);
            //return el;
        }).orElseGet(()->{
            return userCoinRepository.save(new UserCoin(userId, userCoinVO.balance()));
        });
    }
}
