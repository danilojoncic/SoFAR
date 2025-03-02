package dj.nwp.sofar.controller;

import dj.nwp.sofar.dto.RouteInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/routes")
public class RootController {
    @GetMapping
    public ResponseEntity<?> giveRootInfo(){
        List<RouteInfo> routes = new ArrayList<>();
        routes.add(new RouteInfo("GET", "/"));
        return ResponseEntity.ok(routes);
    }
}
