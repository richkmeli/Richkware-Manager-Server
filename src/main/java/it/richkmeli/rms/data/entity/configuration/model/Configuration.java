package it.richkmeli.rms.data.entity.configuration.model;

import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.util.Date;

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
