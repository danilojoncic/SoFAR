package dj.nwp.sofar.controller;
import dj.nwp.sofar.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return null;
    }

    @GetMapping("/pg")
    public ResponseEntity<?> getAllUsersPaginated(@RequestParam Integer page,
                                                  @RequestParam Integer size) {
        return null;
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getOneUser(@PathVariable Long id) {
        return null;
    }


    @PostMapping("/create")
    public ResponseEntity<?> createUser() {
        return null;
    }



    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editUser(@PathVariable Long id) {
        return null;
    }



    @PutMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return null;
    }




}
