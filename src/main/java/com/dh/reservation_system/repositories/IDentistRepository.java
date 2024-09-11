package com.dh.reservation_system.repositories;

import com.dh.reservation_system.entities.Dentist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IDentistRepository extends JpaRepository<Dentist, Long> {
    Optional<Dentist> findByLicenseMedical(String licenseMedical);
}
