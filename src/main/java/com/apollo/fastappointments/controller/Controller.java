package com.apollo.fastappointments.controller;


import com.apollo.fastappointments.model.Appointment;
import com.apollo.fastappointments.model.Doctor;
import com.apollo.fastappointments.model.Patient;
import com.apollo.fastappointments.service.AppointmentService;
import com.apollo.fastappointments.service.DoctorService;
import com.apollo.fastappointments.service.PatientService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@org.springframework.stereotype.Controller
public class Controller {
	
	HashMap<String, String> patientMap=new HashMap<String, String>();
	HashMap<String, String> doctorMap=new HashMap<String, String>();
	private String adminpass1="adminpass";

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AppointmentService appointmentService;
    
    public String getAdminPassword() {
    	return adminpass1;
    }

    // Landing page with login options
    @GetMapping("/")
    public String home() {
        return "home"; // returns home.html
    }

    // Patient Login
    @GetMapping("/patient/login")
    public String patientLogin() {
        return "patientLogin"; // returns patientLogin.html
    }

    @PostMapping("/patient/login")
    public String patientLogin(@RequestParam String username, @RequestParam String password, Model model) {
        if (patientService.authenticate(username, password)) {
            model.addAttribute("username", username);
            return "redirect:/patient/profile?username="+username+"&password="+password;
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "patientLogin";
        }
    }

    // Patient Registration
    @GetMapping("/patient/register")
    public String patientRegister() {
        return "patientRegister"; // returns patientRegister.html
    }

    @PostMapping("/patient/register")
    public String patientRegister(@RequestParam String username, @RequestParam String password, @RequestParam String confirmPassword, @RequestParam String name, @RequestParam String phone, @RequestParam String email ,Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "patientRegister";
        }
        if (patientService.register(username, password, name, phone, email)) {
            return "redirect:/patient/login";
        } else {
            model.addAttribute("error", "Registration failed");
            return "patientRegister";
        }
    }

    // Patient Profile
    @GetMapping("/patient/profile")
    public String patientProfile(Model model, @RequestParam String username, @RequestParam String password) {
        Patient patient = patientService.findByUsername(username);
        if(!(password.equals(patient.getPassword()))) {
        	return "errorThrow";
        }
        model.addAttribute("patient", patient);
        return "patientProfile"; // returns patientProfile.html
    }
    
    @GetMapping("/patient/logout")
    public String patientLogout() {
    	return "redirect:/";
    }
    
    @GetMapping("/patient/update")
    public String showUpdateForm(Model model, @RequestParam String username, @RequestParam String password) {
        Patient patient = patientService.findByUsername(username);
        if(!(password.equals(patient.getPassword()))) {
        	return "errorThrow";
        }
        model.addAttribute("patient", patient);
        return "patientUpdateForm"; // return the HTML page for the update form
    }

    // POST request to handle form submission and update patient information
    @PostMapping("/patient/update")
    public String patientUpdate(@RequestParam String username, @RequestParam String name, 
                                @RequestParam String email, @RequestParam String phone) {
        // Retrieve the patient from the database
        Patient patient = patientService.findByUsername(username);
        
        // Update the patient information
        patient.setName(name);
        patient.setEmail(email);
        patient.setPhone(phone);
        
        // Save the updated patient information in the database
        patientService.save(patient);
        String password=patient.getPassword();
        
        // Redirect to the patient profile page
        return "redirect:/patient/profile?username=" + username+"&password="+password;
    }
    // My Appointments
    @GetMapping("/patient/appointments")
    public String patientAppointments(Model model, @RequestParam String username, @RequestParam String password) {
//    	if(!(password.equals(patientMap.get(username)))) {
//    		return "errorThrow";
//    	}
    	Patient patient = patientService.findByUsername(username);
    	if(!(password.equals(patient.getPassword()))) {
        	return "errorThrow";
        }
        model.addAttribute("patient", patient);
        List<Appointment> list1=appointmentService.findByPatientUsername(username);
        List<CompleteAppointment> list2=new ArrayList<>();
        for (Appointment ap : list1) {
            Doctor dc = doctorService.findByUsername(ap.getDoctorUsername());
            CompleteAppointment ca = new CompleteAppointment(ap, dc);
            list2.add(ca);
        }
        model.addAttribute("appointments", list2);
        return "patientAppointments"; // returns patientAppointments.html
    }
    
    @GetMapping("/patient/ffghhh")
    public String patientffghhh(Model model, @RequestParam String username, @RequestParam String password) {
//    	if(!(password.equals(patientMap.get(username)))) {
//    		return "errorThrow";
//    	}
    	return "redirect:/patient/profile?username=" + username+"&password="+password;
    }

    // Create Appointment
    @GetMapping("/patient/appointment/create")
    public String createAppointment(Model model, @RequestParam String username, @RequestParam String password) {
    	Patient patient = patientService.findByUsername(username);
    	if(!(password.equals(patient.getPassword()))) {
        	return "errorThrow";
        }
        model.addAttribute("patient", patient);
        model.addAttribute("specialties", doctorService.findAllSpecialties());
        return "createAppointment"; // returns createAppointment.html
    }

    @GetMapping("/patient/appointment/doctors")
    public String listDoctors(@RequestParam String specialty, Model model, @RequestParam String username, @RequestParam String password) {
    	Patient patient = patientService.findByUsername(username);
    	if(!(password.equals(patient.getPassword()))) {
        	return "errorThrow";
        }
        model.addAttribute("patient", patient);
        model.addAttribute("doctors", doctorService.findBySpecialty(specialty));
        return "listDoctors"; // returns listDoctors.html
    }

    @GetMapping("/patient/appointment/set")
    public String setAppointment(@RequestParam String doctorUsername, @RequestParam String patientUsername, @RequestParam String patientPassword,Model model) {

        appointmentService.createAppointment(patientUsername, doctorUsername);
        return "redirect:/patient/appointments?username="+patientUsername+"&password="+patientPassword;
    }

    // Doctor Login
    @GetMapping("/doctor/login")
    public String doctorLogin() {
        return "doctorLogin"; // returns doctorLogin.html
    }

    @PostMapping("/doctor/login")
    public String doctorLogin(@RequestParam String username, @RequestParam String password, Model model) {
        if (doctorService.authenticate(username, password)) {
            model.addAttribute("username", username);
            return "redirect:/doctor/profile?username="+username+"&password="+password;
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "doctorLogin";
        }
    }
    
    @GetMapping("/doctor/logout")
    public String doctorLogout() {
    	return "redirect:/";
    }

    // Doctor Profile
    @GetMapping("/doctor/profile")
    public String doctorProfile(Model model, @RequestParam String username, @RequestParam String password) {
        Doctor doctor = doctorService.findByUsername(username);
        if(!(password.equals(doctor.getPassword()))) {
        	return "errorThrow";
        }
        model.addAttribute("doctor", doctor);
        return "doctorProfile"; // returns doctorProfile.html
    }
    
    @GetMapping("/doctor/update")
    public String showUpdateForm1(Model model, @RequestParam String username, @RequestParam String password) {
        Doctor doctor = doctorService.findByUsername(username);
        if(!(password.equals(doctor.getPassword()))) {
        	return "errorThrow";
        }
        model.addAttribute("doctor", doctor);
        return "doctorUpdateForm"; // return the HTML page for the update form
    }

    // POST request to handle form submission and update patient information
    @PostMapping("/doctor/update")
    public String doctorUpdate(@RequestParam String username, @RequestParam String name, 
                                @RequestParam String email, @RequestParam String phone, @RequestParam String specialty) {
        // Retrieve the doctor from the database
        Doctor doctor = doctorService.findByUsername(username);
        
        // Update the doctor information
        doctor.setName(name);
        doctor.setEmail(email);
        doctor.setPhone(phone);
        doctor.setSpecialty(specialty);
        
        // Save the updated doctor information in the database
        doctorService.save(doctor);
        String password=doctor.getPassword();
        
        // Redirect to the doctor profile page
        return "redirect:/doctor/profile?username=" + username+"&password="+password;
    }

    // Appointments with Patients
    @GetMapping("/doctor/appointments")
    public String doctorAppointments(Model model, @RequestParam String username, @RequestParam String password) {
    	Doctor doctor = doctorService.findByUsername(username);
    	if(!(password.equals(doctor.getPassword()))) {
        	return "errorThrow";
        }
        model.addAttribute("doctor", doctor);
        List<Appointment> list1=appointmentService.findByDoctorUsername(username);
        List<CompleteAppointment2> list2=new ArrayList<>();
        for(Appointment ap : list1) {
        	Patient pt=patientService.findByUsername(ap.getPatientUsername());
        	CompleteAppointment2 ca = new CompleteAppointment2(ap, pt);
        	list2.add(ca);
        }
        model.addAttribute("appointments", list2);
        return "doctorAppointments"; // returns doctorAppointments.html
    }
    
    
    
    @GetMapping("/doctor/fffghhh")
    public String patientfffghhh(Model model, @RequestParam String username, @RequestParam String password) {
    	return "redirect:/doctor/profile?username=" + username+"&password="+password;
    }

    // Cancel Appointment
    @PostMapping("/doctor/appointment/cancel")
    public String cancelAppointment(@RequestParam Long appointmentId, @RequestParam String username, @RequestParam String password) {
        appointmentService.cancelAppointment(appointmentId);
        return "redirect:/doctor/appointments?username=" + username+"&password="+password;
    }

    // Admin Login
    @GetMapping("/admin/login")
    public String adminLogin() {
        return "adminLogin"; // returns adminLogin.html
    }

    @PostMapping("/admin/login")
    public String adminLogin(@RequestParam String username, @RequestParam String password, Model model) {
        if ("admin".equals(username) && "adminpass".equals(password)) { // replace with actual admin authentication
            return "redirect:/admin/home?password="+password;
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "adminLogin";
        }
    }
    
    @GetMapping("/admin/logout")
    public String adminLogout() {
    	return "redirect:/";
    }

    // Admin Home
    @GetMapping("/admin/home")
    public String adminHome(@RequestParam String password, Model model) {
    	if(!(password.equals(adminpass1))) {
    		return "errorThrow";
    	}
    	model.addAttribute("admin_password", password);
        return "adminHome"; // returns adminHome.html
    }

    // Patient Information
    @GetMapping("/admin/patients")
    public String adminPatients(Model model, @RequestParam String password) {
    	if(!(password.equals(adminpass1))) {
    		return "errorThrow";
    	}
        model.addAttribute("patients", patientService.findAll());
        model.addAttribute("admin_password", password);
        return "adminPatients"; // returns adminPatients.html
    }
    
    @PostMapping("/admin/patient/delete")
    public String deletePatient(@RequestParam String username, @RequestParam String password) {
    	if(!(password.equals(adminpass1))) {
    		return "errorThrow";
    	}
    	patientService.deletePatient(username);
    	return "redirect:/admin/patients?password="+password;
    }
    
    // Doctor Information
    @GetMapping("/admin/doctors")
    public String adminDoctors(Model model, @RequestParam String password) {
    	if(!(password.equals(adminpass1))) {
    		return "errorThrow";
    	}
        model.addAttribute("doctors", doctorService.findAll());
        model.addAttribute("admin_password", password);
        return "adminDoctors"; // returns adminDoctors.html
    }

    // Add Doctor
    @GetMapping("/admin/doctor/add")
    public String addDoctor(@RequestParam String password, Model model) {
    	if(!(password.equals(adminpass1))) {
    		return "errorThrow";
    	}
    	model.addAttribute("admin_password", password);
        return "addDoctor"; // returns addDoctor.html
    }

    @PostMapping("/admin/doctor/add")
    public String addDoctor(@RequestParam String username, @RequestParam String password, @RequestParam String specialty,  @RequestParam String name,  @RequestParam String phone,  @RequestParam String email, @RequestParam String adminPassword, Model model) {
    	if(!(adminPassword.equals(adminpass1))) {
    		return "errorThrow";
    	}
        doctorService.addDoctor(username, password, specialty, name, phone, email);
        return "redirect:/admin/doctors?password="+adminPassword;
    }

    // Delete Doctor
    @PostMapping("/admin/doctor/delete")
    public String deleteDoctor(@RequestParam String username, @RequestParam String password) {
    	if(!(password.equals(adminpass1))) {
    		return "errorThrow";
    	}
        doctorService.deleteDoctor(username);
        return "redirect:/admin/doctors?password="+password;
    }
}
