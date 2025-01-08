package com.emanuelaugolotti.firemap.map;

import org.jxmapviewer.viewer.TileFactoryInfo;

public class TracestrackTileFactoryInfo extends TileFactoryInfo {
    private final String key;

    public TracestrackTileFactoryInfo(String key) {
        this("tracetrack", "https://tile.tracestrack.com", key);
    }

    public TracestrackTileFactoryInfo(String name, String baseURL, String key) {
        super(name, 5, 18, 19, 512, true, true, baseURL, "x", "y", "z");
        this.key = key;
    }

    @Override
    public String getTileUrl(int x, int y, int zoom) {
        int z = 19 - zoom;
        return this.baseURL + "/en" + "/" + z + "/" + x + "/" + y + ".png" + "?key=" + key;
    }
}