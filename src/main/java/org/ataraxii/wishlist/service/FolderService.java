package org.ataraxii.wishlist.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ataraxii.wishlist.database.entity.Folder;
import org.ataraxii.wishlist.database.entity.User;
import org.ataraxii.wishlist.database.repository.FolderRepository;
import org.ataraxii.wishlist.dto.folder.FolderDto;
import org.ataraxii.wishlist.dto.folder.FolderResponseDto;
import org.ataraxii.wishlist.exception.ConflictException;
import org.ataraxii.wishlist.exception.NotFoundException;
import org.ataraxii.wishlist.mapper.FolderMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;
    private final FolderMapper folderMapper;

    @Transactional
    public FolderResponseDto createFolder(FolderDto dto, User currentUser) {
        Folder folder = Folder.builder()
                .name(dto.getName())
                .user(currentUser)
                .build();
        folderRepository.save(folder);

        return folderMapper.toDto(folder);
    }

    public List<FolderResponseDto> findAllFolders(User currentUser) {
        List<Folder> folders = folderRepository.findByUser(currentUser);
        return folders.stream()
                .map(folderMapper::toDto)
                .collect(Collectors.toList());
    }

    public FolderResponseDto findFolderById(User currentUser, UUID id) {
        Folder folder = folderRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new NotFoundException("Папка с таким id не найдена"));
        return folderMapper.toDto(folder);
    }

    public FolderResponseDto updateFolder(User currentUser, UUID id, FolderDto dto) {
        Folder folder = folderRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new NotFoundException("Папка с таким id не найдена"));
        folder.setName(dto.getName());

        folderRepository.save(folder);
        return folderMapper.toDto(folder);
    }

    @Transactional
    public void deleteFolder(User currentUser, UUID id) {
        Folder folder = folderRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new NotFoundException("Папка с таким id не найдена"));
        folderRepository.delete(folder);
    }
}
