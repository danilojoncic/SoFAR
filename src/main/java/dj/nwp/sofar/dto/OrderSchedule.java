package dj.nwp.sofar.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record OrderSchedule(
        @NotNull
        String email,
        @NotNull
        List<String> dishes,
        @NotNull
        LocalDate scheduleDate
) {
}
