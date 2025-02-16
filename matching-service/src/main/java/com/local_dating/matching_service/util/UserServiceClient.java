package com.local_dating.matching_service.util;

import com.local_dating.matching_service.presentation.dto.UserCoinDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service", url = "http://localhost:8081")
public interface UserServiceClient {

    @GetMapping(value = "/v1/users/{id}/coin")
    Long viewCoin(final @PathVariable("id") long id, final @RequestHeader("Authorization") String authentication);

    @PostMapping("/v1/users/{id}/coin")
    void saveCoin(final @PathVariable("id") long id, final @RequestHeader("Authorization") String authentication, @RequestBody final UserCoinDTO userCoinDTO);
    //public void saveCoin(final @PathVariable("id") long id, final @RequestHeader("Authorization") String authentication, final UserCoinDTO userCoinDTO);
}
