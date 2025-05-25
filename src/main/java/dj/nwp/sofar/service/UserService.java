package dj.nwp.sofar.service;

import dj.nwp.sofar.dto.Message;
import dj.nwp.sofar.dto.ServiceResponse;
import dj.nwp.sofar.dto.UserOperation;
import dj.nwp.sofar.dto.UserPresentation;
import dj.nwp.sofar.mapper.UserMapper;
import dj.nwp.sofar.model.Permission;
import dj.nwp.sofar.model.SUser;
import dj.nwp.sofar.repository.PermissionRepository;
import dj.nwp.sofar.repository.UserRepository;
import dj.nwp.sofar.service.abstraction.UserAbs;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService implements UserAbs {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public ServiceResponse getUsers() {
        return new ServiceResponse(
                200,
                userRepository.findAll()
                        .stream()
                        .map(UserMapper::UserToUserPresentation)
                        .toList()
        );
    }

    @Override
    public ServiceResponse getUsersPaginated(Integer page, Integer size) {
        PageRequest pg = PageRequest.of(page, size);
        Page<UserPresentation> result = userRepository.findAll(pg)
                .map(UserMapper::UserToUserPresentation);
        return new ServiceResponse(200, result);
    }

    @Override
    public ServiceResponse getOneUserBy(Long id) {
        return userRepository.findById(id)
                .map(SUser -> new ServiceResponse(200, UserMapper.UserToUserPresentation(SUser)))
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

        SUser SUser = new SUser();
        SUser.setEmail(dto.email());
        SUser.setFirstName(dto.firstName());
        SUser.setLastName(dto.lastName());
        SUser.setPassword(passwordEncoder.encode(dto.password()));

        Set<Permission> userPerms = new HashSet<>();


        dto.permissions().forEach(perm -> {
            Permission permission = permissionRepository.findByTitle(perm).get();
            userPerms.add(permission);
        });

        SUser.setPermissions(userPerms);
        userRepository.save(SUser);

        return new ServiceResponse(201, new Message("User created!"));
    }

    @Transactional
    @Override
    public ServiceResponse deleteUser(Long id) {
        return userRepository.findById(id)
                .map(SUser -> {
                    userRepository.deleteById(id);
                    return new ServiceResponse(201,new Message("User with id "+id+" deleted"));
                })
                .orElseGet(()->new ServiceResponse(404,new Message("User with id "+id+" does not exist")));
    }

    @Transactional
    @Override
    public ServiceResponse editUser(Long id, UserOperation dto) {
        return userRepository.findById(id).map(SUser -> {
            if (dto.firstName() != null) SUser.setFirstName(dto.firstName());
            if (dto.lastName() != null) SUser.setLastName(dto.lastName());

            if (dto.email() != null && userRepository.findByEmail(dto.email())
                    .filter(existingSUser -> !existingSUser.getId().equals(id))
                    .isPresent()) {
                return new ServiceResponse(401, new Message("Email is already in use by another user"));
            }


            if (dto.email() != null) SUser.setEmail(dto.email());
            if (dto.password() != null) SUser.setPassword(dto.password());

            if (dto.permissions() != null) {
                if (!checkPermission(dto.permissions())) {
                    return new ServiceResponse(401, new Message("Bad Permissions"));
                }
                Set<Permission> userPerms = new HashSet<>();
                dto.permissions().forEach(perm -> {
                    permissionRepository.findByTitle(perm).ifPresent(userPerms::add);
                });
                SUser.setPermissions(userPerms);
            }

            userRepository.save(SUser);
            return new ServiceResponse(200, new Message("User updated successfully!"));

        }).orElseGet(() -> new ServiceResponse(404, new Message("User does not exist")));
    }



    private boolean checkPermission(List<String> stringPerms) {
        return stringPerms.stream()
                .allMatch(perm -> permissionRepository.findByTitle(perm).isPresent());
    }

}
