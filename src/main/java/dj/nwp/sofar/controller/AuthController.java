package dj.nwp.sofar.controller;

import dj.nwp.sofar.dto.LoginRequest;
import dj.nwp.sofar.dto.ServiceResponse;
import dj.nwp.sofar.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final LoginService loginService;

    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        ServiceResponse sr = loginService.login(loginRequest);
        return ResponseEntity.status(sr.code()).body(sr.content());
    }

}
