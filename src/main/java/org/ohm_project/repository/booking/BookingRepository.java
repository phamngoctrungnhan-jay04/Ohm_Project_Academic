package org.ohm_project.repository.booking;

import org.ohm_project.entity.booking.Booking;
import org.ohm_project.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findAllByCustomerId(Long customerId, Pageable pageable);
    Page<Booking> findAllByLicensePlate(String licensePlate, Pageable pageable);
    Page<Booking> findAllByStatus(BookingStatus status, Pageable pageable);
}
