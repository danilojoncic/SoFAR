package dj.nwp.sofar.dto;

import jakarta.validation.constraints.NotNull;

public record DishOperation(
        @NotNull
        String title,
        @NotNull
        String description
) {
}
