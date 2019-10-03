package it.richkmeli.rms.data.rmc.model;


import it.richkmeli.jframework.orm.DatabaseException;

import java.util.List;

public interface RMCModel {

    boolean addRMC(RMC client) throws DatabaseException;

    boolean editRMC(RMC client) throws DatabaseException;

    boolean removeRMCs(String associatedUser) throws DatabaseException;

    boolean removeRMC(String rmcId) throws DatabaseException;

    boolean removeRmcUserPair(RMC client) throws DatabaseException;

    boolean checkRmcUserPair(RMC client) throws DatabaseException;

    boolean checkRmc(String rmcID) throws DatabaseException;

    List<RMC> getRMCs(String user) throws DatabaseException;

    List<RMC> getAssociatedUsers(String rmcId) throws DatabaseException;

    List<RMC> getAllRMCs() throws DatabaseException;

    public List<String> getUnassociatedRmcs(String rmcID) throws DatabaseException;

}
