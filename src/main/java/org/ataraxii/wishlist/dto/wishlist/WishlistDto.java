package org.ataraxii.wishlist.dto.wishlist;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WishlistDto {
    @Size(max = 32)
    @NotBlank
    private String name;
}
