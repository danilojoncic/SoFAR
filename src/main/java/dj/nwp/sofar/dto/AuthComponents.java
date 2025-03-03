package dj.nwp.sofar.dto;

import dj.nwp.sofar.model.Permission;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public record AuthComponents(
        String name,
        List<String> authorities
) {
}
