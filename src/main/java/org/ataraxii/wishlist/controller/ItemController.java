package org.ataraxii.wishlist.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.ataraxii.wishlist.dto.item.ItemDto;
import org.ataraxii.wishlist.dto.item.ItemResponseDto;
import org.ataraxii.wishlist.security.JwtUser;
import org.ataraxii.wishlist.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/prod/")
public class ItemController {

    private final ItemService itemService;

//    @PostMapping("/items")
//    public ResponseEntity<ItemResponseDto> createItem(@RequestBody ItemDto dto,
//                                                      HttpServletRequest request) {
//        User currentUser = (User) request.getAttribute("currentUser");
//        ItemResponseDto response = itemService.createItem(dto, currentUser);
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }
//
//    @GetMapping("/items")
//    public ResponseEntity<List<ItemResponseDto>> findAllItems(HttpServletRequest request) {
//        User currentUser = (User) request.getAttribute("currentUser");
//        List<ItemResponseDto> items = itemService.findAllItems(currentUser);
//        return ResponseEntity.status(HttpStatus.OK).body(items);
//    }

    @GetMapping("/items/{id}")
    public ResponseEntity<ItemResponseDto> findItemById(@PathVariable UUID id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtUser user = (JwtUser) authentication.getPrincipal();
        UUID userId = user.getUserId();
        ItemResponseDto response = itemService.findItemById(userId, id);
        return ResponseEntity.ok().body(response);
    }

//    @PutMapping("/items/{id}")
//    public ResponseEntity<ItemResponseDto> updateItem(HttpServletRequest request,
//                                                      @PathVariable UUID id,
//                                                      @RequestBody ItemDto dto) {
//        User currentUser = (User) request.getAttribute("currentUser");
//        ItemResponseDto item = itemService.updateItem(currentUser, id, dto);
//        return ResponseEntity.status(HttpStatus.OK).body(item);
//    }
//
//    @DeleteMapping("/items/{id}")
//    public ResponseEntity<Void> deleteItem(HttpServletRequest request,
//                                           @PathVariable UUID id) {
//        User currentUser = (User) request.getAttribute("currentUser");
//        itemService.deleteItem(currentUser, id);
//        return ResponseEntity.status(HttpStatus.OK).build();
//    }
}
