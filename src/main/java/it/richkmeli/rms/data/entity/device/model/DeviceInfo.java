package it.richkmeli.rms.data.entity.device.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import it.richkmeli.jframework.util.DataFormat;
import it.richkmeli.jframework.util.log.Logger;
import org.hibernate.validator.constraints.Length;
import org.json.JSONObject;

import javax.persistence.*;

@Entity
public class DeviceInfo {
        @Id
        //@GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "device_name")
        private String id;
        @Length(max = 50)
        private String osVersion;
        private int osApiLevel;
        @Length(max = 20)
        private String devName;
        @Length(max = 25)
        private String brand;
        @Length(max = 50)
        private String modelProduct;

        @OneToOne
        @MapsId
        @JoinColumn(name = "device_name")
        @JsonManagedReference
        private Device device;

        public static final String OS_VERSION_JSON_KEY = "os_version";
        public static final String OS_API_LEVEL_JSON_KEY = "os_api_level";
        public static final String DEVICE_JSON_KEY = "device";
        public static final String BRAND_JSON_KEY = "brand";
        public static final String MODEL_PRODUCT_JSON_KEY = "model_product";

        public DeviceInfo(){}

        public DeviceInfo(String osVersion, int osApiLevel, @Length(max = 20) String deviceName, @Length(max = 25) String brand, @Length(max = 50) String modelProduct) {
                //this.id = SHA256.hash(osVersion+osApiLevel+ deviceName +brand+modelProduct);
                this.osVersion = osVersion;
                this.osApiLevel = osApiLevel;
                this.devName = deviceName;
                this.brand = brand;
                this.modelProduct = modelProduct;
        }

        public DeviceInfo(String deviceInfoJson) {
                //this.id = SHA256.hash("test");
                if (DataFormat.isJSONValid(deviceInfoJson)) {
                        JSONObject locationData = new JSONObject(deviceInfoJson);
                        //Logger.info(location);
                        this.osVersion = locationData.getString(OS_VERSION_JSON_KEY);
                        this.osApiLevel = locationData.getInt(OS_API_LEVEL_JSON_KEY);
                        this.devName = locationData.getString(DEVICE_JSON_KEY);
                        this.brand = locationData.getString(BRAND_JSON_KEY);
                        this.modelProduct = locationData.getString(MODEL_PRODUCT_JSON_KEY);

                } else {
                        String error = "DeviceInfo JSON not correct";
                        //throw new ModelException(error);
                        Logger.debug(error);
                        this.osVersion = null;
                        this.osApiLevel = 0;
                        this.devName = null;
                        this.brand = null;
                        this.modelProduct = null;
                }
        }

        public String getId() {
                return id;
        }

        public void setId(String id) {
                this.id = id;
        }

        public String getOsVersion() {
                return osVersion;
        }

        public void setOsVersion(String osVersion) {
                this.osVersion = osVersion;
        }

        public int getOsApiLevel() {
                return osApiLevel;
        }

        public void setOsApiLevel(int osApiLevel) {
                this.osApiLevel = osApiLevel;
        }

        public String getDevName() {
                return devName;
        }

        public void setDevName(String device) {
                this.devName = device;
        }

        public String getBrand() {
                return brand;
        }

        public void setBrand(String brand) {
                this.brand = brand;
        }

        public String getModelProduct() {
                return modelProduct;
        }

        public void setModelProduct(String modelProduct) {
                this.modelProduct = modelProduct;
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
                output = "{" + getOsVersion() + ", "
                        + getOsApiLevel() + ", "
                        + getDevName() + ", "
                        + getBrand() + ", "
                        + getModelProduct()
                        + "}";

                return output;
        }
}
