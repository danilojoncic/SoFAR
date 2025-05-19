package dj.nwp.sofar.dto;

import java.util.List;

public record RouteInfo(
        String method,
        String route,
        List<String> rolesRequired
) {
}
