package dj.nwp.sofar.service;

import dj.nwp.sofar.dto.DishOperation;
import dj.nwp.sofar.dto.Message;
import dj.nwp.sofar.dto.ServiceResponse;
import dj.nwp.sofar.model.Dish;
import dj.nwp.sofar.repository.DishRepository;
import dj.nwp.sofar.repository.FoodOrderRepository;
import dj.nwp.sofar.service.abstraction.DishAbs;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DishService implements DishAbs {
    private final DishRepository dishRepository;
    private final FoodOrderRepository orderRepository;

    @Override
    public ServiceResponse getOneDish(Long id) {
        return dishRepository.findById(id)
                .map(dish -> new ServiceResponse(200,dish))
                .orElseGet(()-> new ServiceResponse(404,new Message("Dish not found")));
    }

    @Override
    public ServiceResponse getAllDishes() {
        return new ServiceResponse(200,dishRepository.findAll());
    }

    @Override
    public ServiceResponse getAllDishesPaginated(Integer page, Integer size) {
        Pageable pg = PageRequest.of(page, size);
        return new ServiceResponse(200,dishRepository.findAll(pg));
    }

    @Override
    @Transactional
    public ServiceResponse deleteDish(Long id) {
        return dishRepository.findById(id)
                .map(dish -> {

                    orderRepository.findFoodOrdersByItemsContaining(dish).forEach(order -> order.getItems().remove(dish));

                    dishRepository.deleteById(id);
                    return new ServiceResponse(201,new Message("Dish deleted"));
                })
                .orElseGet(() -> new ServiceResponse(404,new Message("Dish does not exist!")));
    }

    @Override
    @Transactional
    public ServiceResponse createDish(DishOperation dto) {
        return dishRepository.findByTitle(dto.title())
                .map(dish -> new ServiceResponse(401,new Message("Dish with the same name already exists!")))
                .orElseGet(()->{
                    Dish dish = new Dish(dto.title(), dto.description());
                    dishRepository.save(dish);
                    return new ServiceResponse(201,new Message("Dish created"));
                });
    }

    @Override
    @Transactional
    public ServiceResponse updateDish(Long id, DishOperation dto) {
        return dishRepository.findById(id)
                .map(dish -> {
                    if(!Objects.equals(dto.title(), ""))dish.setTitle(dto.title());
                    if(!Objects.equals(dto.description(), "")) dish.setDescription(dto.description());
                    dishRepository.save(dish);
                    return new ServiceResponse(201,new Message("Dish updated"));
                })
                .orElseGet(()-> new ServiceResponse(404,new Message("Dish does not exist!")));
    }
}
