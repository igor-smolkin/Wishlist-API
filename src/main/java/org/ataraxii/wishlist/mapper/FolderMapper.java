package org.ataraxii.wishlist.mapper;

import org.ataraxii.wishlist.database.entity.Folder;
import org.ataraxii.wishlist.dto.folder.FolderResponseDto;
import org.springframework.stereotype.Component;

@Component
public class FolderMapper {
    public FolderResponseDto toDto(Folder folder) {
        return FolderResponseDto.builder()
                .id(folder.getId())
                .name(folder.getName())
                .build();
    }
}
