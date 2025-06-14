package dj.nwp.sofar.controller;

import dj.nwp.sofar.dto.ServiceResponse;
import dj.nwp.sofar.service.PermissionsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/permissions")
@AllArgsConstructor
public class PermissionsController {

    private final PermissionsService permissionsService;


    @GetMapping("/as-object")
    public ResponseEntity<?> getAllPermissionsObjects(){
        ServiceResponse sr = permissionsService.getPermissions();
        return ResponseEntity.status(sr.code()).body(sr.content());
    }


    @GetMapping("/as-string")
    public ResponseEntity<?> getAllPermissionsString(){
        ServiceResponse sr = permissionsService.getPermissionsString();
        return ResponseEntity.status(sr.code()).body(sr.content());
    }
}
