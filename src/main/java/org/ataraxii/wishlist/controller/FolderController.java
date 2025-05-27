package org.ataraxii.wishlist.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.ataraxii.wishlist.database.entity.User;
import org.ataraxii.wishlist.dto.folder.FolderDto;
import org.ataraxii.wishlist.dto.folder.FolderResponseDto;
import org.ataraxii.wishlist.service.FolderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;

    @PostMapping("/folders")
    public ResponseEntity<FolderResponseDto> createFolder(@RequestBody FolderDto dto,
                                                          HttpServletRequest request) {
        User currentUser = (User) request.getAttribute("currentUser");
        FolderResponseDto response = folderService.createFolder(dto, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/folders")
    public ResponseEntity<List<FolderResponseDto>> findAllFolders(HttpServletRequest request) {
        User currentUser = (User) request.getAttribute("currentUser");
        List<FolderResponseDto> response = folderService.findAllFolders(currentUser);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/folders/{id}")
    public ResponseEntity<FolderResponseDto> findFolderById(HttpServletRequest request,
                                                            @PathVariable UUID id) {
        User currentUser = (User) request.getAttribute("currentUser");
        FolderResponseDto response = folderService.findFolderById(currentUser, id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/folders/{id}")
    public ResponseEntity<FolderResponseDto> updateFolder(HttpServletRequest request,
                                                          @PathVariable UUID id,
                                                          FolderDto dto) {
        User currentUser = (User) request.getAttribute("currentUser");
        FolderResponseDto response = folderService.updateFolder(currentUser, id ,dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/folders/{id}")
    public ResponseEntity<Void> deleteFolder(HttpServletRequest request,
                                             @PathVariable UUID id) {
        User currentUser = (User) request.getAttribute("currentUser");
        folderService.deleteFolder(currentUser, id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
