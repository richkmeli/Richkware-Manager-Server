package it.richkmeli.rms.data.model.rmc;


import it.richkmeli.jframework.orm.DatabaseException;

import java.util.List;

public interface RmcDatabaseModel {

    Rmc addRMC(Rmc client) throws DatabaseException;

    Rmc editRMC(Rmc client) throws DatabaseException;

    void removeRMCs(String associatedUser) throws DatabaseException;

    void removeRMC(String rmcId) throws DatabaseException;

    void removeRmcUserPair(Rmc client) throws DatabaseException;

    boolean checkRmcUserPair(Rmc client) throws DatabaseException;

    boolean checkRmc(String rmcID) throws DatabaseException;

    List<Rmc> getRMCs(String user) throws DatabaseException;

    List<Rmc> getAssociatedUsers(String rmcId) throws DatabaseException;

    List<Rmc> getAllRMCs() throws DatabaseException;

    public List<String> getUnassociatedRmcs(String rmcID) throws DatabaseException;

}
