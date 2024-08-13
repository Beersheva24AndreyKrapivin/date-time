package telran.time;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.util.Arrays;

public class PastTemporalDateProximity implements TemporalAdjuster{
    // array of temporals supporting Day, Month, Year (Dates)
    Temporal[] temporals;

    public PastTemporalDateProximity(Temporal[] temporals) {
        this.temporals = Arrays.copyOf(temporals, temporals.length);
        Arrays.sort(this.temporals, (t1, t2) -> {
            return LocalDate.from(t1).compareTo(LocalDate.from(t2));
        });
    }

    @Override
    public Temporal adjustInto(Temporal temporal) {
        // TODO Auto-generated method stub
        // return the temporal for the incapsulated array that is a neares in past
        Temporal res = null;
        int index = binarySearchTemporal(temporal);

        if (index >= 0) {
            long amount = LocalDate.from(temporal).until(LocalDate.from(temporals[index]), ChronoUnit.DAYS);
            res = temporal.plus(amount, ChronoUnit.DAYS);   
        }
        
        return res;
    }

    private int binarySearchTemporal(Temporal key) {
        int first = 0;
        int last = temporals.length - 1;
        int middleIndex = (first + last) / 2;
        int compInt = 0;

        while (first <= last) {
            compInt = LocalDate.from(key).compareTo(LocalDate.from(temporals[middleIndex]));
            if (compInt > 0) {
                first = middleIndex + 1;
            } else {
                last = middleIndex - 1;
            }
            middleIndex = (first + last) / 2;
        }

        return last;
    }

}
