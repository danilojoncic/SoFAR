package dj.nwp.sofar.dto;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull
        String email,
        @NotNull
        String password
) {
}
