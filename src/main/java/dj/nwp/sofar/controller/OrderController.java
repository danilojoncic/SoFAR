package dj.nwp.sofar.controller;

import dj.nwp.sofar.dto.AuthComponents;
import dj.nwp.sofar.dto.OrderOperation;
import dj.nwp.sofar.dto.OrderSchedule;
import dj.nwp.sofar.dto.ServiceResponse;
import dj.nwp.sofar.service.FoodOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final FoodOrderService foodOrderService;

    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(@RequestBody @Valid OrderOperation dto, Authentication authentication) {
        if (authentication == null || authentication.getAuthorities() == null) {
            return ResponseEntity.status(401).body("Unauthorized: No authentication found");
        }
        List<String> auths = new ArrayList<>();
        authentication.getAuthorities().forEach(authority -> {
            auths.add(authority.toString());});
        ServiceResponse sr = foodOrderService.placeOrder(dto,new AuthComponents(authentication.getName(), auths));
        return ResponseEntity.status(sr.code()).body(sr.content());
    }


    @GetMapping
    public ResponseEntity<?> getOrdersBasedOnAuth(Authentication authentication) {
        if (authentication == null || authentication.getAuthorities() == null) {
            return ResponseEntity.status(401).body("Unauthorized: No authentication found");
        }
        List<String> auths = new ArrayList<>();
        authentication.getAuthorities().forEach(authority -> {
            auths.add(authority.toString());});
        ServiceResponse sr = foodOrderService.getAllOrders(new AuthComponents(authentication.getName(), auths));
        return ResponseEntity.status(sr.code()).body(sr.content());
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id, Authentication authentication) {
        if (authentication == null || authentication.getAuthorities() == null) {
            return ResponseEntity.status(401).body("Unauthorized: No authentication found");
        }
        List<String> auths = new ArrayList<>();
        authentication.getAuthorities().forEach(authority -> {
            auths.add(authority.toString());});
        ServiceResponse sr = foodOrderService.cancelOrder(id,new AuthComponents(authentication.getName(), auths));
        return ResponseEntity.status(sr.code()).body(sr.content());
    }


    @PostMapping("/schedule")
    public ResponseEntity<?> scheduleOrder(@RequestBody @Valid OrderSchedule dto, Authentication authentication) {
        if (authentication == null || authentication.getAuthorities() == null) {
            return ResponseEntity.status(401).body("Unauthorized: No authentication found");
        }
        List<String> auths = new ArrayList<>();
        authentication.getAuthorities().forEach(authority -> {
            auths.add(authority.toString());});
        ServiceResponse sr = foodOrderService.placeOrder(dto,new AuthComponents(authentication.getName(), auths));
        return ResponseEntity.status(sr.code()).body(sr.content());
    }





}
