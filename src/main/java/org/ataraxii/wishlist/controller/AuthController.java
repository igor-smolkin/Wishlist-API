package org.ataraxii.wishlist.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ataraxii.wishlist.dto.authentication.AuthRequestDto;
import org.ataraxii.wishlist.dto.authentication.AuthResponseDto;
import org.ataraxii.wishlist.dto.authentication.UserDto;
import org.ataraxii.wishlist.service.AuthService;
import org.ataraxii.wishlist.service.SessionService;
import org.ataraxii.wishlist.utils.CookieUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthService authService;
    private final SessionService sessionService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody @Valid AuthRequestDto request, HttpServletResponse response) {
        AuthResponseDto auth = authService.registerUser(request);
//        Присвоение сессии сразу после регистрации (убрать/добавить по желанию)
//        CookieUtil.addSessionCookie(response, auth.getSession().getSessionId());
        return ResponseEntity.status(HttpStatus.CREATED).body(auth.getUser());
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody @Valid AuthRequestDto request, HttpServletResponse response) {
        AuthResponseDto auth = authService.loginUser(request);
        CookieUtil.addSessionCookie(response, auth.getSession().getSessionId());
        return ResponseEntity.status(HttpStatus.OK).body(auth.getUser());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(value = "sessionId", required = false) String sessionId,
                                       HttpServletResponse response) {
        sessionService.deleteBySessionId(sessionId);
        CookieUtil.deleteSessionCookie(response);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
