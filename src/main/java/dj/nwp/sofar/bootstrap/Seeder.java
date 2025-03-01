package dj.nwp.sofar.bootstrap;

import dj.nwp.sofar.dto.UserOperation;
import dj.nwp.sofar.model.Permission;
import dj.nwp.sofar.repository.PermissionRepository;
import dj.nwp.sofar.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Seeder implements CommandLineRunner {

    private final UserService userService;
    private final PermissionRepository permissionRepository;

    public Seeder(UserService userService, PermissionRepository permissionRepository) {
        this.userService = userService;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        Permission p1 = new Permission("CAN_VIEW");
        Permission p2 = new Permission("CAN_EDIT");
        Permission p3 = new Permission("CAN_DELETE");
        Permission p4 = new Permission("CAN_CREATE");
        List<Permission> permissions = List.of(
            p1,p2,p3,p4
        );
        permissionRepository.saveAll(permissions);

        UserOperation u1 = new UserOperation(
                "Edmund",
                "Blackadder",
                "black@black.com",
                "pass",
                List.of(p1.getTitle(),p2.getTitle(),p3.getTitle(),p4.getTitle())
        );


        UserOperation u2 = new UserOperation(
                "Captain",
                "Darling",
                "darling@cpt.com",
                "passkey",
                List.of(p1.getTitle(),p2.getTitle(),p3.getTitle(),p4.getTitle())
        );

        UserOperation u3 = new UserOperation(
                "Baldrick",
                "Sodoff",
                "sod@off.com",
                "word",
                List.of(p1.getTitle())
        );

        userService.createUser(u1);
        userService.createUser(u2);
        userService.createUser(u3);


    }
}
