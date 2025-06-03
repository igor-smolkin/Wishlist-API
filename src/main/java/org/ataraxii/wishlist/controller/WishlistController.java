package org.ataraxii.wishlist.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.ataraxii.wishlist.database.entity.User;
import org.ataraxii.wishlist.dto.wishlist.WishlistDto;
import org.ataraxii.wishlist.dto.wishlist.WishlistItemsResponseDto;
import org.ataraxii.wishlist.dto.wishlist.WishlistResponseDto;
import org.ataraxii.wishlist.service.WishlistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/wishlists")
    public ResponseEntity<WishlistResponseDto> createWishlist(@RequestBody WishlistDto dto,
                                                              HttpServletRequest request) {
        User currentUser = (User) request.getAttribute("currentUser");
        WishlistResponseDto response = wishlistService.createWishlist(dto, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/wishlists")
    public ResponseEntity<List<WishlistResponseDto>> findAllWishlists(HttpServletRequest request) {
        User currentUser = (User) request.getAttribute("currentUser");
        List<WishlistResponseDto> response = wishlistService.findAllWishlists(currentUser);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/wishlists/{id}")
    public ResponseEntity<WishlistItemsResponseDto> findWishlistById(HttpServletRequest request,
                                                                     @PathVariable UUID id) {
        User currentUser = (User) request.getAttribute("currentUser");
        WishlistItemsResponseDto response = wishlistService.findWishlistById(currentUser, id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/wishlists/{id}")
    public ResponseEntity<WishlistResponseDto> updateWishlist(HttpServletRequest request,
                                                              @PathVariable UUID id,
                                                              WishlistDto dto) {
        User currentUser = (User) request.getAttribute("currentUser");
        WishlistResponseDto response = wishlistService.updateWishlist(currentUser, id ,dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/wishlists/{id}")
    public ResponseEntity<Void> deleteWishlist(HttpServletRequest request,
                                               @PathVariable UUID id) {
        User currentUser = (User) request.getAttribute("currentUser");
        wishlistService.deleteWishlist(currentUser, id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
