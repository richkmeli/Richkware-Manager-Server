package it.richkmeli.rms.data.entity.device.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import it.richkmeli.jframework.auth.model.exception.ModelException;
import it.richkmeli.jframework.util.DataFormat;
import it.richkmeli.jframework.util.log.Logger;
import org.hibernate.validator.constraints.Length;
import org.json.JSONObject;

import javax.persistence.*;

@Entity
public class Location {
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "device_name")
    private String id;
    private double latitude;
    private double longitude;
    private double accuracy;
    private double altitude;
    @Length(max = 20)
    private String provider;
    private boolean isMock;

    @OneToOne
    @MapsId
    @JoinColumn(name = "device_name")
    @JsonManagedReference
    private Device device;

    public static final String LATITUDE_JSON_KEY = "latitude";
    public static final String LONGITUDE_JSON_KEY = "longitude";
    public static final String ACCURACY_JSON_KEY = "accuracy";
    public static final String ALTITUDE_JSON_KEY = "altitude";
    public static final String PROVIDER_JSON_KEY = "provider";
    public static final String IS_MOCK_JSON_KEY = "isMock";

    public Location() {
    }

    public Location(double latitude, double longitude, double accuracy, double altitude, @Length(max = 20) String provider, boolean isMock) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
        this.altitude = altitude;
        this.provider = provider;
        this.isMock = isMock;
    }

    public Location(String locationJson) throws ModelException {
        if (DataFormat.isJSONValid(locationJson)) {
            JSONObject locationData = new JSONObject(locationJson);
            //Logger.info(location);
            this.latitude = locationData.getDouble(LATITUDE_JSON_KEY);
            this.longitude = locationData.getDouble(LONGITUDE_JSON_KEY);
            this.accuracy = locationData.getDouble(ACCURACY_JSON_KEY);
            this.altitude = locationData.getDouble(ALTITUDE_JSON_KEY);
            this.provider = locationData.getString(PROVIDER_JSON_KEY);
            this.isMock = locationData.getBoolean(IS_MOCK_JSON_KEY);

        } else {
            String error = "Location JSON not correct";
            //throw new ModelException(error);
            Logger.debug(error);
            this.latitude = 0;
            this.longitude = 0;
            this.accuracy = 0;
            this.altitude = 0;
            this.provider = null;
            this.isMock = false;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public boolean isMock() {
        return isMock;
    }

    public void setMock(boolean mock) {
        isMock = mock;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    @Override
    public String toString() {
        String output = "";
        output = "{" + getLatitude() + ", "
                + getLongitude() + ", "
                + getAccuracy() + ", "
                + getAltitude() + ", "
                + getProvider() + ", "
                + isMock()
                + "}";

        return output;
    }
}
