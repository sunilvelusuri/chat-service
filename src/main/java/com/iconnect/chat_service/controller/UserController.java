package com.iconnect.chat_service.controller;


import com.iconnect.chat_service.controller.dto.LoginRequest;
import com.iconnect.chat_service.controller.dto.MatrixLoginResponse;
import com.iconnect.chat_service.controller.dto.UserRegisterRequest;
import com.iconnect.chat_service.entity.UserMapping;
import com.iconnect.chat_service.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @PostMapping("/register")
//    public ResponseEntity<?> register(
//            @RequestParam String userId) {
//
//        return ResponseEntity.ok(
//                userService.registerUser(userId));
//    }

    @PostMapping("/register")
    public ResponseEntity<UserMapping> register(@RequestBody UserRegisterRequest request) {

        UserMapping result =
                userService.registerUser(request.getIconnectUserId());

        System.out.println("called");
        return ResponseEntity.ok(result);
    }
    @PostMapping("/login")
    public MatrixLoginResponse login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }

}

