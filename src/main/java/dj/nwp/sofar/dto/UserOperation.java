package dj.nwp.sofar.dto;

import java.util.List;

public record UserOperation(
        String firstName,
        String lastName,
        String email,
        String password,
        List<String> permissions
) {
}
