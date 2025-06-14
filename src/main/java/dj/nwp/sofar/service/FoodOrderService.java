package dj.nwp.sofar.service;

import dj.nwp.sofar.config.JWTUtil;
import dj.nwp.sofar.dto.*;
import dj.nwp.sofar.mapper.OrderMapper;
import dj.nwp.sofar.model.*;
import dj.nwp.sofar.repository.DishRepository;
import dj.nwp.sofar.repository.ErrorMessageRepository;
import dj.nwp.sofar.repository.FoodOrderRepository;
import dj.nwp.sofar.repository.UserRepository;
import dj.nwp.sofar.service.abstraction.FoodOrderAbs;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Order;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodOrderService implements FoodOrderAbs {
    private final FoodOrderRepository foodOrderRepository;
    private final UserRepository userRepository;
    private final DishRepository dishRepository;
    private final ErrorMessageRepository errorMessageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final Logger logger = Logger.getLogger(FoodOrderService.class.getName());


    @Override
    public ServiceResponse searchOrder(Long userId, List<Status> status, LocalDateTime dateFrom, LocalDateTime dateTo, AuthComponents auth) {
        String email = auth.name();
        boolean isAdmin = auth.authorities().size() == 9;

        if (!userRepository.existsByEmail(email)) {
            return new ServiceResponse(401, new Message("User does not exist"));
        }

        List<FoodOrder> orders;

        if (!isAdmin) {
            // Regular user can only see their own orders
            if (dateFrom != null && dateTo != null && status != null) {
                orders = foodOrderRepository.findFoodOrdersByCreatedByIdAndStatusInAndScheduleDateTimeIsBetween(
                        userRepository.findByEmail(email).get().getId(), status, dateFrom, dateTo);
            } else if (dateFrom != null && dateTo != null) {
                orders = foodOrderRepository.findFoodOrdersByCreatedBy_IdAndScheduleDateTimeIsBetween(
                        userRepository.findByEmail(email).get().getId(), dateFrom, dateTo);
            } else if (status != null) {
                orders = foodOrderRepository.findFoodOrdersByCreatedBy_IdAndStatusIn(
                        userRepository.findByEmail(email).get().getId(), status);
            } else {
                orders = foodOrderRepository.findFoodOrdersByCreatedBy_Email(email);
            }
        } else {
            // Admin can filter by userId or see all orders
            if (userId != null) {
                if (dateFrom != null && dateTo != null && status != null) {
                    orders = foodOrderRepository.findFoodOrdersByCreatedByIdAndStatusInAndScheduleDateTimeIsBetween(
                            userId, status, dateFrom, dateTo);
                } else if (dateFrom != null && dateTo != null) {
                    orders = foodOrderRepository.findFoodOrdersByCreatedBy_IdAndScheduleDateTimeIsBetween(
                            userId, dateFrom, dateTo);
                } else if (status != null) {
                    orders = foodOrderRepository.findFoodOrdersByCreatedBy_IdAndStatusIn(userId, status);
                } else {
                    orders = foodOrderRepository.findFoodOrdersByCreatedBy_Id(userId);
                }
            } else {
                if (dateFrom != null && dateTo != null && status != null) {
                    orders = foodOrderRepository.findFoodOrdersByStatusInAndScheduleDateTimeIsBetween(status, dateFrom, dateTo);
                } else if (dateFrom != null && dateTo != null) {
                    orders = foodOrderRepository.findFoodOrdersByScheduleDateTimeIsBetween(dateFrom, dateTo);
                } else if (status != null) {
                    orders = foodOrderRepository.findFoodOrdersByStatusIn(status);
                } else {
                    orders = foodOrderRepository.findAll();
                }
            }
        }

        return new ServiceResponse(200, entityToPresentation(orders));
    }


    @Transactional
    public void updateOrderStatus(FoodOrder foodOrder,Status newStatus) {
        logger.info("UPDATE ORDER STATUS from " + foodOrder.getStatus().toString() + " to " + newStatus.toString());
        foodOrder.setStatus(newStatus);
        foodOrderRepository.save(foodOrder);
        messagingTemplate.convertAndSend("/tracker/order-status/" + foodOrder.getId(), OrderMapper.OrderToOrderPresentation(foodOrder));
    }




    private boolean canPlaceNewOrder() {
        long activeOrders = foodOrderRepository.countFoodOrderByStatusIn(List.of(Status.PREPARING, Status.IN_DELIVERY,Status.ORDERED));
        return activeOrders < 3;
    }

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void processOrders() {
        logger.info("Processing orders");
        if(foodOrderRepository.count() == 0)return;
        List<FoodOrder> orders = foodOrderRepository.findFoodOrdersByStatusIn(List.of(Status.ORDERED, Status.PREPARING, Status.IN_DELIVERY,Status.SCHEDULED));
        LocalDateTime now = LocalDateTime.now();

        orders.forEach(order -> {
            switch (order.getStatus()) {
                case ORDERED -> {
                    //10 seconds after pressing order
                    //10 + now = now + 10
                    if (order.getScheduleDateTime().plusSeconds(10).isBefore(now)) {
                        updateOrderStatus(order,Status.PREPARING);
                    }
                }
                case PREPARING -> {
                    //15 seconds after getting into prepareing stage
                    //10 + now + 15 = now + 25
                    if (order.getScheduleDateTime().plusSeconds(25).isBefore(now)) {
                        updateOrderStatus(order,Status.IN_DELIVERY);
                    }
                }
                case IN_DELIVERY -> {
                    //20 seconds after getting into in delivery stage
                    //10 + now + 15 + 20 = now + 45
                    if (order.getScheduleDateTime().plusSeconds(45).isBefore(now)) {
                        updateOrderStatus(order,Status.DELIVERED);
                    }
                }
                //go in a cercle :) (yes cercle...)
                case SCHEDULED -> {
                    if(order.getScheduleDateTime().isBefore(now)){
                        order.setActive(true);
                        updateOrderStatus(order,Status.ORDERED);
                    }
                }
            }
        });
        foodOrderRepository.saveAll(orders);
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
        List<Dish> dishes = dto.dishes().stream()
                .map(dishRepository::findByTitle) 
                .flatMap(Optional::stream)
                .collect(Collectors.toList());

        FoodOrder foodOrder = new FoodOrder(
                Status.ORDERED,
                true,
                LocalDateTime.now(),
                user,
                dishes
        );

        if(!canPlaceNewOrder()){
            foodOrder.setStatus(Status.CANCELED);
            foodOrderRepository.save(foodOrder);
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setFoodOrder(foodOrder);
            errorMessage.setDescription("Can't place new order");
            errorMessage.setOperation("You tried to have more than 3 orders in the delivery stage at the same time!");
            errorMessage.setTimestamp(LocalDateTime.now());
            errorMessageRepository.save(errorMessage);
            return new ServiceResponse(401,new Message("Order cannot be placed"));
        }



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

        updateOrderStatus(order,Status.CANCELED);
        return new ServiceResponse(201, new Message("Order successfully canceled"));
    }


    @Override
    @Transactional
    public ServiceResponse scheduleOrder(OrderSchedule dto, AuthComponents auth) {
        String email = auth.name();
        if(!userRepository.existsByEmail(email)) return new ServiceResponse(404,new Message("User does not exist!"));
        SUser user = userRepository.findByEmail(email).get();

        if(LocalDateTime.now().isAfter(dto.scheduleDateTime())){
            logger.info("SCHEDULE ORDER IS SCHEDULED FOR TIME THAT ALREADY PASSED!");
            return new ServiceResponse(401,new Message("Order cannot be scheduled,due to time issues!"));

        }


        FoodOrder foodOrder = new FoodOrder(Status.SCHEDULED,false,dto.scheduleDateTime(),user,dishRepository.findByTitleIn(dto.dishes()));
        if(foodOrderRepository.countFoodOrderByStatus(Status.SCHEDULED) > 3){
            ErrorMessage errorMessage = new ErrorMessage();

            foodOrder.setStatus(Status.CANCELED);

            errorMessage.setFoodOrder(foodOrder);
            errorMessage.setOperation("SCHEDULING ERROR");
            errorMessage.setDescription("TOO MANY SCHEDULED ORDERS AT THIS TIME");
            errorMessage.setTimestamp(LocalDateTime.now());
            foodOrderRepository.save(foodOrder);
            errorMessageRepository.save(errorMessage);
            return new ServiceResponse(401, new Message("Scheduling ERROR, too many scheduled orders"));
        }
        foodOrderRepository.save(foodOrder);
        return new ServiceResponse(201,new Message("Order has been scheduled at: " + dto.scheduleDateTime()));
    }

    @Override
    @Transactional
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
                    entity.getCreatedBy() == null ? "Deleted User" : entity.getCreatedBy().getEmail(),
                    entity.getItems(),
                    entity.getStatus().name(),
                    entity.getScheduleDateTime(),
                    entity.getActive()
            ));
        });
        return presentationList;
    }


    private boolean checkDishList(List<String> dishes) {
        return dishes.stream().allMatch(dishRepository::existsByTitle);
    }


    private boolean checkEmail(String email){
        return userRepository.existsByEmail(email);
    }

    @Override
    public ServiceResponse trackPing(Long id){
        if(foodOrderRepository.findById(id).isEmpty()){
            return new ServiceResponse(404,new Message("Order does not exist"));
        }
        FoodOrder foodOrder = foodOrderRepository.findById(id).get();
        String status = foodOrder.getStatus().name();
        return new ServiceResponse(200, new Message(status));
    }


}
