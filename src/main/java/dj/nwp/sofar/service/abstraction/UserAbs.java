package dj.nwp.sofar.service.abstraction;

import dj.nwp.sofar.dto.UserOperation;
import dj.nwp.sofar.dto.ServiceResponse;

public interface UserAbs {
    ServiceResponse getUsers();
    ServiceResponse getUsersPaginated(Integer page, Integer size);
    ServiceResponse getOneUserBy(Long id);
    ServiceResponse createUser(UserOperation dto);
    ServiceResponse deleteUser(Long id);
    ServiceResponse editUser(Long id, UserOperation dto);
}
