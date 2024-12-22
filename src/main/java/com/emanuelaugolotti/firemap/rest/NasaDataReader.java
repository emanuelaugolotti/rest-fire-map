package com.emanuelaugolotti.firemap.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public class NasaDataReader {

    private final String apiKey;
    private final static String BASE_URI = "https://firms.modaps.eosdis.nasa.gov/api/area/csv/";

    public NasaDataReader(String key) {
        this.apiKey = key;
    }

    private URI getUri(int dayRange, String area, String source) throws URISyntaxException {
        return new URI(BASE_URI + apiKey + "/" + source + "/" + area + "/" + dayRange);
    }

    public Set<FireWaypoint> getData(int dayRange, String area, String source) {
        //HttpClient è una classe che permette di chiamare delle API rest, lo metto dentro alle parentesi tonde del
        //try-catch per evitare la perdita di risorse

        try (HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build()) {
            URI uri = getUri(dayRange, area, source);
            HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException("Unsuccessful response: code = " + response.statusCode());
            }
            return parse(response.body());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private LocalDateTime parseAcquisitionTime(LocalDate date, String timeString) {
        //funzione per trasformare l'ora nel formato giusto, ritorna un LocalDateTime

        int numberOfZero = 4 - timeString.length();
        StringBuilder sb = new StringBuilder();
        sb.repeat("0", numberOfZero).append(timeString).insert(2, ":");
        LocalTime time = LocalTime.parse(sb);
        return LocalDateTime.of(date, time);
    }

    private Set<FireWaypoint> parse(String data) {
        Set<FireWaypoint> fires = new HashSet<>();
        String[] arrayString = data.split("\n");

        for (int i = 1; i < arrayString.length; i++) {
            String[] tmpArrayString = arrayString[i].split(",");
            double latitude = Double.parseDouble(tmpArrayString[0]);
            double longitude = Double.parseDouble(tmpArrayString[1]);
            String instrument = tmpArrayString[8];
            String confidence = tmpArrayString[9];
            double frp = Double.parseDouble(tmpArrayString[12]);
            char daynight = tmpArrayString[13].charAt(0);
            LocalDateTime acquisitionTime = parseAcquisitionTime(LocalDate.parse(tmpArrayString[5]), tmpArrayString[6]);
            FireWaypoint fire = new FireWaypoint(latitude, longitude, acquisitionTime, frp, daynight, instrument, confidence);
            fires.add(fire);
        }

        return fires;
    }
}