package org.ataraxii.wishlist.dto.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ItemDto {
    @Size(max = 32)
    @NotBlank
    private String name;

    @Size(max = 255)
    @NotBlank
    private String url;

    private UUID folderId;
}
