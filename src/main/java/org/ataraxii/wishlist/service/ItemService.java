package org.ataraxii.wishlist.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ataraxii.wishlist.database.entity.Folder;
import org.ataraxii.wishlist.database.entity.Item;
import org.ataraxii.wishlist.database.entity.ItemFolder;
import org.ataraxii.wishlist.database.entity.User;
import org.ataraxii.wishlist.database.repository.FolderRepository;
import org.ataraxii.wishlist.database.repository.ItemFolderRepository;
import org.ataraxii.wishlist.database.repository.ItemRepository;
import org.ataraxii.wishlist.dto.item.ItemDto;
import org.ataraxii.wishlist.dto.item.ItemResponseDto;
import org.ataraxii.wishlist.exception.ConflictException;
import org.ataraxii.wishlist.exception.NotFoundException;
import org.ataraxii.wishlist.mapper.ItemMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final FolderRepository folderRepository;
    private final ItemFolderRepository itemFolderRepository;
    private final ItemMapper itemMapper;

    @Transactional
    public ItemResponseDto createItem(ItemDto dto, User currentUser) {
        Item item = Item.builder()
                .name(dto.getName())
                .url(dto.getUrl())
                .user(currentUser)
                .build();

        UUID folderId = null;

        if (dto.getFolderId() != null) {
            Folder folder = folderRepository.findByIdAndUser(dto.getFolderId(), currentUser)
                    .orElseThrow(() -> new NotFoundException("Папка не найдена"));

            itemRepository.save(item);

            ItemFolder itemFolder = ItemFolder.builder()
                    .item(item)
                    .folder(folder)
                    .build();
            itemFolderRepository.save(itemFolder);
            folderId = folder.getId();
        } else {
            itemRepository.save(item);
        }
        return itemMapper.toDto(item, folderId);
    }

    public List<ItemResponseDto> findAllItems(User currentUser) {
        List<Item> items = itemRepository.findAllByUser(currentUser);
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
                .orElseThrow(() -> new NotFoundException("Предмет с таким id не найден"));
        return itemMapper.toDto(item);
    }

    @Transactional
    public ItemResponseDto updateItem(User currentUser, UUID id, ItemDto updatedItem) {
        Item item = itemRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new NotFoundException("Предмет с таким id не найден"));
        item.setName(updatedItem.getName());
        item.setUrl(updatedItem.getUrl());

        itemRepository.save(item);
        return itemMapper.toDto(item);
    }

    @Transactional
    public void deleteItem(User currentUser, UUID id) {
        Item item = itemRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new NotFoundException("Предмет с таким id не найден"));

        itemRepository.delete(item);
    }
}
