package de.adorsys.smartanalytics.calculator;

import de.adorsys.smartanalytics.api.WrappedBooking;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by alexg on 18.08.17.
 */
public class AmountCalculator {

    public static BigDecimal calcAmount(List<WrappedBooking> bookings) {
        if (bookings == null || bookings.isEmpty()) {
            return null;
        }

        List<WrappedBooking> sortedBookings = bookings.stream()
                .sorted(Comparator.comparing(WrappedBooking::getAmount))
                .collect(Collectors.toList());

        final int countBookings = sortedBookings.size();

        if (countBookings <= 1) {
            // Default, nimm den letzten Betrag. Deckt den Fall mit nur einem jährlichen Umsatz ab.
            return sortedBookings.get(countBookings - 1).getAmount();
        }

        if (countBookings == 2) {
            return calcAmountTwoBookings(sortedBookings, countBookings);
        }

        List<BigDecimal> lastThreeAmounts = sortedBookings.stream()
                .map(booking -> booking.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP))
                .limit(3)
                .collect(Collectors.toList());

        BigDecimal twoSame = checkTwoSameAmount(lastThreeAmounts);

        if (twoSame != null) {
            return twoSame;
        }

        return new BigDecimal(sortedBookings.stream()
                .mapToDouble(value -> value.getAmount().doubleValue())
                .average().getAsDouble());

    }

    private static BigDecimal calcAmountTwoBookings(List<WrappedBooking> sortierteUmsaetze, int umsatzAnzahl) {
        // Wenn mehr als ein Umsatz in der Liste
        List<WrappedBooking> letzteZweiUmsaetze = sortierteUmsaetze.subList(umsatzAnzahl - 2, umsatzAnzahl);

        // Prüfe, ob die zwei letzten Transaktionen einer sonstigen Fixkostenzahlung den gleichen Betrag hatten
        // (last_transaction.amount = second_last_transaction.amount)
        final boolean letzteZweiBetraegeGleich = // Hinweis: Hier könnte eine NPE auftreten, falls der Betrag beim (0) null ist.
                letzteZweiUmsaetze.get(0).getAmount().compareTo(letzteZweiUmsaetze.get(1).getAmount()) == 0;

        if (letzteZweiBetraegeGleich) {
            // Dann setze diesen Betrag als zu erwartenden Betrag für die nächste Transaktion
            return letzteZweiUmsaetze.get(0).getAmount();
        }

        // sollten nur zwei Transaktionen vorliegen,
        // dann soll daraus der auf volle Euro aufgerundete Mittelwert berechnet werden
        return ermittleMittelwert(letzteZweiUmsaetze);
    }

    private static BigDecimal ermittleMittelwert(List<WrappedBooking> umsaetze) {
        BigDecimal summe = BigDecimal.ZERO;
        for (WrappedBooking umsatz : umsaetze) {
            summe = summe.add(umsatz.getAmount());
        }
        return summe.divide(new BigDecimal(umsaetze.size()), RoundingMode.CEILING)
                .setScale(0, RoundingMode.CEILING).setScale(2, RoundingMode.UNNECESSARY);
    }

    /**
     * Es wird ermittelt ob zwei BigDecimal Zahlen gleich sind,
     * wenn diese gleich sind, wird der Betrag ausgegeben.
     */
    static BigDecimal checkTwoSameAmount(List<BigDecimal> letztenDreiBetraege) {

        Map<BigDecimal, Long> gruppen = letztenDreiBetraege.stream()
                .map(betrag -> betrag.setScale(2, BigDecimal.ROUND_HALF_UP))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return gruppen.entrySet().stream()
                .filter(entry -> entry.getValue() >= 2)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }
}
