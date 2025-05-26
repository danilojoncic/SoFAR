package dj.nwp.sofar.service;

import dj.nwp.sofar.dto.ServiceResponse;
import dj.nwp.sofar.mapper.PermissionMapper;
import dj.nwp.sofar.repository.PermissionRepository;
import dj.nwp.sofar.service.abstraction.PermissionsAbs;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PermissionsService implements PermissionsAbs {
    private final PermissionRepository permissionRepository;
    @Override
    public ServiceResponse getPermissions() {
        return new ServiceResponse(200,permissionRepository.findAll());
    }

    @Override
    public ServiceResponse getPermissionsString() {
        List<String> perms = permissionRepository.findAll()
                .stream()
                .map(PermissionMapper::PermissionToString)
                .collect(Collectors.toList());
        return new ServiceResponse(200, perms);
    }
}
