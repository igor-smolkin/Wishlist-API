package org.ataraxii.wishlist.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ataraxii.wishlist.database.entity.Wishlist;
import org.ataraxii.wishlist.database.repository.WishlistRepository;
import org.ataraxii.wishlist.dto.wishlist.WishlistDto;
import org.ataraxii.wishlist.dto.wishlist.WishlistItemsResponseDto;
import org.ataraxii.wishlist.dto.wishlist.WishlistResponseDto;
import org.ataraxii.wishlist.exception.NotFoundException;
import org.ataraxii.wishlist.mapper.WishlistMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistMapper wishlistMapper;

//    @Transactional
//    public WishlistResponseDto createWishlist(WishlistDto dto, User currentUser) {
//        log.info("Создание вишлиста {} пользователем {}", dto.getName(), currentUser.getUsername());
//        Wishlist wishlist = org.ataraxii.wishlist.database.entity.Wishlist.builder()
//                .name(dto.getName())
//                .user(currentUser)
//                .build();
//        wishlistRepository.save(wishlist);
//        log.info("Вишлист {} успешно создан пользователем {}", dto.getName(), currentUser.getUsername());
//        return wishlistMapper.toDto(wishlist);
//    }
//
//    public List<WishlistResponseDto> findAllWishlists(User currentUser) {
//        List<Wishlist> wishlists = wishlistRepository.findByUser(currentUser);
//        log.info("Найдено {} вишлистов у пользователя {}", wishlists.size(), currentUser.getUsername());
//        return wishlists.stream()
//                .map(wishlistMapper::toDto)
//                .collect(Collectors.toList());
//    }
//
//    public WishlistItemsResponseDto findWishlistById(User currentUser, UUID id) {
//        Wishlist wishlist = wishlistRepository.findByIdAndUser(id, currentUser)
//                .orElse(null);
//
//        if (wishlist == null) {
//            log.warn("Ошибка поиска вишлиста: вишлист с id={} не найден у пользователя {}", id, currentUser.getUsername());
//            throw new NotFoundException("Папка с таким id не найдена");
//        }
//        log.info("Вишлист {} найден у пользователя {}", wishlist.getName(), currentUser.getUsername());
//        return wishlistMapper.toDtoWithItems(wishlist);
//    }
//
//    public WishlistResponseDto updateWishlist(User currentUser, UUID id, WishlistDto dto) {
//        Wishlist wishlist = wishlistRepository.findByIdAndUser(id, currentUser)
//                .orElse(null);
//
//        if (wishlist == null) {
//            log.warn("Ошибка обновления вишлиста: вишлист с id={} не найден у пользователя {}", id, currentUser.getUsername());
//            throw new NotFoundException("Вишлист с таким id не найден");
//        }
//        log.info("Обновление вишлиста {} пользователем {}", wishlist.getName(), currentUser.getUsername());
//
//        String oldName = wishlist.getName();
//        wishlist.setName(dto.getName());
//        log.info("Имя вишлиста успешно изменено: {} -> {}", oldName, wishlist.getName());
//
//        wishlistRepository.save(wishlist);
//        return wishlistMapper.toDto(wishlist);
//    }
//
//    @Transactional
//    public void deleteWishlist(User currentUser, UUID id) {
//        Wishlist wishlist = wishlistRepository.findByIdAndUser(id, currentUser)
//                .orElse(null);
//        if (wishlist == null) {
//            log.warn("Ошибка удаления вишлиста: вишлист с id={} не найден у пользователя {}", id, currentUser.getUsername());
//            throw new NotFoundException("Вишлист с таким id не найден");
//        }
//        wishlistRepository.delete(wishlist);
//        log.info("Вишлист {} удален пользователем {}", wishlist.getName(), currentUser.getUsername());
//    }
}
