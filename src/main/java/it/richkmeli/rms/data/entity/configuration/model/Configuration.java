package it.richkmeli.rms.data.entity.configuration.model;

import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Configuration {
    @Id
    @NotNull
    @Length(max = 100)
    private String cKey;
    @Length(max = 1000)
    private String value;

    public Configuration() {
    }

    public Configuration(@NotNull @Length(max = 100) String cKey, @Length(max = 1000) String value) {
        this.cKey = cKey;
        this.value = value;
    }

    public String getKey() {
        return cKey;
    }

    public void setKey(String key) {
        this.cKey = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
