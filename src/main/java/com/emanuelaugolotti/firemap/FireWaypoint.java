package com.emanuelaugolotti.firemap;

import org.jxmapviewer.viewer.DefaultWaypoint;

import java.time.LocalDateTime;
import java.util.Objects;

// A waypoint that identifies a fire with its data in the map
public class FireWaypoint extends DefaultWaypoint {
    private LocalDateTime acqusitionTime;   // data e ora di acquisizione dell'incendio
    private double frp;     // Potenza in mega watt dell'incendio
    private char daynight; // D = Fuoco diurno, N = Fuoco notturno
    private String instrument;  // tipologia del sensore che ha fatto la rilevazione dell'incendio
    private String confidence;  // probabilità che l'incendio sia veritiero

    public FireWaypoint(double latitude, double longitude, LocalDateTime acqusitionTime, double frp, char daynight,
                        String instrument, String confidence) {
        super(latitude, longitude);
        this.acqusitionTime = acqusitionTime;
        this.frp = frp;
        this.daynight = daynight;
        this.instrument = instrument;
        this.confidence = confidence;
    }

    public LocalDateTime getAcqusitionTime() {
        return acqusitionTime;
    }

    public double getFrp() {
        return frp;
    }

    public char getDaynight() {
        return daynight;
    }

    public String getInstrument() {
        return instrument;
    }

    public String getConfidence() {
        return confidence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FireWaypoint that = (FireWaypoint) o;
        return Double.compare(frp, that.frp) == 0 && daynight == that.daynight && Objects.equals(acqusitionTime, that.acqusitionTime)
                && Objects.equals(instrument, that.instrument) && Objects.equals(confidence, that.confidence) && Objects.equals(getPosition(), that.getPosition());
    }

    @Override
    public int hashCode() {
        return Objects.hash(acqusitionTime, frp, daynight, instrument, confidence, getPosition());
    }
}
