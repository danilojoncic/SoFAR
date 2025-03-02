package dj.nwp.sofar.service.abstraction;

import dj.nwp.sofar.dto.OrderOperation;
import dj.nwp.sofar.dto.OrderSchedule;
import dj.nwp.sofar.dto.ServiceResponse;

import java.time.LocalDate;
import java.util.List;

public interface FoodOrderAbs {

    /// TODO add search criteria
    ServiceResponse searchOrder(Long id, List<String> status, LocalDate dateTo, LocalDate dateFrom);
    /// TODO add creation dto
    ServiceResponse placeOrder(OrderOperation dto);
    ServiceResponse cancelOrder(Long id);
    //uses websockets
    ServiceResponse trackOrder(Long id);
    /// TODO add creation dto
    ServiceResponse scheduleOrder(OrderSchedule dto);
}
