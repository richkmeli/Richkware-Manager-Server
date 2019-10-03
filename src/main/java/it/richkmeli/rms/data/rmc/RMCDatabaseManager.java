package it.richkmeli.rms.data.rmc;


import it.richkmeli.jframework.orm.DatabaseException;
import it.richkmeli.jframework.orm.DatabaseManager;
import it.richkmeli.rms.data.rmc.model.RMC;
import it.richkmeli.rms.data.rmc.model.RMCModel;

import java.util.ArrayList;
import java.util.List;

public class RMCDatabaseManager extends DatabaseManager implements RMCModel {

    public RMCDatabaseManager() throws DatabaseException {
        schemaName = "AuthSchema";
        tableName = schemaName + "." + "rmc";
        table = "(" +
                "associatedUser VARCHAR(50) REFERENCES AuthSchema.auth(email) ON DELETE CASCADE," +
                "rmcId VARCHAR(68) NOT NULL," +
                "PRIMARY KEY (associatedUser, rmcId)" +
                ")";

        init();
    }

    @Override
    public boolean addRMC(RMC client) throws DatabaseException {
        return create(client);
    }

    @Override
    public boolean editRMC(RMC client) throws DatabaseException {
        if (removeRmcUserPair(new RMC("", client.getRmcId())))
            return addRMC(client);
        return false;
        //TODO: Fix update -> Anche le chiavi primarie devono essere modificabili
        // return update(client);
    }

    @Override
    public boolean removeRMCs(String associatedUser) throws DatabaseException {
        List<RMC> rmcs = getRMCs(associatedUser);
        boolean response = true;
        for (RMC rmc : rmcs) {
            if (!delete(rmc)) {
                response = false;
            }
        }
        return response;
    }

    @Override
    public boolean removeRMC(String rmcId) throws DatabaseException {
        List<RMC> rmcs = getAssociatedUsers(rmcId);
        boolean response = true;
        for (RMC rmc : rmcs) {
            if (!removeRmcUserPair(rmc)) {
                response = false;
            }
        }
        return response;
    }

    @Override
    public boolean removeRmcUserPair(RMC client) throws DatabaseException {
        return delete(client);
    }

    @Override
    public boolean checkRmcUserPair(RMC client) throws DatabaseException {
        return read(client) != null;
    }

    @Override
    public boolean checkRmc(String rmcID) throws DatabaseException {
//        return read(new RMC("", rmcID)) != null;
        return getAssociatedUsers(rmcID).size() > 0;
    }


    public List<RMC> getRMCs() throws DatabaseException {
        return getRMCs("");
    }

    @Override
    public List<RMC> getAssociatedUsers(String rmcId) throws DatabaseException {
        List<RMC> rmcs = readAll(RMC.class);
        if (rmcs != null) {
            // filter user rmcs
            List<RMC> userRmcs = new ArrayList<>();
            for (RMC rmc : rmcs) {
                if (rmc.getRmcId().equalsIgnoreCase(rmcId)) {
                    userRmcs.add(rmc);
                }
            }
            return userRmcs;
        } else {
            return null;
        }
    }

    @Override
    public List<RMC> getRMCs(String user) throws DatabaseException {
        List<RMC> rmcs = readAll(RMC.class);
        if (rmcs != null) {
            // filter user rmcs
            List<RMC> userRmcs = new ArrayList<>();
            for (RMC rmc : rmcs) {
                if (rmc.getAssociatedUser().equalsIgnoreCase(user)) {
                    userRmcs.add(rmc);
                }
            }
            return userRmcs;
        } else {
            return null;
        }
    }

    @Override
    public List<RMC> getAllRMCs() throws DatabaseException {
        return readAll(RMC.class);
    }

    @Override
    public List<String> getUnassociatedRmcs(String rmcID) throws DatabaseException {
        List<RMC> rmcs = getRMCs("");
        List<String> user = new ArrayList<>();
        for (RMC rmc : rmcs) {
            user.add(rmc.getAssociatedUser());
        }
        return user;
    }

}