package de.adorsys.smartanalytics.calculator;

import de.adorsys.smartanalytics.api.Cycle;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static de.adorsys.smartanalytics.api.Cycle.*;


/**
 * Created by alexg on 18.08.17.
 */
public class CycleCalculator {

    public static final int MIND_ANZAHL_UMSAETZE_FUER_WOECHENTLICHES_INTERVALL = 6;
    public static final int MAX_ANZAHL_RELEVANTE_DATEN = 13;

    public static Cycle fromDates(List<LocalDate> daten) {
        if (daten.size() == 1) {
            return YEARLY;
        }

        List<LocalDate> sortierteDaten = daten.stream()
                .sorted(Comparator.naturalOrder())
                // nur die letzten 13 Umsätze berücksichtigen
                .skip(Math.max(0, daten.size() - MAX_ANZAHL_RELEVANTE_DATEN))
                .collect(Collectors.toList());

        List<Long> abstaende = new ArrayList<>();
        for (int i = 0; i < sortierteDaten.size() - 1; i++) {
            abstaende.add(ChronoUnit.DAYS.between(sortierteDaten.get(i), sortierteDaten.get(i + 1)));
        }

        for (Cycle ausfuehrungsintervall : Cycle.values()) {
            if (ausfuehrungsintervall == WEEKLY && daten.size() < MIND_ANZAHL_UMSAETZE_FUER_WOECHENTLICHES_INTERVALL) {
                continue;
            }

            if (genuegendAbstaendeMitPassenderLaenge(ausfuehrungsintervall.getMinDays(), ausfuehrungsintervall.getMaxDays(), abstaende)) {
                return ausfuehrungsintervall;
            }

            // ZKB-3068
            if (ausfuehrungsintervall == MONTHLY) {
                long abstandMittelwert = Math.round(abstaende.stream().mapToLong(l -> l).sum() / (float) abstaende.size());
                if (genuegendAbstaendeMitPassenderLaenge(ausfuehrungsintervall.getMinDays(), ausfuehrungsintervall.getMaxDays(), abstandMittelwert)) {
                    return ausfuehrungsintervall;
                }
            }
        }
        return null;
    }

    private static boolean genuegendAbstaendeMitPassenderLaenge(int min, int max, long abstandMittelwert) {
        return abstandMittelwert >= min && abstandMittelwert <= max;
    }

    private static boolean genuegendAbstaendeMitPassenderLaenge(int min, int max, List<Long> abstaende) {
        int fehlerhafteIntervalle = (int) abstaende.stream()
                .map(abstand -> abstand >= min && abstand <= max)
                .filter(imIntervall -> !imIntervall)
                .count();
        if (abstaende.size() <= 2) {
            return fehlerhafteIntervalle == 0;
        }
        if (abstaende.size() <= 11) {
            return fehlerhafteIntervalle <= 1;
        }
        return fehlerhafteIntervalle <= 3;
    }


}
