package dj.nwp.sofar.service;

import dj.nwp.sofar.dto.ServiceResponse;
import dj.nwp.sofar.dto.UserOperation;
import dj.nwp.sofar.service.abstraction.UserAbs;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserAbs {
    @Override
    public ServiceResponse getUsers() {
        return null;
    }

    @Override
    public ServiceResponse getUsersPaginated(Integer page, Integer size) {
        return null;
    }

    @Override
    public ServiceResponse getOneUserBy(Long id) {
        return null;
    }

    @Override
    public ServiceResponse createUser(UserOperation dto) {
        return null;
    }

    @Override
    public ServiceResponse deleteUser(Long id) {
        return null;
    }

    @Override
    public ServiceResponse editUser(Long id, UserOperation dto) {
        return null;
    }
}
