package org.ataraxii.wishlist.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ataraxii.wishlist.database.entity.Item;
import org.ataraxii.wishlist.database.entity.ItemWishlist;
import org.ataraxii.wishlist.database.entity.User;
import org.ataraxii.wishlist.database.entity.Wishlist;
import org.ataraxii.wishlist.database.repository.WishlistRepository;
import org.ataraxii.wishlist.database.repository.ItemWishlistRepository;
import org.ataraxii.wishlist.database.repository.ItemRepository;
import org.ataraxii.wishlist.dto.item.ItemDto;
import org.ataraxii.wishlist.dto.item.ItemResponseDto;
import org.ataraxii.wishlist.exception.NotFoundException;
import org.ataraxii.wishlist.mapper.ItemMapper;
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

    @Transactional
    public ItemResponseDto createItem(ItemDto dto, User currentUser) {
        log.info("Создание предмета {} пользователем {}", dto.getName(), currentUser.getUsername());
        Item item = Item.builder()
                .name(dto.getName())
                .url(dto.getUrl())
                .user(currentUser)
                .build();

        UUID wishlistId = null;

        if (dto.getWishlistId() != null) {
            Wishlist wishlist = wishlistRepository.findByIdAndUser(dto.getWishlistId(), currentUser)
                    .orElse(null);

            if (wishlist == null) {
                log.warn("Ошибка создания предмета: вишлист с id={} не найден у пользователя {}", dto.getWishlistId(), currentUser.getUsername());
                throw new NotFoundException("Вишлист не найден");
            }
            itemRepository.save(item);
            log.info("Предмет {} успешно создан в вишлисте {} пользователем {}", item.getName(), wishlist.getName(), currentUser.getUsername());

            ItemWishlist itemWishlist = ItemWishlist.builder()
                    .item(item)
                    .wishlist(wishlist)
                    .build();
            itemWishlistRepository.save(itemWishlist);
            wishlistId = wishlist.getId();
        } else {
            itemRepository.save(item);
            log.info("Предмет {} успешно создан пользователем {}", item.getName(), currentUser.getUsername());
        }
        return itemMapper.toDto(item, wishlistId);
    }

    public List<ItemResponseDto> findAllItems(User currentUser) {
        List<Item> items = itemRepository.findAllByUser(currentUser);
        log.info("Найдено {} предметов у пользователя {}", items.size(), currentUser.getUsername());
        return items.stream()
                .map(item -> {
                    UUID wishlistId = null;
                    List<ItemWishlist> wishlists = item.getItemWishlist();
                    if (wishlists != null && !wishlists.isEmpty()) {
                        wishlistId = wishlists.get(0).getWishlist().getId();
                    }
                    return itemMapper.toDto(item, wishlistId);
                })
                .collect(Collectors.toList());
    }

    public ItemResponseDto findItemById(User currentUser, UUID id) {
        Item item = itemRepository.findByIdAndUser(id, currentUser)
                .orElse(null);
        if (item == null) {
            log.warn("Ошибка поиска предмета: предмет с id={} не найдена у пользователя {}", id, currentUser.getUsername());
            throw new NotFoundException("Предмет с таким id не найден");
        }
        log.info("Предмет {} найден у пользователя {}", item.getName(), currentUser.getUsername());
        return itemMapper.toDto(item);
    }

    @Transactional
    public ItemResponseDto updateItem(User currentUser, UUID id, ItemDto updatedItem) {
        Item item = itemRepository.findByIdAndUser(id, currentUser)
                .orElse(null);

        if (item == null) {
            log.warn("Ошибка обновления предмета: предмет с id={} не найден у пользователя {}", id, currentUser.getUsername());
            throw new NotFoundException("Предмет с таким id не найден");
        }
        log.info("Обновление вишлиста {} пользователем {}", item.getName(), currentUser.getUsername());

        item.setName(updatedItem.getName());
        item.setUrl(updatedItem.getUrl());
        log.info("Предмет {} успешно изменен", item.getName());

        itemRepository.save(item);
        return itemMapper.toDto(item);
    }

    @Transactional
    public void deleteItem(User currentUser, UUID id) {
        Item item = itemRepository.findByIdAndUser(id, currentUser)
                .orElse(null);

        if (item == null) {
            log.warn("Ошибка удаления предмета: предмет с id={} не найден у пользователя {}", id, currentUser.getUsername());
            throw new NotFoundException("Предмет с таким id не найден");
        }
        itemRepository.delete(item);
        log.info("Предмет {} удален пользователем {}", item.getName(), currentUser.getUsername());
    }
}
