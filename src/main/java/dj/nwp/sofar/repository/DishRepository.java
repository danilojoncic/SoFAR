package dj.nwp.sofar.repository;

import dj.nwp.sofar.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, Long> {
}
