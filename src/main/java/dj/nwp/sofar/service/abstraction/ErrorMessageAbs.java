package dj.nwp.sofar.service.abstraction;

import dj.nwp.sofar.dto.ServiceResponse;

public interface ErrorMessageAbs {
    ServiceResponse getAllErrorMessages();
    ServiceResponse getAllErrorMessagesPaginated(Integer page, Integer size);
    ServiceResponse getAllErrorMessagesFromOneUser(String email);
}
