package dj.nwp.sofar.service.abstraction;

import dj.nwp.sofar.dto.ServiceResponse;

public interface FoodOrderAbs {

    /// TODO add search criteria
    ServiceResponse searchOrder();
    /// TODO add creation dto
    ServiceResponse placeOrder();
    ServiceResponse cancelOrder(Long id);
    ServiceResponse trackOrder(Long id);
    /// TODO add creation dto
    ServiceResponse scheduleOrder();
}
