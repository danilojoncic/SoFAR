package dj.nwp.sofar.service.abstraction;

import dj.nwp.sofar.dto.AuthComponents;
import dj.nwp.sofar.dto.OrderOperation;
import dj.nwp.sofar.dto.OrderSchedule;
import dj.nwp.sofar.dto.ServiceResponse;

import java.time.LocalDate;
import java.util.List;

public interface FoodOrderAbs {

    /// TODO add search criteria
    ServiceResponse searchOrder(Long id, List<String> status, LocalDate dateTo, LocalDate dateFrom, AuthComponents auth);
    /// TODO add creation dto
    ServiceResponse placeOrder(OrderOperation dto, AuthComponents auth);
    ServiceResponse cancelOrder(Long id, AuthComponents auth);
    //uses websockets
    ServiceResponse trackOrder(Long id, AuthComponents auth);
    /// TODO add creation dto
    ServiceResponse scheduleOrder(OrderSchedule dto);
    ServiceResponse getAllOrders( AuthComponents auth);


}
