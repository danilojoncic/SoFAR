package dj.nwp.sofar.service.abstraction;

import dj.nwp.sofar.dto.DishOperation;
import dj.nwp.sofar.dto.ServiceResponse;

public interface DishAbs {
    ServiceResponse getOneDish(Long id);
    ServiceResponse getAllDishes();
    ServiceResponse getAllDishesPaginated(Integer page,Integer size);
    ServiceResponse deleteDish(Long id);
    ServiceResponse createDish(DishOperation dto);
    ServiceResponse updateDish(Long id,DishOperation dto);

}
