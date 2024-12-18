package com.emanuelaugolotti.firemap;

import org.jxmapviewer.viewer.TileFactoryInfo;

public class TracestrackTileFactoryInfo extends TileFactoryInfo {

    // Utilizzata una mappa alternativa a quella di default della libreria presa da un server di mappe, chiamato tracestrack
    public TracestrackTileFactoryInfo() {
        this("tracetrack", "https://tile.tracestrack.com");
    }

    public TracestrackTileFactoryInfo(String name, String baseURL) {
        super(name, 0, 18, 19, 512, true, true, baseURL, "x", "y", "z");
    }

    @Override
    public String getTileUrl(int x, int y, int zoom) {
        int z = 19 - zoom;
        String url = this.baseURL + "/en" + "/" + z + "/" + x + "/" + y + ".png" + "?key=fe087be4b2b3c8f992fce4262b13938d";
        return url;
    }
}
