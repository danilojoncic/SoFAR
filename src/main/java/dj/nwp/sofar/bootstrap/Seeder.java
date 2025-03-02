package dj.nwp.sofar.bootstrap;

import dj.nwp.sofar.dto.DishOperation;
import dj.nwp.sofar.dto.UserOperation;
import dj.nwp.sofar.model.Permission;
import dj.nwp.sofar.repository.DishRepository;
import dj.nwp.sofar.repository.PermissionRepository;
import dj.nwp.sofar.service.DishService;
import dj.nwp.sofar.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Seeder implements CommandLineRunner {

    private final UserService userService;
    private final PermissionRepository permissionRepository;
    private final DishService dishService;

    public Seeder(UserService userService, PermissionRepository permissionRepository, DishService dishService) {
        this.userService = userService;
        this.permissionRepository = permissionRepository;
        this.dishService = dishService;
    }

    @Override
    public void run(String... args) throws Exception {

        Permission p1 = new Permission("CAN_VIEW");
        Permission p2 = new Permission("CAN_EDIT");
        Permission p3 = new Permission("CAN_DELETE");
        Permission p4 = new Permission("CAN_CREATE");
        //EARCH, PLACE_ORDER, CANCEL, TRACK, SCHEDULE
        Permission p5 = new Permission("CAN_SEARCH_ORDER");
        Permission p6 = new Permission("CAN_PLACE_ORDER");
        Permission p7 = new Permission("CAN_CANCEL_ORDER");
        Permission p8 = new Permission("CAN_TRACK_ORDER");
        Permission p9 = new Permission("CAN_SCHEDULE_ORDER");
        List<Permission> permissions = List.of(
            p1,p2,p3,p4,p5,p6,p7,p8,p9
        );
        permissionRepository.saveAll(permissions);

        UserOperation u1 = new UserOperation(
                "Edmund",
                "Blackadder",
                "black@black.com",
                "pass",
                List.of(p1.getTitle(),p2.getTitle(),p3.getTitle(),p4.getTitle(),p5.getTitle(),p6.getTitle(),p7.getTitle(),p8.getTitle(),p9.getTitle())
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


        DishOperation d1 = new DishOperation("Sathee","You take a rat, drown it in a big puddle then you marinte it in the same puddle, one of Baldrick's specialties");
        DishOperation d2 = new DishOperation("Fricassee","Same sa Sathee but just a slightly larger rat");
        DishOperation d3 = new DishOperation("Pigeon","Freshly shot carrier pigeon, high possibility of carrying General Melchet's orders (possibly to certain death)");
        DishOperation d4 = new DishOperation("Breakfast Buffet","General Melchet's buffet, enough food to feed a hungry trench for a week");

        dishService.createDish(d1);
        dishService.createDish(d2);
        dishService.createDish(d3);
        dishService.createDish(d4);
    }
}
