package com.local_dating.user_service.domain.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckCiValidationVO {
    private String ci;
    private boolean result;
    private long userNo;
}
