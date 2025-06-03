package org.ataraxii.wishlist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ataraxii.wishlist.database.entity.User;
import org.ataraxii.wishlist.database.entity.Wishlist;
import org.ataraxii.wishlist.database.repository.WishlistRepository;
import org.ataraxii.wishlist.dto.wishlist.WishlistResponseDto;
import org.ataraxii.wishlist.dto.user.ProfileResponseDto;
import org.ataraxii.wishlist.mapper.WishlistMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final WishlistRepository wishlistRepository;
    private final WishlistMapper wishlistMapper;

    public ProfileResponseDto getProfile(User currentUser) {
        List<Wishlist> wishlists = wishlistRepository.findByUser(currentUser);
        log.info("Найдено {} вишлистов у пользователя {}", wishlists.size(), currentUser.getUsername());
        List<WishlistResponseDto> foldersDto = wishlists.stream()
                .map(wishlistMapper::toDto)
                .toList();

        return ProfileResponseDto.builder()
                .id(currentUser.getId())
                .username(currentUser.getUsername())
                .wishlists(foldersDto)
                .build();
    }
}
