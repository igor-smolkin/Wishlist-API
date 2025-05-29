package org.ataraxii.wishlist.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ataraxii.wishlist.database.entity.Folder;
import org.ataraxii.wishlist.database.entity.User;
import org.ataraxii.wishlist.database.repository.FolderRepository;
import org.ataraxii.wishlist.dto.folder.FolderDto;
import org.ataraxii.wishlist.dto.folder.FolderResponseDto;
import org.ataraxii.wishlist.exception.NotFoundException;
import org.ataraxii.wishlist.mapper.FolderMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;
    private final FolderMapper folderMapper;

    @Transactional
    public FolderResponseDto createFolder(FolderDto dto, User currentUser) {
        log.info("Создание папки {} пользователем {}", dto.getName(), currentUser.getUsername());
        Folder folder = Folder.builder()
                .name(dto.getName())
                .user(currentUser)
                .build();
        folderRepository.save(folder);
        log.info("Папка {} успешно создана пользователем {}", dto.getName(), currentUser.getUsername());
        return folderMapper.toDto(folder);
    }

    public List<FolderResponseDto> findAllFolders(User currentUser) {
        List<Folder> folders = folderRepository.findByUser(currentUser);
        log.info("Найдено {} папок у пользователя {}", folders.size(), currentUser.getUsername());
        return folders.stream()
                .map(folderMapper::toDto)
                .collect(Collectors.toList());
    }

    public FolderResponseDto findFolderById(User currentUser, UUID id) {
        Folder folder = folderRepository.findByIdAndUser(id, currentUser)
                .orElse(null);

        if (folder == null) {
            log.warn("Ошибка поиска папки: папка с id={} не найдена у пользователя {}", id, currentUser.getUsername());
            throw new NotFoundException("Папка с таким id не найдена");
        }
        log.info("Папка {} найдена у пользователя {}", folder.getName(), currentUser.getUsername());
        return folderMapper.toDto(folder);
    }

    public FolderResponseDto updateFolder(User currentUser, UUID id, FolderDto dto) {
        Folder folder = folderRepository.findByIdAndUser(id, currentUser)
                .orElse(null);

        if (folder == null) {
            log.warn("Ошибка обновления папки: папка с id={} не найдена у пользователя {}", id, currentUser.getUsername());
            throw new NotFoundException("Папка с таким id не найдена");
        }
        log.info("Обновление папки {} пользователем {}", folder.getName(), currentUser.getUsername());

        String oldName = folder.getName();
        folder.setName(dto.getName());
        log.info("Имя папки успешно изменено: {} -> {}", oldName, folder.getName());

        folderRepository.save(folder);
        return folderMapper.toDto(folder);
    }

    @Transactional
    public void deleteFolder(User currentUser, UUID id) {
        Folder folder = folderRepository.findByIdAndUser(id, currentUser)
                .orElse(null);
        if (folder == null) {
            log.warn("Ошибка удаления папки: папка с id={} не найдена у пользователя {}", id, currentUser.getUsername());
            throw new NotFoundException("Папка с таким id не найдена");
        }
        folderRepository.delete(folder);
        log.info("Папка {} удалена пользователем {}", folder.getName(), currentUser.getUsername());
    }
}
