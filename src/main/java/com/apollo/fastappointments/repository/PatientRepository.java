package com.apollo.fastappointments.repository;

import com.apollo.fastappointments.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Patient findByUsername(String username);
}

