package dj.nwp.sofar.dto;

public record LoginRequest(
        String email,
        String password
) {
}
