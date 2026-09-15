package com.local_dating.matching_service.application;

import lombok.Getter;

import java.io.Serializable;
import java.security.Principal;

@Getter
public class AuthenticatedUserDetails implements Principal, Serializable {

    private static final long serialVersionUID = 0;

    private final Long userNo;

    public AuthenticatedUserDetails(Long userNo) {
        this.userNo = userNo;
    }

    @Override
    public String getName() {
        return userNo.toString();
    }
}
