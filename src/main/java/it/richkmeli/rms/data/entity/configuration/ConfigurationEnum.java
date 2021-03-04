package it.richkmeli.rms.data.entity.configuration;

public enum ConfigurationEnum {
    MAPBOX_ACCESS_TOKEN("REPLACE_WITH_API_TOKEN"),
    DEFAULT_CONFIGURATION_2("dafault_value_2"),
    DEFAULT_CONFIGURATION_3("dafault_value_3");


    private String key;
    private String defaultValue;

    ConfigurationEnum(String defaultValue) {
        this.key = this.name();
        this.defaultValue = defaultValue;
    }

    ConfigurationEnum(String key, String defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public String getKey() {
        return key;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

}
