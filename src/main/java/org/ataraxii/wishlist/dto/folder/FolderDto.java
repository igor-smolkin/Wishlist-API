package org.ataraxii.wishlist.dto.folder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FolderDto {
    @Size(max = 32)
    @NotBlank
    private String name;
}
