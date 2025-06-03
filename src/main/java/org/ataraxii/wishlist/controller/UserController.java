package org.ataraxii.wishlist.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.ataraxii.wishlist.database.entity.User;
import org.ataraxii.wishlist.dto.user.ProfileResponseDto;
import org.ataraxii.wishlist.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ProfileResponseDto> getProfile(HttpServletRequest request) {
        User currentUser = (User) request.getAttribute("currentUser");
        ProfileResponseDto response = userService.getProfile(currentUser);
        return ResponseEntity.ok().body(response);
    }
}
