package telran.time;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.*;
import java.time.chrono.HijrahDate;
import java.time.chrono.MinguoDate;
import java.time.chrono.ThaiBuddhistDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

public class DateTimeTest {

    @Test
    void localDateTest() {
        LocalDate current = LocalDate.now();
        LocalDateTime currentTime = LocalDateTime.now();
        ZonedDateTime currentZonedDateTime = ZonedDateTime.now();
        Instant currentInstant = Instant.now();
        LocalTime currenLocaltTime = LocalTime.now();
        System.out.printf("Current date is %s in ISO format \n", current);
        System.out.printf("Current date and time is %s in ISO format \n", currentTime);
        System.out.printf("Current zoned date & time is %s in ISO format \n", currentZonedDateTime);
        System.out.printf("Current instant is %s in ISO format \n", currentInstant);
        System.out.printf("Current time is %s in ISO format \n", currenLocaltTime);
        System.out.printf("Current date is %s in dd/mm/yyyy \n", current.format(DateTimeFormatter.ofPattern("dd/MMMM/yyyy", Locale.forLanguageTag("he"))));
    }

    @Test
    void nextFriday13Test() {
        LocalDate current = LocalDate.of(2024, 8, 11);
        LocalDate expected = LocalDate.of(2024, 9, 13);
        TemporalAdjuster adjuster = new NextFriday13();
        assertEquals(expected, current.with(new NextFriday13()));
        assertThrows(RuntimeException.class, () -> LocalTime.now().with(adjuster));
    }

    @Test
    void PastTemporalDateProximityTest() {
        LocalDate localDate1 = LocalDate.now();
        LocalDate localDate2 = LocalDate.of(2024, 12, 8);
        LocalDateTime localDateTime1 = LocalDateTime.of(2024, 9, 20, 3, 8, 3);
        LocalDateTime localDateTime2 = LocalDateTime.of(2024, 12, 20, 3, 8, 3);
        HijrahDate hijarahDate1 = HijrahDate.of(1446, 8, 10);
        HijrahDate hijarahDate2 = HijrahDate.of(1446, 10, 10);
        MinguoDate minguoDate1 = MinguoDate.now();
        ThaiBuddhistDate thaiBuddhistDate = ThaiBuddhistDate.now();

        Temporal[] temporals = {localDate1, localDate2, localDateTime1, localDateTime2, hijarahDate1, hijarahDate2, minguoDate1, thaiBuddhistDate};
        Temporal[] temporalsSort = {localDate1, minguoDate1, thaiBuddhistDate, localDateTime1, localDate2, localDateTime2, hijarahDate1, hijarahDate2};

        PastTemporalDateProximity pastTemporalDateProximity = new PastTemporalDateProximity(temporals);

        assertArrayEquals(temporalsSort, pastTemporalDateProximity.temporals);

        assertEquals(LocalDate.from(thaiBuddhistDate), LocalDate.from(pastTemporalDateProximity.adjustInto(localDateTime1)));
        assertEquals(hijarahDate2, pastTemporalDateProximity.adjustInto(HijrahDate.of(1447, 10, 10)));

        assertEquals(null, pastTemporalDateProximity.adjustInto(minguoDate1));
        assertEquals(null, pastTemporalDateProximity.adjustInto(thaiBuddhistDate));
    }
}
