package org.ataraxii.wishlist.dto.item;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ItemResponseDto {
    private UUID id;
    private String name;
    private String url;
    private UUID user;
    private UUID folderId;
}
