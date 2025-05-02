package dj.nwp.sofar.controller;

import dj.nwp.sofar.dto.AuthComponents;
import dj.nwp.sofar.dto.OrderOperation;
import dj.nwp.sofar.dto.OrderSchedule;
import dj.nwp.sofar.dto.ServiceResponse;
import dj.nwp.sofar.model.Status;
import dj.nwp.sofar.service.FoodOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
        ServiceResponse sr = foodOrderService.scheduleOrder(dto,new AuthComponents(authentication.getName(), auths));
        return ResponseEntity.status(sr.code()).body(sr.content());
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchOrder(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) List<Status> status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTo,
            Authentication authentication) {
        if (authentication == null || authentication.getAuthorities() == null) {
            return ResponseEntity.status(401).body("Unauthorized: No authentication found");
        }
        List<String> auths = new ArrayList<>();
        authentication.getAuthorities().forEach(authority -> {
            auths.add(authority.toString());});
        ServiceResponse sr = foodOrderService.searchOrder(userId,status,dateFrom,dateTo,new AuthComponents(authentication.getName(), auths));
        return ResponseEntity.status(sr.code()).body(sr.content());
    }


    //for testing purposes only
    @GetMapping("/track/ping/{id}")
    public ResponseEntity<?> trackPing(@PathVariable Long id) {
        ServiceResponse sr = foodOrderService.trackPing(id);
        return ResponseEntity.status(sr.code()).body(sr.content());
    }




}
