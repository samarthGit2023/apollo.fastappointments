package com.apollo.fastappointments.repository;

import com.apollo.fastappointments.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientUsername(String patientUsername);
    List<Appointment> findByDoctorUsername(String doctorUsername);
    
    @Query("SELECT MAX(a.appointmentTime) FROM Appointment a WHERE a.doctorUsername = :doctorUsername")
    Optional<Integer> findMaxAppointmentTimeByDoctorUsername(@Param("doctorUsername") String doctorUsername);
}
