package dj.nwp.sofar.service.abstraction;

import dj.nwp.sofar.dto.ServiceResponse;

public interface PermissionsAbs {
    ServiceResponse getPermissions();
    ServiceResponse getPermissionsString();
}
