package dj.nwp.sofar.mapper;

import dj.nwp.sofar.model.Permission;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PermissionMapper {
    public static String PermissionToString(Permission permission) {
        return permission.getTitle();
    }
}
