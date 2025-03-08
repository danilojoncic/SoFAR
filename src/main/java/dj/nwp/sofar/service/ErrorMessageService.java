package dj.nwp.sofar.service;

import dj.nwp.sofar.dto.AuthComponents;
import dj.nwp.sofar.dto.Message;
import dj.nwp.sofar.dto.ServiceResponse;
import dj.nwp.sofar.repository.ErrorMessageRepository;
import dj.nwp.sofar.repository.FoodOrderRepository;
import dj.nwp.sofar.repository.UserRepository;
import dj.nwp.sofar.service.abstraction.ErrorMessageAbs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ErrorMessageService implements ErrorMessageAbs {

    private final ErrorMessageRepository errorMessageRepository;
    private final UserRepository userRepository;

    @Override
    public ServiceResponse getAllErrorMessages() {
        return new ServiceResponse(200,errorMessageRepository.findAll());
    }

    @Override
    public ServiceResponse getAllErrorMessagesPaginated(Integer page, Integer size) {
        Pageable pg = PageRequest.of(page, size);
        return new ServiceResponse(200,errorMessageRepository.findAll(pg));
    }

    @Override
    public ServiceResponse getAllErrorMessagesFromOneUser(String email, AuthComponents auth) {
        if(auth.authorities().size() == 9){
            return new ServiceResponse(200,errorMessageRepository.findAll());
        }
        if(!userRepository.existsByEmail(email))return new ServiceResponse(404,new Message("User does not exist!"));
        return new ServiceResponse(200,errorMessageRepository.findErrorMessagesByFoodOrder_CreatedBy_Email(email));
    }
}
