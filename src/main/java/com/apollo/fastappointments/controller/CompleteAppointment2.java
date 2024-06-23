package com.apollo.fastappointments.controller;

import com.apollo.fastappointments.model.Appointment;
import com.apollo.fastappointments.model.Patient;

public class CompleteAppointment2 {
    private Appointment appointment;
    private Patient patient;

    public CompleteAppointment2(Appointment appointment, Patient patient) {
        this.appointment = appointment;
        this.patient = patient;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public Patient getPatient() {
        return patient;
    }
}
