package dj.nwp.sofar.mapper;

import dj.nwp.sofar.dto.UserOperation;
import dj.nwp.sofar.dto.UserPresentation;
import dj.nwp.sofar.model.Permission;
import dj.nwp.sofar.model.SUser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class UserMapper {
    public static UserPresentation UserToUserPresentation(SUser user) {
        return new UserPresentation(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                stringifyPermissions(user.getPermissions())
        );
    }

    public static UserOperation UserToUserOperation(SUser user) {
        return new UserOperation(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                stringifyPermissions(user.getPermissions())
        );
    }





    private static List<String> stringifyPermissions(Set<Permission> permissions) {
        List<String> permissionsList = new ArrayList<>();
        permissions.forEach(permission -> {
            permissionsList.add(permission.getTitle());
        });
        return permissionsList;
    }
}
