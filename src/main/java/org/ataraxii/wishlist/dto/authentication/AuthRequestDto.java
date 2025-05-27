package org.ataraxii.wishlist.dto.authentication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthRequestDto {
    @Size(max = 32)
    @NotBlank
    private String username;

    @Size(max = 32)
    @NotBlank
    private String password;
}
