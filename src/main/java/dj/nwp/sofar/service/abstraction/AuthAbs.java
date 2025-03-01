package dj.nwp.sofar.service.abstraction;

import dj.nwp.sofar.dto.LoginRequest;
import dj.nwp.sofar.dto.ServiceResponse;

public interface AuthAbs {
    ServiceResponse login(LoginRequest dto);
}
