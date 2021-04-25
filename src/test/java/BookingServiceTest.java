import com.opencsv.exceptions.CsvException;
import com.psic.*;
import com.psic.exceptions.BookingException;
import com.psic.exceptions.TreatmentBookedException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

@RunWith(JUnit4.class)
public class BookingServiceTest {

    private Main main;
    private BookingService bookingService;

    public BookingServiceTest() throws IOException, CsvException {
        main = new Main();
        bookingService = main.getBookingService();
    }

    @Test
    public void addBookingTest() throws BookingException, TreatmentBookedException {
        Patient patient = main.patientsMap.get("PSIC-7");
        Treatment treatment = main.physioTreatmentAvailMap.get("PSIC-1").get(0);
        Assert.assertTrue(bookingService.addBooking(patient, treatment));
    }

    @Test(expected = TreatmentBookedException.class)
    public void addBookingExceptionTest() throws BookingException, TreatmentBookedException {
        Patient patient = main.patientsMap.get("PSIC-7");
        Treatment treatment = main.physioTreatmentAvailMap.get("PSIC-1").get(0);
        treatment.setStatus(Status.BOOKED);
        Assert.assertTrue(bookingService.addBooking(patient, treatment));
    }

    @Test(expected = BookingException.class)
    public void treatmentOverlappingTest() throws BookingException, TreatmentBookedException {
        Patient patient = main.patientsMap.get("PSIC-7");
        Treatment treatment1 = main.physioTreatmentAvailMap.get("PSIC-1").get(0);
        Treatment treatment2 = main.physioTreatmentAvailMap.get("PSIC-3").get(0);
        bookingService.addBooking(patient, treatment1);
        bookingService.addBooking(patient, treatment2);
    }

    @Test
    public void addConsultancyBookingTest() throws BookingException, TreatmentBookedException {
        String visitor = "V1";
        ConsultancyAppointment consultancyAppointment = main.physioConsultancyAvailMap.get("PSIC-1").get(0);
        bookingService.addConsultancyBooking(visitor, consultancyAppointment);
    }

    @Test(expected = TreatmentBookedException.class)
    public void addConsultancyBookingExceptionTest() throws BookingException, TreatmentBookedException {
        String visitor = "V1";
        ConsultancyAppointment consultancyAppointment = main.physioConsultancyAvailMap.get("PSIC-1").get(0);
        consultancyAppointment.setStatus(Status.BOOKED);
        bookingService.addConsultancyBooking(visitor, consultancyAppointment);
    }

}
