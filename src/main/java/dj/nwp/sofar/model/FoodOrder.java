package dj.nwp.sofar.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class FoodOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Boolean active;

    @ManyToOne(fetch = FetchType.EAGER)
    private SUser createdBy;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "order_dishes",
            joinColumns = @JoinColumn(name = "food_order_id"),
            inverseJoinColumns = @JoinColumn(name = "dish_id")
    )
    private List<Dish> items = new ArrayList<>();




}

