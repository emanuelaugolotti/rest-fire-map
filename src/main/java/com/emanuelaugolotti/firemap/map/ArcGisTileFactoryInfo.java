package com.emanuelaugolotti.firemap.map;

import org.jxmapviewer.viewer.TileFactoryInfo;

public class ArcGisTileFactoryInfo extends TileFactoryInfo {

    public ArcGisTileFactoryInfo() {
        super("ArcGis",
                3,
                17,
                19,
                256,
                true,
                true,
                "https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/",
                "x", "y", "z");
    }

    @Override
    public String getTileUrl(int x, int y, int zoom) {
        int z = 19 - zoom;
        return this.baseURL + z + "/" + y + "/" + x + ".png";
    }
}