package dj.nwp.sofar.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UserPresentation(
        @NotNull
        String firstName,

        @NotNull
        String lastName,

        @NotNull
        String email,

        @NotNull
        List<String> permissions
) {
}
