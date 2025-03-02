package dj.nwp.sofar.service;

import dj.nwp.sofar.dto.DishOperation;
import dj.nwp.sofar.dto.ServiceResponse;
import dj.nwp.sofar.service.abstraction.DishAbs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DishService implements DishAbs {
    @Override
    public ServiceResponse getOneDish(Long id) {
        return null;
    }

    @Override
    public ServiceResponse getAllDishes() {
        return null;
    }

    @Override
    public ServiceResponse getAllDishesPaginated(Integer page, Integer size) {
        return null;
    }

    @Override
    public ServiceResponse deleteDish(Long id) {
        return null;
    }

    @Override
    public ServiceResponse createDish(DishOperation dto) {
        return null;
    }

    @Override
    public ServiceResponse updateDish(Long id, DishOperation dto) {
        return null;
    }
}
