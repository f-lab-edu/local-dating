package com.local_dating.matching_service.util;

import com.local_dating.matching_service.domain.vo.UserProfileCoreVO;
import com.local_dating.matching_service.presentation.dto.UserCoinDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "user-service", url = "http://localhost:8080")
public interface UserServiceClient {

    @GetMapping(value = "/v1/users/{id}/coin")
    Long viewCoin(final @PathVariable("id") long id, final @RequestHeader("Authorization") String authentication);

    @PostMapping("/v1/users/{id}/coin")
    void saveCoin(final @PathVariable("id") long id, final @RequestHeader("Authorization") String authentication, @RequestBody final UserCoinDTO userCoinDTO);
    //public void saveCoin(final @PathVariable("id") long id, final @RequestHeader("Authorization") String authentication, final UserCoinDTO userCoinDTO);

    @PostMapping("/v1/users/{id}/coins")
    void updateCoin(final @PathVariable("id") long id, final @RequestHeader("Authorization") String authentication, @RequestBody final UserCoinDTO userCoinDTO);

    @GetMapping(value = "/api/users/{id}/next")
    List<UserProfileCoreVO> searchNext(final @PathVariable("id") Long id, final @RequestHeader("Authorization") String authentication);
    //List<UserProfileCoreVO> searchNext(final @PathVariable("id") Long id);
    //List<UserProfileCoreVO> searchNext(final @PathVariable("id") Long id, final @RequestHeader("Authorization") Authentication authentication);
}
