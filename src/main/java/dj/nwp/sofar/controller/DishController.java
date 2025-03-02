package dj.nwp.sofar.controller;

import dj.nwp.sofar.dto.DishOperation;
import dj.nwp.sofar.dto.ServiceResponse;
import dj.nwp.sofar.model.Dish;
import dj.nwp.sofar.service.DishService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dish")
@RequiredArgsConstructor
public class DishController {
    private final DishService dishService;

    @GetMapping
    public ResponseEntity<?> getAllDishes(){
        ServiceResponse sr = dishService.getAllDishes();
        return ResponseEntity.status(sr.code()).body(sr.content());
    }

    @GetMapping("/pg")
    public ResponseEntity<?> getAllDishesPaginated(@RequestParam Integer page,
                                                   @RequestParam Integer size){

        ServiceResponse sr = dishService.getAllDishesPaginated(page, size);
        return ResponseEntity.status(sr.code()).body(sr.content());
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getOneDish(@PathVariable Long id){
        ServiceResponse sr = dishService.getOneDish(id);
        return ResponseEntity.status(sr.code()).body(sr.content());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDish(@PathVariable Long id){
        ServiceResponse sr = dishService.deleteDish(id);
        return ResponseEntity.status(sr.code()).body(sr.content());
    }

    @PostMapping("/create")
    public ResponseEntity<?> createDish(@RequestBody @Valid DishOperation dto){
        ServiceResponse sr = dishService.createDish(dto);
        return ResponseEntity.status(sr.code()).body(sr.content());
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editDish(
            @PathVariable Long id,
            @RequestBody @Valid DishOperation dto){
        ServiceResponse sr = dishService.updateDish(id, dto);
        return ResponseEntity.status(sr.code()).body(sr.content());
    }
}
