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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

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

        Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(testItem);
        Mockito.when(itemMapper.toDto(Mockito.any(Item.class), Mockito.isNull())).thenReturn(expectedResponse);

        ItemResponseDto actualResponse = itemService.createItem(dto, testUser);

        Assertions.assertEquals(expectedResponse, actualResponse);
        Mockito.verify(itemRepository).save(Mockito.any(Item.class));
        Mockito.verify(itemFolderRepository, Mockito.never()).save(Mockito.any());
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

        Mockito.when(folderRepository.findByIdAndUser(folderId, testUser)).thenReturn(Optional.of(testFolder));
        Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(testItem);
        Mockito.when(itemFolderRepository.save(Mockito.any(ItemFolder.class))).thenReturn(testItemFolder);
        Mockito.when(itemMapper.toDto(Mockito.any(Item.class), Mockito.eq(folderId))).thenReturn(expectedResponse);

        ItemResponseDto actualResponse = itemService.createItem(dto, testUser);

        Assertions.assertEquals(expectedResponse, actualResponse);
        Mockito.verify(itemRepository).save(Mockito.any(Item.class));
        Mockito.verify(folderRepository).findByIdAndUser(folderId, testUser);
        Mockito.verify(itemFolderRepository).save(Mockito.any(ItemFolder.class));
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

        Mockito.when(folderRepository.findByIdAndUser(wrongFolderId, testUser)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> {
            itemService.createItem(dto, testUser);
        });

        Mockito.verify(itemRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(itemFolderRepository, Mockito.never()).save(Mockito.any());
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

        Mockito.when(itemRepository.findByIdAndUser(itemId, testUser)).thenReturn(Optional.of(testItem));
        Mockito.when(itemMapper.toDto(Mockito.any(Item.class))).thenReturn(expectedResponse);

        ItemResponseDto actualResponse = itemService.findItemById(testUser, itemId);

        Assertions.assertEquals(expectedResponse, actualResponse);
        Mockito.verify(itemRepository).findByIdAndUser(itemId, testUser);
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

        Mockito.when(itemRepository.findByIdAndUser(incorrectItemId, testUser)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> {
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

        Mockito.when(itemRepository.findByIdAndUser(itemId, testUser)).thenReturn(Optional.of(testItem));
        Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(updatedItem);
        Mockito.when(itemMapper.toDto(Mockito.any(Item.class))).thenReturn(expectedResponse);

        ItemResponseDto actualResponse = itemService.updateItem(testUser, itemId, updatedItemDto);

        Assertions.assertEquals(expectedResponse, actualResponse);
        Mockito.verify(itemRepository).findByIdAndUser(itemId, testUser);
        Mockito.verify(itemRepository).save(Mockito.any(Item.class));
        Mockito.verify(itemMapper).toDto(Mockito.any(Item.class));
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

        Mockito.when(itemRepository.findByIdAndUser(incorrectItemId, testUser)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> {
            itemService.updateItem(testUser, incorrectItemId, updatedItemDto);
        });

        Mockito.verify(itemRepository, Mockito.never()).save(Mockito.any());
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

        Mockito.when(itemRepository.findByIdAndUser(itemId, testUser)).thenReturn(Optional.of(testItem));

        itemService.deleteItem(testUser, itemId);

        Mockito.verify(itemRepository).delete(testItem);
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

        Mockito.when(itemRepository.findByIdAndUser(incorrectItemId, testUser)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> {
            itemService.deleteItem(testUser, incorrectItemId);
        });

        Mockito.verify(itemRepository, Mockito.never()).delete(Mockito.any());
    }
}
