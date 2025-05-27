package org.ataraxii.wishlist.dto.authentication;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponseDto {
    private UserDto user;
    private SessionDto session;
}
