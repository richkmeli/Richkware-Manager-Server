package it.richkmeli.rms.data.model.rmc;


import it.richkmeli.jframework.orm.DatabaseException;

import java.util.List;

public interface _RmcModel {

    boolean addRMC(Rmc client) throws DatabaseException;

    boolean editRMC(Rmc client) throws DatabaseException;

    boolean removeRMCs(String associatedUser) throws DatabaseException;

    boolean removeRMC(String rmcId) throws DatabaseException;

    boolean removeRmcUserPair(Rmc client) throws DatabaseException;

    boolean checkRmcUserPair(Rmc client) throws DatabaseException;

    boolean checkRmc(String rmcID) throws DatabaseException;

    List<Rmc> getRMCs(String user) throws DatabaseException;

    List<Rmc> getAssociatedUsers(String rmcId) throws DatabaseException;

    List<Rmc> getAllRMCs() throws DatabaseException;

    public List<String> getUnassociatedRmcs(String rmcID) throws DatabaseException;

}
