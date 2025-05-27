package dj.nwp.sofar.service;

import dj.nwp.sofar.dto.AuthComponents;
import dj.nwp.sofar.dto.ErrorOperation;
import dj.nwp.sofar.dto.Message;
import dj.nwp.sofar.dto.ServiceResponse;
import dj.nwp.sofar.mapper.ErrorMapper;
import dj.nwp.sofar.model.ErrorMessage;
import dj.nwp.sofar.repository.ErrorMessageRepository;
import dj.nwp.sofar.repository.FoodOrderRepository;
import dj.nwp.sofar.repository.UserRepository;
import dj.nwp.sofar.service.abstraction.ErrorMessageAbs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ErrorMessageService implements ErrorMessageAbs {

    private final ErrorMessageRepository errorMessageRepository;
    private final UserRepository userRepository;

    @Override
    public ServiceResponse getAllErrorMessages() {
        return new ServiceResponse(200,errorMessageRepository.findAll().stream().map(ErrorMapper::ErrorToErrorOperation).collect(Collectors.toList()));
    }

    @Override
    public ServiceResponse getAllErrorMessagesPaginated(Integer page, Integer size) {
        Pageable pg = PageRequest.of(page, size);
        Page<ErrorMessage> msgs = errorMessageRepository.findAll(pg);
        Page<ErrorOperation> results = msgs.map(ErrorMapper::ErrorToErrorOperation);
        return new ServiceResponse(200,results);
    }

    @Override
    public ServiceResponse getAllErrorMessagesFromOneUser(String email, AuthComponents auth) {
        if (auth.authorities().size() == 9) {
            List<ErrorOperation> allErrors = errorMessageRepository.findAll().stream()
                    .map(ErrorMapper::ErrorToErrorOperation)
                    .collect(Collectors.toList());
            return new ServiceResponse(200, allErrors);
        }

        if (!userRepository.existsByEmail(email)) {
            return new ServiceResponse(404, new Message("User does not exist!"));
        }

        List<ErrorOperation> userErrors = errorMessageRepository
                .findErrorMessagesByFoodOrder_CreatedBy_Email(email).stream()
                .map(ErrorMapper::ErrorToErrorOperation)
                .collect(Collectors.toList());

        return new ServiceResponse(200, userErrors);
    }

}
