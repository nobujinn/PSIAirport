import PlaneManagement.Plane;

import java.time.LocalDateTime;

public record ParkingReservationResult(
        Plane plane,
        LocalDateTime dateFrom,
        LocalDateTime dateTo,
        int parkingId
) {
}
