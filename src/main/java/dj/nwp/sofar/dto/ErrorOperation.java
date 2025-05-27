package dj.nwp.sofar.dto;


import java.time.LocalDateTime;

public record ErrorOperation(
        Long errorId,
        Long orderId,
        String emailOfCreator,
        String description,
        String operation,
        LocalDateTime timestamp
) {}

