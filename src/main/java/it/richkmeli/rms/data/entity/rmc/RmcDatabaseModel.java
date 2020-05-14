package it.richkmeli.rms.data.entity.rmc;

import it.richkmeli.jframework.auth.data.exception.AuthDatabaseException;
import it.richkmeli.rms.data.entity.rmc.model.Rmc;

import java.util.List;

public interface RmcDatabaseModel {

    Rmc addRMC(Rmc client) throws AuthDatabaseException;

    Rmc editRMC(Rmc client) throws AuthDatabaseException;

    void removeRMCs(String associatedUser) throws AuthDatabaseException;

    void removeRMC(String rmcId) throws AuthDatabaseException;

    void removeRmcUserPair(Rmc client) throws AuthDatabaseException;

    boolean checkRmcUserPair(Rmc client) throws AuthDatabaseException;

    boolean checkRmc(String rmcID) throws AuthDatabaseException;

    List<Rmc> getRMCs(String user) throws AuthDatabaseException;

    List<Rmc> getAssociatedUsers(String rmcId) throws AuthDatabaseException;

    List<Rmc> getAllRMCs() throws AuthDatabaseException;

    public List<Rmc> getUnassociatedRmcs(String rmcID) throws AuthDatabaseException;

}
