package dj.nwp.sofar.repository;

import dj.nwp.sofar.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DishRepository extends JpaRepository<Dish, Long> {

    Optional<Dish> findByTitle(String title);
    boolean existsByTitle(String title);
    List<Dish> findByTitleIn(List<String> titles);
    int countByTitleIn(List<String> titles);
}
