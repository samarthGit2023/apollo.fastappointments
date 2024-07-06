package com.apollo.fastappointments.service;

import com.apollo.fastappointments.model.Appointment;
import com.apollo.fastappointments.model.Doctor;
import com.apollo.fastappointments.model.Patient;
import com.apollo.fastappointments.repository.AppointmentRepository;
import com.apollo.fastappointments.repository.DoctorRepository;
import com.apollo.fastappointments.repository.PatientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired 
    private PatientRepository patientRepository;
    
    @Autowired 
    private DoctorRepository doctorRepository;
    

    public List<Appointment> getPatientAppointments(String patientUsername) {
        // Retrieve appointments for a specific patient
        return appointmentRepository.findByPatientUsername(patientUsername);
    }

    public List<Appointment> getDoctorAppointments(String doctorUsername) {
        // Retrieve appointments for a specific doctor
        return appointmentRepository.findByDoctorUsername(doctorUsername);
    }

    //public void createAppointment(String patientUsername, Long doctorId) {
    public void createAppointment(String patientUsername, String doctorUsername) {
        // Logic to create a new appointment for a patient with the given doctor ID
        // Step 1: Retrieve the patient and doctor entities from their respective repositories
        Patient patient = patientRepository.findByUsername(patientUsername);
        //Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        Doctor doctor = doctorRepository.findByUsername(doctorUsername);
        
        
        
        if (patient != null && doctor != null) {
            // Step 3: Find the maximum appointment time for the doctor
            Optional<Integer> maxAppointmentTimeOpt = appointmentRepository.findMaxAppointmentTimeByDoctorUsername(doctorUsername);

            // Step 4: Determine the new appointment time
            int newAppointmentTime;
            if (maxAppointmentTimeOpt.isPresent()) {
                newAppointmentTime = maxAppointmentTimeOpt.get() + 1;
            } else {
                // Generate a random time within the range of 5000
                Random random = new Random();
                newAppointmentTime = random.nextInt(5000);
            }

            // Step 5: Create a new Appointment object
            Appointment appointment = new Appointment();
            appointment.setPatientUsername(patient.getUsername());
            appointment.setDoctorUsername(doctor.getUsername());
            appointment.setStatus("pending"); // Default status
            appointment.setAppointmentTime(newAppointmentTime);

            // Step 6: Save the appointment using AppointmentRepository
            appointmentRepository.save(appointment);
        } else {
            // Handle the case where either the patient or the doctor does not exist
            // throw an exception or log an error message
        }
    }


    public void cancelAppointment(Long appointmentId) {
    	// Find the appointment by its ID
        Appointment appointment = appointmentRepository.findById(appointmentId)
                                                        .orElseThrow(() -> new IllegalArgumentException("Invalid appointment ID"));

        // Delete the appointment from the database
        appointmentRepository.delete(appointment);

    }

    public List<Appointment> findByPatientUsername(String username) {
        return appointmentRepository.findByPatientUsername(username);
    }

	public List<Appointment> findByDoctorUsername(String username) {
		return appointmentRepository.findByDoctorUsername(username);
	}
}
