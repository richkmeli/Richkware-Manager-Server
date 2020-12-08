package it.richkmeli.rms.data.entity.configuration;

public enum ConfigurationEnum {
    DEFAULT_CONFIGURATION_1("dafault_value_1"),
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
