package dj.nwp.sofar.repository;

import dj.nwp.sofar.model.FoodOrder;
import dj.nwp.sofar.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodOrderRepository extends JpaRepository<FoodOrder, Long> {
    List<FoodOrder> findFoodOrdersByCreatedBy_Email(String email);
    int countFoodOrderByStatus(Status status);
}
