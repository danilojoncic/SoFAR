package dj.nwp.sofar.dto;

import dj.nwp.sofar.model.Dish;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record OrderPresentation(
        Long id,
        String email,
        List<Dish> items,
        String orderStatus,
        LocalDateTime scheduledAt,
        boolean active
) {
}
