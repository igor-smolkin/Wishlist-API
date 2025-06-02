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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

        when(folderRepository.save(any(Folder.class))).thenReturn(testFolder);
        when(folderMapper.toDto(any(Folder.class))).thenReturn(expectedResponse);

        FolderResponseDto actualResponse = folderService.createFolder(dto, testUser);

        assertEquals(expectedResponse, actualResponse);
        verify(folderRepository).save(any(Folder.class));
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

        when(folderRepository.findByIdAndUser(folderId, testUser)).thenReturn(Optional.of(testFolder));
        when(folderMapper.toDto(any(Folder.class))).thenReturn(expectedResponse);

        FolderResponseDto actualResponse = folderService.findFolderById(testUser, folderId);

        assertEquals(expectedResponse, actualResponse);
        verify(folderRepository).findByIdAndUser(folderId, testUser);
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

        when(folderRepository.findByIdAndUser(folderId, testUser)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
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

        when(folderRepository.findByIdAndUser(folderId, testUser)).thenReturn(Optional.of(testFolder));
        when(folderRepository.save(any(Folder.class))).thenReturn(updatedFolder);
        when(folderMapper.toDto(any(Folder.class))).thenReturn(expectedResponse);

        FolderResponseDto actualResponse = folderService.updateFolder(testUser, folderId, updatedFolderDto);

        assertEquals(expectedResponse, actualResponse);

        verify(folderRepository).findByIdAndUser(folderId, testUser);
        verify(folderRepository).save(any(Folder.class));
        verify(folderMapper).toDto(any(Folder.class));
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

        when(folderRepository.findByIdAndUser(incorrectItemId, testUser)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            folderService.updateFolder(testUser, incorrectItemId, updatedFolderDto);
        });

        verify(folderRepository, never()).save(any());
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

        when(folderRepository.findByIdAndUser(folderId, testUser)).thenReturn(Optional.of(testFolder));

        folderService.deleteFolder(testUser, folderId);

        verify(folderRepository).delete(any());
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

        when(folderRepository.findByIdAndUser(incorrectFolderId, testUser)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            folderService.deleteFolder(testUser, incorrectFolderId);
        });

        verify(folderRepository, never()).delete(any());
    }
}
