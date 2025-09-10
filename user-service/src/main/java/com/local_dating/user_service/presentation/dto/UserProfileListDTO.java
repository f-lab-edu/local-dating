package com.local_dating.user_service.presentation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserProfileListDTO {

    @NotEmpty
    @Valid
    private List<UserProfileDTO> userProfiles;

}
