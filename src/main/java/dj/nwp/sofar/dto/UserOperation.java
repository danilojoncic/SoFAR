package dj.nwp.sofar.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.List;

//dto object used for creating and editing of a User
public record UserOperation(
        @NotNull
        String firstName,

        @NotNull
        String lastName,

        @NotNull
        @Email
        String email,

        @NotNull
        String password,

        @NotNull
        List<String> permissions
) {
}
