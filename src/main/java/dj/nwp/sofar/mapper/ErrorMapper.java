package dj.nwp.sofar.mapper;

import dj.nwp.sofar.dto.ErrorOperation;
import dj.nwp.sofar.model.ErrorMessage;
import org.springframework.stereotype.Component;

@Component
public class ErrorMapper {
    public static ErrorOperation ErrorToErrorOperation(ErrorMessage errorMessage) {
         return new ErrorOperation(
                errorMessage.getId(),
                errorMessage.getFoodOrder().getId(),
                errorMessage.getFoodOrder().getCreatedBy().getEmail(),
                errorMessage.getDescription(),
                errorMessage.getOperation(),
                errorMessage.getTimestamp());
    }

}

