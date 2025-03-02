package dj.nwp.sofar.service;

import dj.nwp.sofar.dto.Message;
import dj.nwp.sofar.dto.OrderOperation;
import dj.nwp.sofar.dto.OrderSchedule;
import dj.nwp.sofar.dto.ServiceResponse;
import dj.nwp.sofar.model.Dish;
import dj.nwp.sofar.model.FoodOrder;
import dj.nwp.sofar.model.SUser;
import dj.nwp.sofar.model.Status;
import dj.nwp.sofar.repository.DishRepository;
import dj.nwp.sofar.repository.FoodOrderRepository;
import dj.nwp.sofar.repository.UserRepository;
import dj.nwp.sofar.service.abstraction.FoodOrderAbs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodOrderService implements FoodOrderAbs {
    FoodOrderRepository foodOrderRepository;
    UserRepository UserRepository;
    DishRepository dishRepository;


    @Override
    public ServiceResponse searchOrder(Long id, List<String> status, LocalDate dateTo, LocalDate dateFrom) {
        return null;
    }

    @Override
    public ServiceResponse placeOrder(OrderOperation dto) {
        if(!checkDishList(dto.dishes()))
            return new ServiceResponse(401,new Message("Invalid dish in order"));
        if(!checkEmail(dto.email()))
            return new ServiceResponse(401,new Message("Invalid user email"));
        SUser user = UserRepository.findByEmail(dto.email()).get();
        List<Dish> dishList = dishRepository.findByTitleIn(dto.dishes());
        FoodOrder foodOrder = new FoodOrder(
                Status.ORDERED,
                true,
                LocalDate.now(),
                user,
                dishList
        );
        foodOrderRepository.save(foodOrder);
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
    public ServiceResponse scheduleOrder(OrderSchedule dto) {
        return null;
    }


    private boolean checkDishList(List<String> dishes) {
        return dishRepository.countByTitleIn(dishes) == dishes.size();
    }


    private boolean checkEmail(String email){
        return UserRepository.existsByEmail(email);
    }

}
