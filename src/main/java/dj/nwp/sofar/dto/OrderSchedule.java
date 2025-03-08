package dj.nwp.sofar.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record OrderSchedule(
        @NotNull
        String email,
        @NotNull
        List<String> dishes,
        @NotNull
        LocalDateTime scheduleDateTime
) {
}
