package org.ataraxii.wishlist.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ataraxii.wishlist.database.entity.Folder;
import org.ataraxii.wishlist.database.entity.Item;
import org.ataraxii.wishlist.database.entity.ItemFolder;
import org.ataraxii.wishlist.database.entity.User;
import org.ataraxii.wishlist.database.repository.FolderRepository;
import org.ataraxii.wishlist.database.repository.ItemFolderRepository;
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
    private final FolderRepository folderRepository;
    private final ItemFolderRepository itemFolderRepository;
    private final ItemMapper itemMapper;

    @Transactional
    public ItemResponseDto createItem(ItemDto dto, User currentUser) {
        log.info("Создание предмета {} пользователем {}", dto.getName(), currentUser.getUsername());
        Item item = Item.builder()
                .name(dto.getName())
                .url(dto.getUrl())
                .user(currentUser)
                .build();

        UUID folderId = null;

        if (dto.getFolderId() != null) {
            Folder folder = folderRepository.findByIdAndUser(dto.getFolderId(), currentUser)
                    .orElse(null);

            if (folder == null) {
                log.warn("Ошибка создания предмета: папка с id={} не найдена у пользователя {}", dto.getFolderId(), currentUser.getUsername());
                throw new NotFoundException("Папка не найдена");
            }
            itemRepository.save(item);
            log.info("Предмет {} успешно создан в папке {} пользователем {}", item.getName(), folder.getName(), currentUser.getUsername());

            ItemFolder itemFolder = ItemFolder.builder()
                    .item(item)
                    .folder(folder)
                    .build();
            itemFolderRepository.save(itemFolder);
            folderId = folder.getId();
        } else {
            itemRepository.save(item);
            log.info("Предмет {} успешно создан пользователем {}", item.getName(), currentUser.getUsername());
        }
        return itemMapper.toDto(item, folderId);
    }

    public List<ItemResponseDto> findAllItems(User currentUser) {
        List<Item> items = itemRepository.findAllByUser(currentUser);
        log.info("Найдено {} предметов у пользователя {}", items.size(), currentUser.getUsername());
        return items.stream()
                .map(item -> {
                    UUID folderId = null;
                    List<ItemFolder> folders = item.getItemFolder();
                    if (folders != null && !folders.isEmpty()) {
                        folderId = folders.get(0).getFolder().getId();
                    }
                    return itemMapper.toDto(item, folderId);
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
        log.info("Обновление папки {} пользователем {}", item.getName(), currentUser.getUsername());

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
