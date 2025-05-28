package dj.nwp.sofar.mapper;

import dj.nwp.sofar.dto.OrderPresentation;
import dj.nwp.sofar.model.FoodOrder;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public static OrderPresentation OrderToOrderPresentation(FoodOrder foodOrder) {
        return new OrderPresentation(
                foodOrder.getId(),
                foodOrder.getCreatedBy().getEmail(),
                foodOrder.getItems(),
                foodOrder.getStatus().name(),
                foodOrder.getScheduleDateTime(),
                foodOrder.getActive());
    }
}
