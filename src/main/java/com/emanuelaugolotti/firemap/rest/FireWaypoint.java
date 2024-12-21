package com.emanuelaugolotti.firemap.rest;

import org.jxmapviewer.viewer.DefaultWaypoint;
import java.time.LocalDateTime;
import java.util.Objects;

// A waypoint that identifies a fire with its data in the map
public class FireWaypoint extends DefaultWaypoint {
    // Classe che contiene i dati di un incendio sulla mappa

    private final LocalDateTime acquisitionTime;   // data e ora di acquisizione dell'incendio
    private final double frp;     // Potenza in mega watt dell'incendio
    private final char dayNight; // D = Fuoco diurno, N = Fuoco notturno
    private final String instrument;  // tipologia del sensore che ha fatto la rilevazione dell'incendio
    private final String confidence;  // probabilità che l'incendio sia veritiero

    public FireWaypoint(double latitude, double longitude, LocalDateTime acquisitionTime, double frp, char dayNight,
                        String instrument, String confidence) {
        super(latitude, longitude);
        this.acquisitionTime = acquisitionTime;
        this.frp = frp;
        this.dayNight = dayNight;
        this.instrument = instrument;
        this.confidence = confidence;
    }

    public LocalDateTime getAcquisitionTime() {
        return acquisitionTime;
    }

    public double getFrp() {
        return frp;
    }

    public char getDayNight() {
        return dayNight;
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
        return Double.compare(frp, that.frp) == 0 && dayNight == that.dayNight && Objects.equals(acquisitionTime, that.acquisitionTime)
                && Objects.equals(instrument, that.instrument) && Objects.equals(confidence, that.confidence) && Objects.equals(getPosition(), that.getPosition());
    }

    @Override
    public int hashCode() {
        return Objects.hash(acquisitionTime, frp, dayNight, instrument, confidence, getPosition());
    }

    @Override
    public String toString() {
        return "FireWaypoint{" +
                "acquisitionTime=" + acquisitionTime +
                ", frp=" + frp +
                ", dayNight=" + dayNight +
                ", instrument='" + instrument + '\'' +
                ", confidence='" + confidence + '\'' +
                '}';
    }
}