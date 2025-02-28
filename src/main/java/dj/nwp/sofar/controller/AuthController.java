package dj.nwp.sofar.controller;

import dj.nwp.sofar.dto.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping
    ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return null;
    }

}
