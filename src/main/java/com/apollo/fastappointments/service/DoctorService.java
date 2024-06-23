package com.apollo.fastappointments.service;

import com.apollo.fastappointments.model.Appointment;
import com.apollo.fastappointments.model.Doctor;
import com.apollo.fastappointments.repository.AppointmentRepository;
import com.apollo.fastappointments.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    

    public boolean authenticate(String username, String password) {
        Doctor doctor = doctorRepository.findByUsername(username);
        return doctor != null && doctor.getPassword().equals(password);
    }

    public List<Doctor> findBySpecialty(String specialty) {
        return doctorRepository.findBySpecialty(specialty);
    }

    public Doctor findByUsername(String username) {
        return doctorRepository.findByUsername(username);
    }

    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }

    public void addDoctor(String username, String password, String specialty, String name, String phone, String email) {
        Doctor doctor = new Doctor();
        doctor.setUsername(username);
        doctor.setPassword(password);
        doctor.setSpecialty(specialty);
        doctor.setName(name);
        doctor.setPhone(phone);
        doctor.setEmail(email);
        // Set other default fields or leave them for later update
        doctorRepository.save(doctor);
    }

    public void deleteDoctor(String username) {
    	Doctor doctor=doctorRepository.findByUsername(username);
        doctorRepository.deleteById(doctor.getDid());
//        Optional<Doctor> doctor=doctorRepository.findById(doctorId);
        List<Appointment> appointments = appointmentRepository.findByDoctorUsername(doctor.getUsername());
        appointmentRepository.deleteAll(appointments);
    }

    public List<String> findAllSpecialties() {
        // Retrieve all doctors from the database
        List<Doctor> doctors = doctorRepository.findAll();

        // Extract specialties from doctors and remove duplicates
        List<String> specialties = doctors.stream()
                .map(Doctor::getSpecialty)
                .distinct()
                .collect(Collectors.toList());

        return specialties;
    }

	public void save(Doctor doctor) {
		doctorRepository.save(doctor);
		
	}
}
