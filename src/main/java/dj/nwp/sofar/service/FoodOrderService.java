package dj.nwp.sofar.service;

import dj.nwp.sofar.dto.ServiceResponse;
import dj.nwp.sofar.service.abstraction.FoodOrderAbs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FoodOrderService implements FoodOrderAbs {
    @Override
    public ServiceResponse searchOrder() {
        return null;
    }

    @Override
    public ServiceResponse placeOrder() {
        return null;
    }

    @Override
    public ServiceResponse cancelOrder(Long id) {
        return null;
    }

    @Override
    public ServiceResponse trackOrder(Long id) {
        return null;
    }

    @Override
    public ServiceResponse scheduleOrder() {
        return null;
    }
}
