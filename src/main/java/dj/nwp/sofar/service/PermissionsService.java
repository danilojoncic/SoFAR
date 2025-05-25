package dj.nwp.sofar.service;

import dj.nwp.sofar.dto.ServiceResponse;
import dj.nwp.sofar.service.abstraction.PermissionsAbs;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PermissionsService implements PermissionsAbs {
    @Override
    public ServiceResponse getPermissions() {
        return null;
    }

    @Override
    public ServiceResponse getPermissionsString() {
        return null;
    }
}
