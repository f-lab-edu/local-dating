package com.local_dating.matching_service.util;

import com.local_dating.matching_service.presentation.dto.UserCoinDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service", url = "http://localhost:8081")
public interface UserServiceClient {

    @PostMapping("/v1/users/{id}/coin")
    //@PostMapping("/user-service/v1/users/{id}/coin")
    //public void saveCoin(final @PathVariable("id") long id);
    //public void saveCoin(final @PathVariable("id") long id, final Authentication authentication, final UserCoinDTO userCoinDTO);

    public void saveCoin(final @PathVariable("id") long id, final @RequestHeader("Authorization") String authentication, final UserCoinDTO userCoinDTO);
}
