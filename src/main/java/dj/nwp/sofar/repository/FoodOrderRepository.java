package dj.nwp.sofar.repository;

import dj.nwp.sofar.model.FoodOrder;
import dj.nwp.sofar.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface FoodOrderRepository extends JpaRepository<FoodOrder, Long> {
    List<FoodOrder> findFoodOrdersByCreatedBy_Email(String email);
    int countFoodOrderByStatus(Status status);
    int countFoodOrderByStatusIn(List<Status> statuses);
    List<FoodOrder> findFoodOrdersByCreatedBy_Id(Long id);
    List<FoodOrder> findFoodOrdersByCreatedBy_IdAndStatusIn(Long id, List<Status> status);
    List<FoodOrder> findFoodOrdersByCreatedBy_IdAndScheduleDateTimeIsBetween(Long id, LocalDateTime dateFrom, LocalDateTime dateTo);
    List<FoodOrder> findFoodOrdersByCreatedByIdAndStatusInAndScheduleDateTimeIsBetween(Long id, List<Status> status, LocalDateTime dateFrom, LocalDateTime dateTo);
    List<FoodOrder> findFoodOrdersByStatusIn(List<Status> status);
    List<FoodOrder> findFoodOrdersByStatusInAndScheduleDateTimeIsBetween(List<Status> status, LocalDateTime dateFrom, LocalDateTime dateTo);
    List<FoodOrder> findFoodOrdersByScheduleDateTimeIsBetween(LocalDateTime dateFrom, LocalDateTime dateTo);
}
