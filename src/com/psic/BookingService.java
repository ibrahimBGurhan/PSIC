package com.psic;

import com.psic.exceptions.BookingException;
import com.psic.exceptions.TreatmentBookedException;

import java.util.*;

public class BookingService {

    private final Map<String, List<Treatment>> physioTreatmentAvailMap;
    private final Map<String, List<ConsultancyAppointment>> physioConsultancyAvailMap;
    private Map<String, List<Treatment>> bookings = new HashMap<>();
    private Map<String, List<ConsultancyAppointment>> consultancyBookings = new HashMap<>();

    public BookingService(Map<String, List<Treatment>> physioTreatmentAvailMap, Map<String,
            List<ConsultancyAppointment>> physioConsultancyAvailMap) {
        this.physioTreatmentAvailMap = physioTreatmentAvailMap;
        this.physioConsultancyAvailMap = physioConsultancyAvailMap;
    }

    public boolean addBooking(Patient patient, Treatment treatment) throws TreatmentBookedException, BookingException {
        if (treatment.getStatus() == Status.BOOKED) {
            throw new TreatmentBookedException("Treatment is already booked");
        }
        if (isTreatmentOverlapping(patient, treatment)) {
            throw new BookingException("Cannot book a treatment as it is Overlapping with the already booked treatment at the given time");
        }
        treatment.setStatus(Status.BOOKED);
        if (!bookings.containsKey(patient.getId())) {
            bookings.put(patient.getId(), new ArrayList<>());
        }
        bookings.get(patient.getId()).add(treatment);
        return true;
    }


    public boolean addConsultancyBooking(String visitorName, ConsultancyAppointment consultancyAppointment) throws TreatmentBookedException, BookingException {
        if (consultancyAppointment.getStatus() == Status.BOOKED) {
            throw new TreatmentBookedException("Treatment is already booked");
        }
        if (isConsultancyAppointmentOverlapping(visitorName, consultancyAppointment)) {
            throw new BookingException("Overlapping Treatment is already booked at the given time");
        }
        consultancyAppointment.setStatus(Status.BOOKED);
        if (!consultancyBookings.containsKey(visitorName)) {
            consultancyBookings.put(visitorName, new ArrayList<>());
        }
        consultancyBookings.get(visitorName).add(consultancyAppointment);
        return true;
    }

    private boolean isConsultancyAppointmentOverlapping(String visitorName, ConsultancyAppointment consultancyAppointment) {
        if (consultancyBookings.containsKey(visitorName)) {
            List<ConsultancyAppointment> consultanciesBooked = consultancyBookings.get(visitorName);
            for (ConsultancyAppointment t : consultanciesBooked) {
                if (t.getStartTime().isEqual(consultancyAppointment.getStartTime())) {
                    return true;
                }
                if (t.getEndTime().isEqual(consultancyAppointment.getEndTime())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isTreatmentOverlapping(Patient patient, Treatment treatment) {
        if (bookings.containsKey(patient.getId())) {
            List<Treatment> treatmentsBooked = bookings.get(patient.getId());
            for (Treatment t : treatmentsBooked) {
                if (t.getStartTime().isEqual(treatment.getStartTime())) {
                    return true;
                }
                if (t.getEndTime().isEqual(treatment.getEndTime())) {
                    return true;
                }
            }
        }
        return false;
    }

    public Treatment getTreatement(String treatmentId) {
        Collection<List<Treatment>> allTreatments = physioTreatmentAvailMap.values();
        for (List<Treatment> treatments : allTreatments) {
            for (Treatment treatment : treatments) {
                if (treatment.getId().equals(treatmentId) && treatment.getStatus() == Status.AVAILABLE) {
                    return treatment;
                }
            }
        }
        return null;
    }

    public ConsultancyAppointment getConsultancyAppointment(String consultancyAppointmentId) {
        Collection<List<ConsultancyAppointment>> allTreatments = physioConsultancyAvailMap.values();
        for (List<ConsultancyAppointment> consultancyAppointments : allTreatments) {
            for (ConsultancyAppointment consultancyAppointment : consultancyAppointments) {
                if (consultancyAppointment.getId().equals(consultancyAppointmentId) &&
                        consultancyAppointment.getStatus() == Status.AVAILABLE) {
                    return consultancyAppointment;
                }
            }
        }
        return null;
    }

    public List<Treatment> getBookedTreatments(String patientId) {
        return bookings.get(patientId);
    }

    public boolean cancelBooking(String patientId, String treatmentId) {
        List<Treatment> treatments = bookings.get(patientId);
        Treatment t = null;
        for (Treatment treatment : treatments) {
            if (treatment.getId().equals(treatmentId)) {
                t = treatment;
                treatment.setStatus(Status.CANCELLED);
            }
        }
        return treatments.remove(t);
    }

    public boolean attendBooking(String patientId, String treatmentId) {
        List<Treatment> treatments = bookings.get(patientId);
        Treatment t = null;
        for (Treatment treatment : treatments) {
            if (treatment.getId().equals(treatmentId)) {
                t = treatment;
                treatment.setStatus(Status.ATTENDED);
                return true;
            }
        }
        return false;
    }

    public void displayAvailablePhysioConsultancyHours() {
        Collection<List<ConsultancyAppointment>> consultancyMap = physioConsultancyAvailMap.values();
        for (List<ConsultancyAppointment> consultancyAppointments : consultancyMap) {
            for (ConsultancyAppointment consultancyAppointment : consultancyAppointments) {
                if (consultancyAppointment.getStatus() == Status.AVAILABLE) {
                    System.out.println(consultancyAppointment);
                }
            }
        }
    }

    public void displayAvailableTreatments() {
        Collection<List<Treatment>> allTreatments = physioTreatmentAvailMap.values();
        for (List<Treatment> treatments : allTreatments) {
            for (Treatment treatment : treatments) {
                if (treatment.getStatus() == Status.AVAILABLE) {
                    System.out.println(treatment);
                }
            }
        }
    }

    public void displayAllTreatments() {
        System.out.println("*********************** All treatment appointments ***********************");
        Collection<List<Treatment>> allTreatments = physioTreatmentAvailMap.values();
        for (List<Treatment> treatments : allTreatments) {
            for (Treatment treatment : treatments) {
                System.out.println(treatment);
            }
        }
    }

    public void displayAllPhysicianAppointments() {
        System.out.println("*********************** All Visitor appointments ***********************");
        Collection<List<ConsultancyAppointment>> consultancyMap = physioConsultancyAvailMap.values();
        if (consultancyMap.isEmpty()) {
            System.out.println("No visitor appointments exist");
        }
        for (List<ConsultancyAppointment> consultancyAppointments : consultancyMap) {
            for (ConsultancyAppointment consultancyAppointment : consultancyAppointments) {
                System.out.println(consultancyAppointment);
            }
        }
    }

    public void displayAllPatientBookings() {
        System.out.println("*********************** All treatment appointments ***********************");
        Collection<List<Treatment>> allBookings = bookings.values();
        if (allBookings.isEmpty()) {
            System.out.println("No bookings exist");
        }
        for (List<Treatment> treatments : allBookings) {
            for (Treatment treatment : treatments) {
                System.out.println(treatment);
            }
        }
    }
}