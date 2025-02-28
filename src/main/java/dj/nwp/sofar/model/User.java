package dj.nwp.sofar.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Primary;

@Entity
@Data
public class User {
    @Id
    private Long id;
    public User() {

    }
}
