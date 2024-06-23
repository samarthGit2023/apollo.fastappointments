package com.apollo.fastappointments.controller;

import com.apollo.fastappointments.model.Appointment;
import com.apollo.fastappointments.model.Doctor;

public class CompleteAppointment {
    private Appointment appointment;
    private Doctor doctor;

    public CompleteAppointment(Appointment appointment, Doctor doctor) {
        this.appointment = appointment;
        this.doctor = doctor;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public Doctor getDoctor() {
        return doctor;
    }
}
