package com.emanuelaugolotti.firemap;

import java.util.Objects;

public class Fire {
    private double latitude;
    private double longitude;

    public Fire(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fire fire = (Fire) o;
        return Double.compare(latitude, fire.latitude) == 0 && Double.compare(longitude, fire.longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    @Override
    public String toString() {
        return "Fire{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
