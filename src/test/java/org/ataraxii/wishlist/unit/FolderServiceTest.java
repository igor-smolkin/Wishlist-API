package org.ataraxii.wishlist.unit;

import org.ataraxii.wishlist.database.entity.Folder;
import org.ataraxii.wishlist.database.entity.Item;
import org.ataraxii.wishlist.database.entity.User;
import org.ataraxii.wishlist.database.repository.FolderRepository;
import org.ataraxii.wishlist.dto.folder.FolderDto;
import org.ataraxii.wishlist.dto.folder.FolderResponseDto;
import org.ataraxii.wishlist.dto.item.ItemDto;
import org.ataraxii.wishlist.exception.NotFoundException;
import org.ataraxii.wishlist.mapper.FolderMapper;
import org.ataraxii.wishlist.service.FolderService;
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
class FolderServiceTest {

    @Mock
    private FolderRepository folderRepository;

    @Mock
    private FolderMapper folderMapper;

    @InjectMocks
    private FolderService folderService;

    @Test
    void createFolder_success_returnFolder() {

        User testUser = User.builder()
                .id(UUID.fromString("ea7c7381-a19c-49a9-92cd-d32c3db25092"))
                .username("testuser")
                .password("testpassword")
                .createdAt(Instant.now())
                .build();

        FolderDto dto = FolderDto.builder()
                .name("testfolder")
                .build();

        UUID folderId = UUID.randomUUID();

        Folder testFolder = Folder.builder()
                .id(folderId)
                .name("testfolder")
                .user(testUser)
                .build();

        FolderResponseDto expectedResponse = FolderResponseDto.builder()
                .id(folderId)
                .name("testfolder")
                .build();

        Mockito.when(folderRepository.save(Mockito.any(Folder.class))).thenReturn(testFolder);
        Mockito.when(folderMapper.toDto(Mockito.any(Folder.class))).thenReturn(expectedResponse);

        FolderResponseDto actualResponse = folderService.createFolder(dto, testUser);

        Assertions.assertEquals(expectedResponse, actualResponse);
        Mockito.verify(folderRepository).save(Mockito.any(Folder.class));
    }

    @Test
    void findFolderById_correctId_returnFolder() {

        UUID folderId = UUID.randomUUID();

        User testUser = User.builder()
                .id(UUID.fromString("ea7c7381-a19c-49a9-92cd-d32c3db25092"))
                .username("testuser")
                .password("testpassword")
                .createdAt(Instant.now())
                .build();

        Folder testFolder = Folder.builder()
                .id(folderId)
                .name("testfolder")
                .user(testUser)
                .build();

        FolderResponseDto expectedResponse = FolderResponseDto.builder()
                .id(folderId)
                .name("testfolder")
                .build();

        Mockito.when(folderRepository.findByIdAndUser(folderId, testUser)).thenReturn(Optional.of(testFolder));
        Mockito.when(folderMapper.toDto(Mockito.any(Folder.class))).thenReturn(expectedResponse);

        FolderResponseDto actualResponse = folderService.findFolderById(testUser, folderId);

        Assertions.assertEquals(expectedResponse, actualResponse);
        Mockito.verify(folderRepository).findByIdAndUser(folderId, testUser);
    }

    @Test
    void findFolderById_incorrectId_returnNotFoundException() {

        UUID folderId = UUID.randomUUID();

        User testUser = User.builder()
                .id(UUID.fromString("ea7c7381-a19c-49a9-92cd-d32c3db25092"))
                .username("testuser")
                .password("testpassword")
                .createdAt(Instant.now())
                .build();

        Mockito.when(folderRepository.findByIdAndUser(folderId, testUser)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> {
            folderService.findFolderById(testUser, folderId);
        });
    }

    @Test
    void updateFolder_correctId_returnUpdatedItem() {

        UUID folderId = UUID.randomUUID();

        User testUser = User.builder()
                .id(UUID.fromString("ea7c7381-a19c-49a9-92cd-d32c3db25092"))
                .username("testuser")
                .password("testpassword")
                .createdAt(Instant.now())
                .build();

        Folder testFolder = Folder.builder()
                .id(folderId)
                .name("testfolder")
                .user(testUser)
                .build();

        Folder updatedFolder = Folder.builder()
                .id(folderId)
                .name("updatedfolder")
                .user(testUser)
                .build();

        FolderDto updatedFolderDto = FolderDto.builder()
                .name("updatedfolder")
                .build();

        FolderResponseDto expectedResponse = FolderResponseDto.builder()
                .id(folderId)
                .name("testfolder")
                .build();

        Mockito.when(folderRepository.findByIdAndUser(folderId, testUser)).thenReturn(Optional.of(testFolder));
        Mockito.when(folderRepository.save(Mockito.any(Folder.class))).thenReturn(updatedFolder);
        Mockito.when(folderMapper.toDto(Mockito.any(Folder.class))).thenReturn(expectedResponse);

        FolderResponseDto actualResponse = folderService.updateFolder(testUser, folderId, updatedFolderDto);

        Assertions.assertEquals(expectedResponse, actualResponse);

        Mockito.verify(folderRepository).findByIdAndUser(folderId, testUser);
        Mockito.verify(folderRepository).save(Mockito.any(Folder.class));
        Mockito.verify(folderMapper).toDto(Mockito.any(Folder.class));
    }

    @Test
    void updateFolder_incorrectId_returnNotFoundException() {

        UUID incorrectItemId = UUID.randomUUID();

        User testUser = User.builder()
                .id(UUID.fromString("ea7c7381-a19c-49a9-92cd-d32c3db25092"))
                .username("testuser")
                .password("testpassword")
                .createdAt(Instant.now())
                .build();

        FolderDto updatedFolderDto = FolderDto.builder()
                .name("updatedfolder")
                .build();

        Mockito.when(folderRepository.findByIdAndUser(incorrectItemId, testUser)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> {
            folderService.updateFolder(testUser, incorrectItemId, updatedFolderDto);
        });

        Mockito.verify(folderRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void deleteFolder_correctId_success() {

        UUID folderId = UUID.randomUUID();

        User testUser = User.builder()
                .id(UUID.fromString("ea7c7381-a19c-49a9-92cd-d32c3db25092"))
                .username("testuser")
                .password("testpassword")
                .createdAt(Instant.now())
                .build();

        Folder testFolder = Folder.builder()
                .id(folderId)
                .name("testfolder")
                .user(testUser)
                .build();

        Mockito.when(folderRepository.findByIdAndUser(folderId, testUser)).thenReturn(Optional.of(testFolder));

        folderService.deleteFolder(testUser, folderId);

        Mockito.verify(folderRepository).delete(Mockito.any());
    }

    @Test
    void deleteFolder_incorrectId_returnNotFoundException() {

        UUID incorrectFolderId = UUID.randomUUID();

        User testUser = User.builder()
                .id(UUID.fromString("ea7c7381-a19c-49a9-92cd-d32c3db25092"))
                .username("testuser")
                .password("testpassword")
                .createdAt(Instant.now())
                .build();

        Mockito.when(folderRepository.findByIdAndUser(incorrectFolderId, testUser)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> {
            folderService.deleteFolder(testUser, incorrectFolderId);
        });

        Mockito.verify(folderRepository, Mockito.never()).delete(Mockito.any());
    }
}
