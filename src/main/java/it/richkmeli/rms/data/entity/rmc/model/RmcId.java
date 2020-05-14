package it.richkmeli.rms.data.entity.rmc.model;

import java.io.Serializable;
import java.util.Objects;

public class RmcId implements Serializable {
    private String associatedUser;
    private String rmcId;

    public RmcId() {
    }

    public RmcId(String associatedUser, String rmcId) {
        this.associatedUser = associatedUser;
        this.rmcId = rmcId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RmcId rmcId1 = (RmcId) o;
        if (!associatedUser.equals(((RmcId) o).associatedUser)) return false;
        return rmcId == rmcId1.rmcId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(associatedUser, rmcId);
    }
}
