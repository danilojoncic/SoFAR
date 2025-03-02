package dj.nwp.sofar.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String title;
    @Column(nullable = false)
    private String description;

    public Dish(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
