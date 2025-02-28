package dj.nwp.sofar.service;

import dj.nwp.sofar.dto.Message;
import dj.nwp.sofar.dto.ServiceResponse;
import dj.nwp.sofar.dto.UserOperation;
import dj.nwp.sofar.model.Permission;
import dj.nwp.sofar.model.User;
import dj.nwp.sofar.repository.PermissionRepository;
import dj.nwp.sofar.repository.UserRepository;
import dj.nwp.sofar.service.abstraction.UserAbs;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService implements UserAbs {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;


    @Override
    public ServiceResponse getUsers() {
        return new ServiceResponse(200,userRepository.findAll());
    }

    @Override
    public ServiceResponse getUsersPaginated(Integer page, Integer size) {
        PageRequest pg = PageRequest.of(page, size);
        return new ServiceResponse(200,userRepository.findAll(pg));
    }

    @Override
    public ServiceResponse getOneUserBy(Long id) {
        return userRepository.findById(id)
                .map(user -> new ServiceResponse(200,user))
                .orElseGet(()->new ServiceResponse(404,new Message("User with id "+id+" does not exist")));
    }

    @Transactional
    @Override
    public ServiceResponse createUser(UserOperation dto) {
        if (userRepository.findByEmail(dto.email()).isPresent()) {
            return new ServiceResponse(401, new Message("User with email " + dto.email() + " already exists"));
        }
        if(dto.permissions() == null || (!checkPermission(dto.permissions()))) {
            return new ServiceResponse(401, new Message("Bad Permissions"));
        }

        User user = new User();
        user.setEmail(dto.email());
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setPassword(dto.password());

        Set<Permission> userPerms = new HashSet<>();


        dto.permissions().forEach(perm -> {
            Permission permission = permissionRepository.findByTitle(perm).get();
            userPerms.add(permission);
        });

        user.setPermissions(userPerms);
        userRepository.save(user);

        return new ServiceResponse(201, new Message("User created!"));
    }

    @Transactional
    @Override
    public ServiceResponse deleteUser(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.deleteById(id);
                    return new ServiceResponse(201,new Message("User with id "+id+" deleted"));
                })
                .orElseGet(()->new ServiceResponse(404,new Message("User with id "+id+" does not exist")));
    }

    @Transactional
    @Override
    public ServiceResponse editUser(Long id, UserOperation dto) {
        return userRepository.findById(id).map(user -> {
            if (dto.firstName() != null) user.setFirstName(dto.firstName());
            if (dto.lastName() != null) user.setLastName(dto.lastName());

            if (dto.email() != null && userRepository.findByEmail(dto.email())
                    .filter(existingUser -> !existingUser.getId().equals(id))
                    .isPresent()) {
                return new ServiceResponse(401, new Message("Email is already in use by another user"));
            }


            if (dto.email() != null) user.setEmail(dto.email());
            if (dto.password() != null) user.setPassword(dto.password());

            if (dto.permissions() != null) {
                if (!checkPermission(dto.permissions())) {
                    return new ServiceResponse(401, new Message("Bad Permissions"));
                }
                Set<Permission> userPerms = new HashSet<>();
                dto.permissions().forEach(perm -> {
                    permissionRepository.findByTitle(perm).ifPresent(userPerms::add);
                });
                user.setPermissions(userPerms);
            }

            userRepository.save(user);
            return new ServiceResponse(200, new Message("User updated successfully!"));

        }).orElseGet(() -> new ServiceResponse(404, new Message("User does not exist")));
    }



    private boolean checkPermission(List<String> stringPerms) {
        return stringPerms.stream()
                .allMatch(perm -> permissionRepository.findByTitle(perm).isPresent());
    }

}
