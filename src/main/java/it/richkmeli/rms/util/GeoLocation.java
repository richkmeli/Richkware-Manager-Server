package it.richkmeli.rms.util;

import com.mapbox.services.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.services.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.services.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.services.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.services.commons.models.Position;
import it.richkmeli.jframework.util.log.Logger;
import it.richkmeli.rms.data.entity.configuration.ConfigurationEnum;
import it.richkmeli.rms.data.entity.configuration.ConfigurationManager;

import java.io.IOException;
import java.util.List;

public class GeoLocation {

    public static final String POSITION_NOT_FOUND = "position not found";

    public static String getPositionFromCoordinates(double longitude, double latitude, double altitude) {

        String mapboxAccessToken = ConfigurationManager.getValue(ConfigurationEnum.MAPBOX_ACCESS_TOKEN);

        MapboxGeocoding reverseGeocode = new MapboxGeocoding.Builder()
                .setAccessToken(mapboxAccessToken)
                .setCoordinates(Position.fromCoordinates(longitude, latitude, altitude))
                .setGeocodingType(GeocodingCriteria.TYPE_ADDRESS)
                .build();
        try {
            GeocodingResponse response = reverseGeocode.executeCall().body();
            List<CarmenFeature> results = response.getFeatures();
            if (results.size() > 0) {
                // Get the first Feature from the successful geocoding response
                CarmenFeature feature = results.get(0);
                // Get the address string from the CarmenFeature
                String place = feature.getPlaceName();
                //Logger.info(place);
                return place;
            } else {
                return POSITION_NOT_FOUND;
            }
        } catch (IOException e) {
            Logger.error(e);
            return POSITION_NOT_FOUND;
        }

    }
}
