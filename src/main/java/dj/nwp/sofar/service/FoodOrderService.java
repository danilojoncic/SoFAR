package dj.nwp.sofar.service;

import dj.nwp.sofar.config.JWTUtil;
import dj.nwp.sofar.dto.*;
import dj.nwp.sofar.model.Dish;
import dj.nwp.sofar.model.FoodOrder;
import dj.nwp.sofar.model.SUser;
import dj.nwp.sofar.model.Status;
import dj.nwp.sofar.repository.DishRepository;
import dj.nwp.sofar.repository.FoodOrderRepository;
import dj.nwp.sofar.repository.UserRepository;
import dj.nwp.sofar.service.abstraction.FoodOrderAbs;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Order;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodOrderService implements FoodOrderAbs {
    private final FoodOrderRepository foodOrderRepository;
    private final UserRepository userRepository;
    private final DishRepository dishRepository;


    @Override
    public ServiceResponse searchOrder(Long id, List<String> status, LocalDate dateTo, LocalDate dateFrom, AuthComponents auth) {
        return null;
    }

    @Override
    public ServiceResponse placeOrder(OrderOperation dto,AuthComponents auth) {
        String email = auth.name();
        if(!checkDishList(dto.dishes()))
            return new ServiceResponse(401,new Message("Invalid dish in order"));
        if(!checkEmail(email))
            return new ServiceResponse(401,new Message("Invalid user email"));
        SUser user = userRepository.findByEmail(email).get();
        List<Dish> dishList = dishRepository.findByTitleIn(dto.dishes());
        FoodOrder foodOrder = new FoodOrder(
                Status.ORDERED,
                true,
                LocalDate.now(),
                user,
                dishList
        );
        foodOrderRepository.save(foodOrder);
        return new ServiceResponse(200,new Message("Order Placed"));
    }

    @Override
    public ServiceResponse cancelOrder(Long id, AuthComponents auth) {

        return null;
    }

    @Override
    public ServiceResponse trackOrder(Long id, AuthComponents auth) {
        return null;
    }

    @Override
    public ServiceResponse scheduleOrder(OrderSchedule dto) {
        return null;
    }

    @Override
    public ServiceResponse getAllOrders(AuthComponents auth) {
        List<String> perms = auth.authorities();
        String email = auth.name();

        if(!userRepository.existsByEmail(email))return new ServiceResponse(401,new Message("User does not exist"));
        //an admin is a user that has all 9 permissions ;)
        if(perms.size() != 9){
            return new ServiceResponse(200,entityToPresentation(foodOrderRepository.findFoodOrdersByCreatedBy_Email(email)));
        }else{
            return new ServiceResponse(200,entityToPresentation(foodOrderRepository.findAll()));
        }
    }

    private List<OrderPresentation> entityToPresentation(List<FoodOrder> entityList){
        List<OrderPresentation> presentationList = new ArrayList<>();
        entityList.forEach(entity -> {
            presentationList.add(new OrderPresentation(
                    entity.getId(),
                    entity.getCreatedBy().getEmail(),
                    entity.getItems(),
                    entity.getStatus().name(),
                    entity.getScheduleDate(),
                    entity.getActive()
            ));
        });
        return presentationList;
    }


    private boolean checkDishList(List<String> dishes) {
        return dishRepository.countByTitleIn(dishes) == dishes.size();
    }


    private boolean checkEmail(String email){
        return userRepository.existsByEmail(email);
    }

}
