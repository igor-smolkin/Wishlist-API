package org.ataraxii.wishlist.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ataraxii.wishlist.dto.wishlist.WishlistResponseDto;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponseDto {

    private UUID id;
    private String username;
    private List<WishlistResponseDto> wishlists;
}
