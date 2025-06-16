package org.ataraxii.wishlist.controller;

import lombok.RequiredArgsConstructor;
import org.ataraxii.wishlist.dto.wishlist.WishlistItemsResponseDto;
import org.ataraxii.wishlist.security.SecurityUtil;
import org.ataraxii.wishlist.service.WishlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ShareController {

    private final WishlistService wishlistService;
    private final SecurityUtil securityUtil;

//    @GetMapping("/shared/{id}")
//    public ResponseEntity<WishlistItemsResponseDto> getSharedWishlist(@PathVariable UUID id) {
//        WishlistItemsResponseDto wishlist = wishlistService.checkShared(id);
//        return ResponseEntity.ok(wishlist);
//    }
//
//    @PatchMapping("/wishlists/share/{id}")
//    public ResponseEntity<Void> setShareWishlist(@PathVariable UUID id) {
//        UUID userId = securityUtil.getCurrentUserId();
//        wishlistService.setShared(userId, id);
//        return ResponseEntity.ok().build();
//    }
}
