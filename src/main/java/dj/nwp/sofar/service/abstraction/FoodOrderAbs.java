package dj.nwp.sofar.service.abstraction;

import dj.nwp.sofar.dto.AuthComponents;
import dj.nwp.sofar.dto.OrderOperation;
import dj.nwp.sofar.dto.OrderSchedule;
import dj.nwp.sofar.dto.ServiceResponse;
import dj.nwp.sofar.model.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface FoodOrderAbs {

    /// TODO add search criteria
    ServiceResponse searchOrder(Long id, List<Status> status, LocalDateTime dateTo, LocalDateTime dateFrom, AuthComponents auth);
    /// TODO add creation dto
    ServiceResponse placeOrder(OrderOperation dto, AuthComponents auth);
    ServiceResponse cancelOrder(Long id, AuthComponents auth);
    /// TODO add creation dto
    ServiceResponse scheduleOrder(OrderSchedule dto,AuthComponents auth);
    ServiceResponse getAllOrders( AuthComponents auth);
    ServiceResponse trackPing(Long id);


}
