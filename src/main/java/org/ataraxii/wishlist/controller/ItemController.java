package org.ataraxii.wishlist.controller;

import lombok.RequiredArgsConstructor;
import org.ataraxii.wishlist.dto.item.ItemDto;
import org.ataraxii.wishlist.dto.item.ItemResponseDto;
import org.ataraxii.wishlist.security.SecurityUtil;
import org.ataraxii.wishlist.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/prod/")
public class ItemController {

    private final ItemService itemService;
    private final SecurityUtil securityUtil;

    @PostMapping("/items")
    public ResponseEntity<ItemResponseDto> createItem(@RequestBody ItemDto dto) {
        UUID userId = securityUtil.getCurrentUserId();
        ItemResponseDto response = itemService.createItem(dto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/items")
    public ResponseEntity<List<ItemResponseDto>> findAllItems() {
        UUID userId = securityUtil.getCurrentUserId();
        List<ItemResponseDto> items = itemService.findAllItems(userId);
        return ResponseEntity.status(HttpStatus.OK).body(items);
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<ItemResponseDto> findItemById(@PathVariable UUID id) {
        UUID userId = securityUtil.getCurrentUserId();
        ItemResponseDto response = itemService.findItemById(userId, id);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<ItemResponseDto> updateItem(@PathVariable UUID id,
                                                      @RequestBody ItemDto dto) {
        UUID userId = securityUtil.getCurrentUserId();
        ItemResponseDto item = itemService.updateItem(userId, id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(item);
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable UUID id) {
        UUID userId = securityUtil.getCurrentUserId();
        itemService.deleteItem(userId, id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
