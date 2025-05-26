package dj.nwp.sofar.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    private LocalDateTime scheduleDateTime;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.DETACH)
    private SUser createdBy;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "order_dishes",
            joinColumns = @JoinColumn(name = "food_order_id"),
            inverseJoinColumns = @JoinColumn(name = "dish_id")
    )
    private List<Dish> items = new ArrayList<>();

    public FoodOrder(Status status, Boolean active, LocalDateTime scheduleDateTime, SUser createdBy, List<Dish> items) {
        this.status = status;
        this.active = active;
        this.scheduleDateTime = scheduleDateTime;
        this.createdBy = createdBy;
        this.items = items;
    }
}

