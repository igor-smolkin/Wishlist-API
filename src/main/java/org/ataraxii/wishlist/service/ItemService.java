package org.ataraxii.wishlist.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ataraxii.wishlist.database.entity.Item;
import org.ataraxii.wishlist.database.entity.ItemWishlist;
import org.ataraxii.wishlist.database.entity.Wishlist;
import org.ataraxii.wishlist.database.repository.WishlistRepository;
import org.ataraxii.wishlist.database.repository.ItemWishlistRepository;
import org.ataraxii.wishlist.database.repository.ItemRepository;
import org.ataraxii.wishlist.dto.item.ItemDto;
import org.ataraxii.wishlist.dto.item.ItemResponseDto;
import org.ataraxii.wishlist.exception.NotFoundException;
import org.ataraxii.wishlist.mapper.ItemMapper;
import org.ataraxii.wishlist.security.SecurityUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final WishlistRepository wishlistRepository;
    private final ItemWishlistRepository itemWishlistRepository;
    private final ItemMapper itemMapper;
    private final SecurityUtil securityUtil;

    @Transactional
    public ItemResponseDto createItem(ItemDto dto, UUID userId) {
        String username = securityUtil.getCurrentUsername();
        log.info("Создание предмета '{}' пользователем '{}'", dto.getName(), username);
        Item item = Item.builder()
                .name(dto.getName())
                .url(dto.getUrl())
                .userId(userId)
                .build();

        UUID wishlistId = null;

        if (dto.getWishlistId() != null) {
            Wishlist wishlist = wishlistRepository.findByIdAndUserId(dto.getWishlistId(), userId)
                    .orElse(null);

            if (wishlist == null) {
                log.warn("Ошибка создания предмета: вишлист с id={} не найден у пользователя '{}'", dto.getWishlistId(), username);
                throw new NotFoundException("Вишлист не найден");
            }
            itemRepository.save(item);
            log.info("Предмет '{}' успешно создан в вишлисте '{}' пользователем '{}'", item.getName(), wishlist.getName(), username);

            ItemWishlist itemWishlist = ItemWishlist.builder()
                    .item(item)
                    .wishlist(wishlist)
                    .build();
            itemWishlistRepository.save(itemWishlist);
            wishlistId = wishlist.getId();
        } else {
            itemRepository.save(item);
            log.info("Предмет '{}' успешно создан пользователем '{}'", item.getName(), username);
        }
        return itemMapper.toDto(userId, item, wishlistId);
    }

    public List<ItemResponseDto> findAllItems(UUID userId) {
        String username = securityUtil.getCurrentUsername();
        List<Item> items = itemRepository.findAllByUserId(userId);
        log.info("Найдено '{}' предметов у пользователя '{}'", items.size(), username);
        return items.stream()
                .map(item -> {
                    UUID wishlistId = null;
                    List<ItemWishlist> wishlists = item.getItemWishlist();
                    if (wishlists != null && !wishlists.isEmpty()) {
                        wishlistId = wishlists.get(0).getWishlist().getId();
                    }
                    return itemMapper.toDto(userId, item, wishlistId);
                })
                .collect(Collectors.toList());
    }

    public ItemResponseDto findItemById(UUID userId, UUID itemId) {
        String username = securityUtil.getCurrentUsername();
        Item item = itemRepository.findItemByIdAndUserId(itemId, userId)
                .orElseThrow(() -> new NotFoundException("Предмет с таким id не найден"));
        log.info("Предмет '{}' найден у пользователя '{}'", item.getName(), username);
        return itemMapper.toDto(userId, item);
    }

    @Transactional
    public ItemResponseDto updateItem(UUID userId, UUID itemId, ItemDto updatedItem) {
        String username = securityUtil.getCurrentUsername();
        Item item = itemRepository.findByIdAndUserId(itemId, userId)
                .orElse(null);

        if (item == null) {
            log.warn("Ошибка обновления предмета: предмет с id={} не найден у пользователя '{}'", itemId, username);
            throw new NotFoundException("Предмет с таким id не найден");
        }
        log.info("Обновление вишлиста '{}' пользователем '{}'", item.getName(), username);

        item.setName(updatedItem.getName());
        item.setUrl(updatedItem.getUrl());
        log.info("Предмет '{}' успешно изменен пользователем '{}'", item.getName(), username);

        itemRepository.save(item);
        return itemMapper.toDto(userId, item);
    }

    @Transactional
    public void deleteItem(UUID userId, UUID itemId) {
        String username = securityUtil.getCurrentUsername();
        Item item = itemRepository.findByIdAndUserId(itemId, userId)
                .orElse(null);

        if (item == null) {
            log.warn("Ошибка удаления предмета: предмет с id={} не найден у пользователя '{}'", itemId, username);
            throw new NotFoundException("Предмет с таким id не найден");
        }
        itemRepository.delete(item);
        log.info("Предмет {} удален пользователем {}", item.getName(), username);
    }
}
