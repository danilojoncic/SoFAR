package dj.nwp.sofar.repository;

import dj.nwp.sofar.model.ErrorMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ErrorMessageRepository extends JpaRepository<ErrorMessage, Long> {

    List<ErrorMessage> findErrorMessagesByFoodOrder_CreatedBy_Email(String email);
}
