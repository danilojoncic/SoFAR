package dj.nwp.sofar.controller;
import dj.nwp.sofar.dto.ServiceResponse;
import dj.nwp.sofar.dto.UserOperation;
import dj.nwp.sofar.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        ServiceResponse sr = userService.getUsers();
        return ResponseEntity.status(sr.code()).body(sr.content());
    }

    @GetMapping("/pg")
    public ResponseEntity<?> getAllUsersPaginated(@RequestParam Integer page,
                                                  @RequestParam Integer size) {
        ServiceResponse sr = userService.getUsersPaginated(page, size);
        return ResponseEntity.status(sr.code()).body(sr.content());
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getOneUser(@PathVariable Long id) {
        ServiceResponse sr = userService.getOneUserBy(id);
        return ResponseEntity.status(sr.code()).body(sr.content());
    }


    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserOperation dto) {
        ServiceResponse sr = userService.createUser(dto);
        return ResponseEntity.status(sr.code()).body(sr.content());
    }



    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editUser(@PathVariable Long id, @RequestBody @Valid UserOperation dto) {
        ServiceResponse sr = userService.editUser(id,dto);
        return ResponseEntity.status(sr.code()).body(sr.content());
    }



    @PutMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        ServiceResponse sr = userService.deleteUser(id);
        return ResponseEntity.status(sr.code()).body(sr.content());
    }




}
