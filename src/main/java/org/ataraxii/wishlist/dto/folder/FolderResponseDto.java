package org.ataraxii.wishlist.dto.folder;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class FolderResponseDto {
    private UUID id;
    private String name;
}
