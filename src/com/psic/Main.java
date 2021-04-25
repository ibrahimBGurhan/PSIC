package com.psic;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.psic.exceptions.BookingException;
import com.psic.exceptions.TreatmentBookedException;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    private int seq = 16;
    public Map<String, Patient> patientsMap = new HashMap<>();
    public Map<String, Physiotherapist> physiotherapistsMap = new HashMap<>();
    public Map<String, List<Treatment>> physioTreatmentAvailMap = new HashMap<>();
    public Map<String, List<ConsultancyAppointment>> physioConsultancyAvailMap = new HashMap<>();
    private Scanner scanner = new Scanner(System.in);
    private int choice = -1;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private BookingService bookingService = new BookingService(physioTreatmentAvailMap, physioConsultancyAvailMap);

    public Main() throws IOException, CsvException {
        loadDataFromCSV();
    }

    public static void main(String[] args) throws IOException, CsvException {
        Main main = new Main();
        main.showAndProcessMenu();
    }

    private boolean checkIsInteger(String choiceStr) {
        try {
            Integer.valueOf(choiceStr);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    private void showAndProcessMenu() {
        while (true) {
            System.out.println("*********************** MENU ***********************");
            System.out.println("Select one of the options from below");
            System.out.println("1. Register a new patient");
            System.out.println("2. Book a treatment appointment");
            System.out.println("3. Change a booking");
            System.out.println("4. Attend a treatment appointment");
            System.out.println("5. Book a visitor appointment");
            System.out.println("6. Term (4 weeks) report no 1");
            System.out.println("7. Term (4 weeks) report no 2");
            System.out.println("8. To Exit");
            String choiceStr = scanner.nextLine();
            if (!checkIsInteger(choiceStr)) {
                System.out.println("Invalid choice, please try again");
                continue;
            } else {
                int choice = Integer.parseInt(choiceStr);
                if (choice == 8) {
                    System.out.println("Exiting the system");
                    break;
                } else if (choice > 8 || choice < 1) {
                    System.out.println("Invalid choice, please try again");
                }
                processChoice(choice);
            }
        }
    }

    private void processChoice(int choice) {
        if (choice == 1) {
            addPatient();
        } else if (choice == 2) {
            bookAppointment();
        } else if (choice == 3) {
            changeBooking();
        } else if (choice == 4) {
            attendAppointment();
        } else if (choice == 5) {
            bookVisitorAppointment();
        } else if (choice == 6) {
            showAllPhysicianAppointments();
        } else if (choice == 7) {
            showAllPatientBookings();
        }
    }

    private void showAllPatientBookings() {
        bookingService.displayAllPatientBookings();
    }

    private void showAllPhysicianAppointments() {
        bookingService.displayAllTreatments();
        bookingService.displayAllPhysicianAppointments();
    }

    private void bookVisitorAppointment() {
        System.out.println("*********************** Book a visitor appointment ***********************");
        while (true) {
            System.out.println("Enter visitor's name");
            String visitorName = scanner.nextLine();
            System.out.println("**** Available physiotherapist's consultation hours ****");
            bookingService.displayAvailablePhysioConsultancyHours();
            System.out.println("Enter consultancy appointment id");
            String consultancyAppointmentId = scanner.nextLine();
            ConsultancyAppointment consultancyAppointment = bookingService.getConsultancyAppointment(consultancyAppointmentId);
            if (consultancyAppointment == null) {
                System.out.println("Invalid consultancy appointment id, please try again");
                continue;
            }
            try {
                bookingService.addConsultancyBooking(visitorName, consultancyAppointment);
            } catch (TreatmentBookedException | BookingException e) {
                System.out.println(e.getMessage());
                continue;
            }
            System.out.println("Consultancy Appointment booked successfully");
            break;
        }
    }

    private void attendAppointment() {
        while (true) {
            displayPatients();
            System.out.println("Enter patient's id");
            String patientId = scanner.nextLine();
            if (!patientsMap.containsKey(patientId.trim())) {
                System.out.println("Patient doesn't exist, please try again");
                continue;
            }
            List<Treatment> bookedTreatments = bookingService.getBookedTreatments(patientId);
            if (bookedTreatments == null || bookedTreatments.isEmpty()) {
                System.out.println("No bookings found for this patient");
                break;
            }
            System.out.println("Please select one of the treats from below list \n");
            for (Treatment treatment : bookedTreatments) {
                System.out.println(treatment.toString());
            }
            System.out.print("Treatment id: ");
            String treatmentId = scanner.nextLine();
            bookingService.attendBooking(patientId, treatmentId);
        }
    }

    private void changeBooking() {
        System.out.println("*********************** Change a Booking (Only cancel allowed)***********************");
        System.out.println("****** New booking can be done from main menu ******");
        while (true) {
            displayPatients();
            System.out.println("Enter patient's id");
            String patientId = scanner.nextLine();
            if (!patientsMap.containsKey(patientId.trim())) {
                System.out.println("Patient doesn't exist, please try again");
                continue;
            }
            List<Treatment> bookedTreatments = bookingService.getBookedTreatments(patientId);
            if (bookedTreatments == null || bookedTreatments.isEmpty()) {
                System.out.println("No bookings found for this patient");
                break;
            }
            System.out.println("Please select one of the treats from below list \n");
            for (Treatment treatment : bookedTreatments) {
                System.out.println(treatment.toString());
            }
            System.out.print("Treatment id: ");
            String treatmentId = scanner.nextLine();
            boolean isCancelled = bookingService.cancelBooking(patientId, treatmentId);
            if (isCancelled) {
                System.out.println("Booking cancelled successfully");
            } else {
                System.out.println("Error: Treatment id not found");
            }
            break;
        }
    }

    private void bookAppointment() {
        System.out.println("*********************** Book a treatment appointment ***********************");
        while (true) {
            displayPatients();
            System.out.println("Enter patient's id");
            String patientId = scanner.nextLine();
            if (!patientsMap.containsKey(patientId.trim())) {
                System.out.println("Patient doesn't exist, please try again");
                continue;
            }
            System.out.println("**** Available treatments ****");
            bookingService.displayAvailableTreatments();
            System.out.println("Enter treatment id");
            String treatmentId = scanner.nextLine();
            Treatment treatment = bookingService.getTreatement(treatmentId);
            if (treatment == null) {
                System.out.println("Invalid treatment id, please try again");
                continue;
            }
            try {
                bookingService.addBooking(patientsMap.get(patientId), treatment);
            } catch (BookingException e) {
                System.out.println(e.getMessage());
                continue;
            } catch (TreatmentBookedException e) {
                System.out.println(e.getMessage());
                continue;
            }
            System.out.println("Appointment booked successfully");
            break;
        }
    }

    private void displayPatients() {
        System.out.println("**** List of patients ****");
        for (String patientId : patientsMap.keySet()) {
            System.out.println("Patient - Id: " + patientId + " Name: " + patientsMap.get(patientId).getFullName());
        }
    }

    private void addPatient() {
        System.out.println("*********************** Register a new patient ***********************");
        System.out.println("Enter patient's full name");
        String patientName = scanner.nextLine();
        System.out.println("Enter patient's address");
        String patientAddress = scanner.nextLine();
        System.out.println("Enter patient's telephone number");
        String patientTelephone = scanner.nextLine();
        Patient patient = new Patient("PSIC" + seq++, patientName, patientAddress, patientTelephone);
        patientsMap.put(patient.getId(), patient);
        System.out.println("Patient added successfully");
    }

    private void loadDataFromCSV() throws IOException, CsvException {
        CSVReader csvReader = new CSVReader(new FileReader("members.csv"));
        List<String[]> records = csvReader.readAll();
        for (String[] record : records) {
            if (record[0].startsWith("PHYSICIAN")) {
                Physiotherapist physiotherapist = new Physiotherapist(record[1], record[2], record[3], record[4], record[5], record[6]);
                physiotherapistsMap.put(physiotherapist.getId(), physiotherapist);
            } else if (record[0].startsWith("PATIENT")) {
                Patient patient = new Patient(record[1], record[2], record[3], record[4]);
                patientsMap.put(patient.getId(), patient);
            }
        }
        csvReader = new CSVReader(new FileReader("availability.csv"));
        List<String[]> availabilityRecords = csvReader.readAll();
        Physiotherapist physiotherapist = null;
        for (String[] record : availabilityRecords) {
            if (record[0].startsWith("PSIC")) {
                physiotherapist = physiotherapistsMap.get(record[0]);
            } else {
                String treatmentName = record[0];
                LocalDateTime startTime = LocalDateTime.parse(record[1], dateTimeFormatter);
                LocalDateTime endTime = LocalDateTime.parse(record[2], dateTimeFormatter);
                String room = record[3];
                Treatment treatment = new Treatment(treatmentName, room, startTime, endTime, physiotherapist);
                updatePhysioTreatmentAvailMap(physiotherapist, treatment);
            }
        }
        readPhysioConsultanceyAvailabilityCSV("physio_consultancy_availability.csv");
    }

    private void readPhysioConsultanceyAvailabilityCSV(String file) throws IOException, CsvException {
        CSVReader csvReader = new CSVReader(new FileReader(file));
        List<String[]> availabilityRecords = csvReader.readAll();
        Physiotherapist physiotherapist = null;
        for (String[] record : availabilityRecords) {
            if (record[0].startsWith("PSIC")) {
                physiotherapist = physiotherapistsMap.get(record[0]);
            } else {
                LocalDateTime startTime = LocalDateTime.parse(record[0], dateTimeFormatter);
                LocalDateTime endTime = LocalDateTime.parse(record[1], dateTimeFormatter);
                ConsultancyAppointment treatment = new ConsultancyAppointment(null, startTime, endTime, physiotherapist);
                updatePhysioConsultancyAvailMap(physiotherapist, treatment);
            }
        }
    }

    private void updatePhysioConsultancyAvailMap(Physiotherapist physiotherapist, ConsultancyAppointment treatment) {
        if (!physioConsultancyAvailMap.containsKey(physiotherapist.getId())) {
            physioConsultancyAvailMap.put(physiotherapist.getId(), new ArrayList<>());
        }
        physioConsultancyAvailMap.get(physiotherapist.getId()).add(treatment);
    }

    private void updatePhysioTreatmentAvailMap(Physiotherapist physiotherapist, Treatment treatment) {
        if (!physioTreatmentAvailMap.containsKey(physiotherapist.getId())) {
            physioTreatmentAvailMap.put(physiotherapist.getId(), new ArrayList<>());
        }
        physioTreatmentAvailMap.get(physiotherapist.getId()).add(treatment);
    }

    public BookingService getBookingService() {
        return bookingService;
    }



}
