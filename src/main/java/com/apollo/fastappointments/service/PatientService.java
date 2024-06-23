package com.apollo.fastappointments.service;

import com.apollo.fastappointments.model.Appointment;
import com.apollo.fastappointments.model.Doctor;
import com.apollo.fastappointments.model.Patient;
import com.apollo.fastappointments.repository.AppointmentRepository;
import com.apollo.fastappointments.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private AppointmentRepository appointmentRepository;

    public boolean authenticate(String username, String password) {
        Patient patient = patientRepository.findByUsername(username);
        return patient != null && patient.getPassword().equals(password);
    }

    public boolean register(String username, String password, String name, String phone, String email) {
        if (patientRepository.findByUsername(username) != null) {
            return false; // Username already exists
        }
        Patient patient = new Patient();
        patient.setUsername(username);
        patient.setPassword(password);
        patient.setName(name);
        patient.setPhone(phone);
        patient.setEmail(email);
        // Set other default fields or leave them for later update
        patientRepository.save(patient);
        return true;
    }

    public Patient findByUsername(String username) {
        return patientRepository.findByUsername(username);
    }

    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

	public void save(Patient patient) {
		patientRepository.save(patient);
		
	}
	
	

	public void deletePatient(String username) {
		Patient patient=patientRepository.findByUsername(username);
		patientRepository.deleteById(patient.getPid());
		List<Appointment> appointments=appointmentRepository.findByPatientUsername(patient.getUsername());
		appointmentRepository.deleteAll(appointments);
		
	}
}
