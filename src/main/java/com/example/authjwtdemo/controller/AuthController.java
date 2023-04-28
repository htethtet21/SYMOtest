package com.example.authjwtdemo.controller;

import com.example.authjwtdemo.data.User;
import com.example.authjwtdemo.service.UserService;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    record RegisterRequest(@JsonProperty("first_name") String firstName,
                           @JsonProperty("last_name") String lastName, String email, String password,
                           @JsonProperty("confirm_password") String confirm_password) {
    }

    record RegisterResponse(@JsonProperty("first_name") String firstName,
                            @JsonProperty("last_name") String lastName, String email) {
    }


    //curl -X POST -H "Content-Type: application/json" -d '{"first_name":"Angle","last_name":"Chan","email":"john@gmail.com","password":"12345","confirm_password":"12345"}' localhost:8000/api/register
    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest registerRequest) throws Exception {
        User user = userService.register(
                registerRequest.firstName(),
                registerRequest.lastName(),
                registerRequest.email(),
                registerRequest.password(),
                registerRequest.confirm_password()
        );
        return new RegisterResponse(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );

    }

    record LoginRequest(String email, String password) {
    }

    record LoginResponse(String token) {
    }

    //curl -X POST -H "Content-Type: application/json" -d '{"email":"angle@gmail.com","password":"12345"}' localhost:8000/api/login
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        var login = userService.login(loginRequest.email(), loginRequest.password());
        Cookie cookie = new Cookie("refresh_token", login.getRefreshToken().getToken());
        cookie.setMaxAge(3600);
        cookie.setHttpOnly(true);
        cookie.setPath("/api");
        response.addCookie(cookie);
        return new LoginResponse(
                login.getAccessToken().getToken()

        );
    }

    record UserResponse(Long id, @JsonProperty("first_name") String firstName,
                        @JsonProperty("last_name") String lastName, String email) {
    }


    @GetMapping("/user")
    public UserResponse user(HttpServletRequest request) {
        var user = (User) request.getAttribute("user");
        return new UserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());

    }

    record RefreshResponse(String token) {
    }

    @PostMapping("/refresh")
    public RefreshResponse refresh(@CookieValue("refresh_token") String refreshToken) {
        return new RefreshResponse(userService.refreshAccess(refreshToken)
                .getAccessToken()
                .getToken());
    }

    record LogoutResponse(String msg) {

    }

    @PostMapping("/logout")
    public LogoutResponse logout(@CookieValue("refresh_token") String refreshToken, HttpServletResponse response) {
        userService.logout(refreshToken);
        Cookie cookie = new Cookie("refresh_token", null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return new LogoutResponse("successful Logout!");
    }

    record ForgotRequest(String email) {
    }

    record ForgotResponse(String message) {
    }

    @PostMapping("/forgot")
    public ForgotResponse forgot(@RequestBody ForgotRequest forgotRequest, HttpServletRequest request) {
        var originUrl = request.getHeader("Origin");
        userService.forgot(forgotRequest.email, originUrl);
        return new ForgotResponse("success");

    }

    record ResetRequest(String token, String password, @JsonProperty("password_confirm") String passwordConfirm) {

    }

    record RequestResponse(String message) {
    }

    @PostMapping("/reset")
    public RequestResponse reset(@RequestBody ResetRequest resetRequest) {
        userService.reset(resetRequest.token(), resetRequest.password(), resetRequest.passwordConfirm());
        return new RequestResponse("success reset password");
    }

}
