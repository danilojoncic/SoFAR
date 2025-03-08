package dj.nwp.sofar.service;

import dj.nwp.sofar.config.JWTUtil;
import dj.nwp.sofar.dto.*;
import dj.nwp.sofar.model.Dish;
import dj.nwp.sofar.model.FoodOrder;
import dj.nwp.sofar.model.SUser;
import dj.nwp.sofar.model.Status;
import dj.nwp.sofar.repository.DishRepository;
import dj.nwp.sofar.repository.ErrorMessageRepository;
import dj.nwp.sofar.repository.FoodOrderRepository;
import dj.nwp.sofar.repository.UserRepository;
import dj.nwp.sofar.service.abstraction.FoodOrderAbs;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Order;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodOrderService implements FoodOrderAbs {
    private final FoodOrderRepository foodOrderRepository;
    private final UserRepository userRepository;
    private final DishRepository dishRepository;
    private final ErrorMessageRepository errorMessageRepository;


    @Override
    public ServiceResponse searchOrder(Long id, List<String> status, LocalDate dateTo, LocalDate dateFrom, AuthComponents auth) {
        return null;
    }

    @Override
    @Transactional
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
                LocalDateTime.now(),
                user,
                dishList
        );
        foodOrderRepository.save(foodOrder);
        return new ServiceResponse(200,new Message("Order Placed"));
    }

    @Override
    @Transactional
    public ServiceResponse cancelOrder(Long id, AuthComponents auth) {
        FoodOrder order = foodOrderRepository.findById(id).orElse(null);
        if (order == null) {
            return new ServiceResponse(404, new Message("Order does not exist"));
        }

        SUser user = userRepository.findByEmail(auth.name()).orElse(null);
        if (user == null) {
            return new ServiceResponse(401, new Message("User does not exist"));
        }

        boolean isAdmin = auth.authorities().size() == 9;

        if (!isAdmin && !order.getCreatedBy().getEmail().equals(auth.name())) {
            return new ServiceResponse(403, new Message("You cannot cancel someone else's order"));
        }

        if (order.getStatus() != Status.ORDERED) {
            return new ServiceResponse(400, new Message("Only orders with status ORDERED can be canceled"));
        }

        order.setStatus(Status.CANCELED);
        foodOrderRepository.save(order);
        return new ServiceResponse(201, new Message("Order successfully canceled"));
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
                    entity.getScheduleDateTime(),
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
