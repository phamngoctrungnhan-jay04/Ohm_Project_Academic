package org.ohm_project.repository.vehicle;

import org.ohm_project.entity.vehicle.VehicleReception;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleReceptionRepository extends JpaRepository<VehicleReception, Long> {
    Page<VehicleReception> findAllByCustomerId(Long customerId, Pageable pageable);
    Page<VehicleReception> findAllByLicensePlate(String licensePlate, Pageable pageable);
    Optional<VehicleReception> findByBookingId(Long bookingId);
}
