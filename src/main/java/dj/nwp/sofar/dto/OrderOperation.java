package dj.nwp.sofar.dto;

import dj.nwp.sofar.model.Status;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderOperation(
        @NotNull
        List<String> dishes
) {
}
