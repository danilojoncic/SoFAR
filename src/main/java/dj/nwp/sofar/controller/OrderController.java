package dj.nwp.sofar.controller;

import dj.nwp.sofar.dto.OrderOperation;
import dj.nwp.sofar.dto.ServiceResponse;
import dj.nwp.sofar.service.FoodOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final FoodOrderService foodOrderService;

    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(@RequestBody @Valid OrderOperation dto) {
        ServiceResponse sr = foodOrderService.placeOrder(dto);
        return ResponseEntity.status(sr.code()).body(sr.content());
    }
}
