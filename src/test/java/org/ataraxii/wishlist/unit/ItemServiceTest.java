package org.ataraxii.wishlist.unit;

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
import org.ataraxii.wishlist.service.ItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private FolderRepository folderRepository;

    @Mock
    private ItemFolderRepository itemFolderRepository;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private ItemService itemService;

    @Test
    void createItem_itemNoFolder_created() {
        User testUser = User.builder()
                .id(UUID.fromString("ea7c7381-a19c-49a9-92cd-d32c3db25092"))
                .username("testuser")
                .password("testpassword")
                .createdAt(Instant.now())
                .build();

        ItemDto dto = ItemDto.builder()
                .name("testname")
                .url("testurl.com/url/url")
                .folderId(null)
                .build();

        Item testItem = Item.builder()
                .name("testname")
                .url("testurl.com/url/url")
                .user(testUser)
                .build();

        ItemResponseDto expectedResponse = ItemResponseDto.builder()
                .name("testname")
                .url("testurl.com/url/url")
                .folderId(null)
                .build();

        when(itemRepository.save(any(Item.class))).thenReturn(testItem);
        when(itemMapper.toDto(any(Item.class), isNull())).thenReturn(expectedResponse);

        ItemResponseDto actualResponse = itemService.createItem(dto, testUser);

        assertEquals(expectedResponse, actualResponse);
        verify(itemRepository).save(any(Item.class));
        verify(itemFolderRepository, never()).save(any());
    }

    @Test
    void createItem_itemWithFolder_created() {

        UUID folderId = UUID.randomUUID();

        User testUser = User.builder()
                .id(UUID.fromString("ea7c7381-a19c-49a9-92cd-d32c3db25092"))
                .username("testuser")
                .password("testpassword")
                .createdAt(Instant.now())
                .build();

        ItemDto dto = ItemDto.builder()
                .name("testname")
                .url("testurl.com/url/url")
                .folderId(folderId)
                .build();

        Item testItem = Item.builder()
                .name("testname")
                .url("testurl.com/url/url")
                .user(testUser)
                .build();

        Folder testFolder = Folder.builder()
                .id(folderId)
                .name("Test Folder")
                .user(testUser)
                .build();

        ItemFolder testItemFolder = ItemFolder.builder()
                .item(testItem)
                .folder(testFolder)
                .build();

        ItemResponseDto expectedResponse = ItemResponseDto.builder()
                .name("testname")
                .url("testurl.com/url/url")
                .folderId(folderId)
                .build();

        when(folderRepository.findByIdAndUser(folderId, testUser)).thenReturn(Optional.of(testFolder));
        when(itemRepository.save(any(Item.class))).thenReturn(testItem);
        when(itemFolderRepository.save(any(ItemFolder.class))).thenReturn(testItemFolder);
        when(itemMapper.toDto(any(Item.class), eq(folderId))).thenReturn(expectedResponse);

        ItemResponseDto actualResponse = itemService.createItem(dto, testUser);

        assertEquals(expectedResponse, actualResponse);
        verify(itemRepository).save(any(Item.class));
        verify(folderRepository).findByIdAndUser(folderId, testUser);
        verify(itemFolderRepository).save(any(ItemFolder.class));
    }

    @Test
    void createItem_itemWithIncorrectFolderId_returnNotFoundException() {

        UUID wrongFolderId = UUID.randomUUID();

        User testUser = User.builder()
                .id(UUID.fromString("ea7c7381-a19c-49a9-92cd-d32c3db25092"))
                .username("testuser")
                .password("testpassword")
                .createdAt(Instant.now())
                .build();

        ItemDto dto = ItemDto.builder()
                .name("testname")
                .url("testurl.com/url/url")
                .folderId(wrongFolderId)
                .build();

        when(folderRepository.findByIdAndUser(wrongFolderId, testUser)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            itemService.createItem(dto, testUser);
        });

        verify(itemRepository, never()).save(any());
        verify(itemFolderRepository, never()).save(any());
    }

    @Test
    void findItemById_correctId_returnItem() {

        UUID itemId = UUID.randomUUID();

        User testUser = User.builder()
                .id(UUID.fromString("ea7c7381-a19c-49a9-92cd-d32c3db25092"))
                .username("testuser")
                .password("testpassword")
                .createdAt(Instant.now())
                .build();

        Item testItem = Item.builder()
                .id(itemId)
                .name("testname")
                .url("testurl.com/url/url")
                .user(testUser)
                .build();

        ItemResponseDto expectedResponse = ItemResponseDto.builder()
                .id(itemId)
                .name("testname")
                .url("testurl.com/url/url")
                .user(testUser.getId())
                .folderId(null)
                .build();

        when(itemRepository.findByIdAndUser(itemId, testUser)).thenReturn(Optional.of(testItem));
        when(itemMapper.toDto(any(Item.class))).thenReturn(expectedResponse);

        ItemResponseDto actualResponse = itemService.findItemById(testUser, itemId);

        assertEquals(expectedResponse, actualResponse);
        verify(itemRepository).findByIdAndUser(itemId, testUser);
    }

    @Test
    void findItemById_incorrectId_returnNotFoundException() {

        UUID incorrectItemId = UUID.randomUUID();

        User testUser = User.builder()
                .id(UUID.fromString("ea7c7381-a19c-49a9-92cd-d32c3db25092"))
                .username("testuser")
                .password("testpassword")
                .createdAt(Instant.now())
                .build();

        when(itemRepository.findByIdAndUser(incorrectItemId, testUser)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            itemService.findItemById(testUser, incorrectItemId);
        });
    }

    @Test
    void updateItem_correctId_returnUpdatedItem() {

        UUID itemId = UUID.randomUUID();

        User testUser = User.builder()
                .id(UUID.fromString("ea7c7381-a19c-49a9-92cd-d32c3db25092"))
                .username("testuser")
                .password("testpassword")
                .createdAt(Instant.now())
                .build();

        Item testItem = Item.builder()
                .id(itemId)
                .name("testname")
                .url("testurl.com/url/url")
                .user(testUser)
                .build();

        Item updatedItem = Item.builder()
                .id(itemId)
                .name("updatedname")
                .url("updatedurl.com/url/url")
                .user(testUser)
                .build();

        ItemDto updatedItemDto = ItemDto.builder()
                .name("updatedname")
                .url("updatedurl.com/url/url")
                .build();

        ItemResponseDto expectedResponse = ItemResponseDto.builder()
                .id(itemId)
                .name(updatedItem.getName())
                .url(updatedItem.getUrl())
                .user(updatedItem.getUser().getId())
                .folderId(null)
                .build();

        when(itemRepository.findByIdAndUser(itemId, testUser)).thenReturn(Optional.of(testItem));
        when(itemRepository.save(any(Item.class))).thenReturn(updatedItem);
        when(itemMapper.toDto(any(Item.class))).thenReturn(expectedResponse);

        ItemResponseDto actualResponse = itemService.updateItem(testUser, itemId, updatedItemDto);

        assertEquals(expectedResponse, actualResponse);
        verify(itemRepository).findByIdAndUser(itemId, testUser);
        verify(itemRepository).save(any(Item.class));
        verify(itemMapper).toDto(any(Item.class));
    }

    @Test
    void updateItem_incorrectId_returnNotFoundException() {

        UUID incorrectItemId = UUID.randomUUID();

        User testUser = User.builder()
                .id(UUID.fromString("ea7c7381-a19c-49a9-92cd-d32c3db25092"))
                .username("testuser")
                .password("testpassword")
                .createdAt(Instant.now())
                .build();

        ItemDto updatedItemDto = ItemDto.builder()
                .name("updatedname")
                .url("updatedurl.com/url/url")
                .build();

        when(itemRepository.findByIdAndUser(incorrectItemId, testUser)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            itemService.updateItem(testUser, incorrectItemId, updatedItemDto);
        });

        verify(itemRepository, never()).save(any());
    }

    @Test
    void deleteItem_correctId_success() {

        UUID itemId = UUID.randomUUID();

        User testUser = User.builder()
                .id(UUID.fromString("ea7c7381-a19c-49a9-92cd-d32c3db25092"))
                .username("testuser")
                .password("testpassword")
                .createdAt(Instant.now())
                .build();

        Item testItem = Item.builder()
                .id(itemId)
                .name("testname")
                .url("testurl.com/url/url")
                .user(testUser)
                .build();

        when(itemRepository.findByIdAndUser(itemId, testUser)).thenReturn(Optional.of(testItem));

        itemService.deleteItem(testUser, itemId);

        verify(itemRepository).delete(testItem);
    }

    @Test
    void deleteItem_incorrectId_returnNotFoundException() {

        UUID incorrectItemId = UUID.randomUUID();

        User testUser = User.builder()
                .id(UUID.fromString("ea7c7381-a19c-49a9-92cd-d32c3db25092"))
                .username("testuser")
                .password("testpassword")
                .createdAt(Instant.now())
                .build();

        when(itemRepository.findByIdAndUser(incorrectItemId, testUser)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            itemService.deleteItem(testUser, incorrectItemId);
        });

        verify(itemRepository, never()).delete(any());
    }
}
