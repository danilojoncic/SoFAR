package dj.nwp.sofar.controller;

import dj.nwp.sofar.dto.ServiceResponse;
import dj.nwp.sofar.service.ErrorMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/error")
@RequiredArgsConstructor
public class ErrorController {

    private final ErrorMessageService errorMessageService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllErrorMessages() {
        ServiceResponse sr = errorMessageService.getAllErrorMessages();
        return ResponseEntity.status(sr.code()).body(sr.content());
    }


    @GetMapping("/yourself-jwt")
    public ResponseEntity<?> getYourselfJwt(Authentication authentication) {
        if (authentication == null || authentication.getAuthorities() == null) {
            return ResponseEntity.status(401).body("Unauthorized: No authentication found");
        }
        ServiceResponse sr = errorMessageService.getAllErrorMessagesFromOneUser(authentication.getName());
        return ResponseEntity.status(sr.code()).body(sr.content());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getEmailError(@PathVariable String email) {
        ServiceResponse sr = errorMessageService.getAllErrorMessagesFromOneUser(email);
        return ResponseEntity.status(sr.code()).body(sr.content());
    }

    @GetMapping("/pg")
    public ResponseEntity<?> getAllErrorsPaginated(@RequestParam Integer page,
                                                   @RequestParam Integer size) {
        ServiceResponse sr = errorMessageService.getAllErrorMessagesPaginated(page,size);
        return ResponseEntity.status(sr.code()).body(sr.content());
    }


}
