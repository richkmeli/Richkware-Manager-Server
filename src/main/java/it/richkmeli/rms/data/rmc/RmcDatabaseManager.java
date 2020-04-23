package it.richkmeli.rms.data.rmc;


import it.richkmeli.jframework.orm.DatabaseException;
import it.richkmeli.jframework.orm.DatabaseManager;
import it.richkmeli.rms.data.rmc.model.Rmc;
import it.richkmeli.rms.data.rmc.model.RmcModel;

import java.util.ArrayList;
import java.util.List;

public class RmcDatabaseManager extends DatabaseManager implements RmcModel {

    public RmcDatabaseManager() throws DatabaseException {
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
    public boolean addRMC(Rmc client) throws DatabaseException {
        return create(client);
    }

    @Override
    public boolean editRMC(Rmc client) throws DatabaseException {
        if (removeRmcUserPair(new Rmc("", client.getRmcId())))
            return addRMC(client);
        return false;
        //TODO: Fix update -> Anche le chiavi primarie devono essere modificabili
        // return update(client);
    }

    @Override
    public boolean removeRMCs(String associatedUser) throws DatabaseException {
        List<Rmc> rmcs = getRMCs(associatedUser);
        boolean response = true;
        for (Rmc rmc : rmcs) {
            if (!delete(rmc)) {
                response = false;
            }
        }
        return response;
    }

    @Override
    public boolean removeRMC(String rmcId) throws DatabaseException {
        List<Rmc> rmcs = getAssociatedUsers(rmcId);
        boolean response = true;
        for (Rmc rmc : rmcs) {
            if (!removeRmcUserPair(rmc)) {
                response = false;
            }
        }
        return response;
    }

    @Override
    public boolean removeRmcUserPair(Rmc client) throws DatabaseException {
        return delete(client);
    }

    @Override
    public boolean checkRmcUserPair(Rmc client) throws DatabaseException {
        return read(client) != null;
    }

    @Override
    public boolean checkRmc(String rmcID) throws DatabaseException {
//        return read(new RMC("", rmcID)) != null;
        return getAssociatedUsers(rmcID).size() > 0;
    }


    public List<Rmc> getRMCs() throws DatabaseException {
        return getRMCs("");
    }

    @Override
    public List<Rmc> getAssociatedUsers(String rmcId) throws DatabaseException {
        List<Rmc> rmcs = readAll(Rmc.class);
        if (rmcs != null) {
            // filter user rmcs
            List<Rmc> userRmcs = new ArrayList<>();
            for (Rmc rmc : rmcs) {
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
    public List<Rmc> getRMCs(String user) throws DatabaseException {
        List<Rmc> rmcs = readAll(Rmc.class);
        if (rmcs != null) {
            // filter user rmcs
            List<Rmc> userRmcs = new ArrayList<>();
            for (Rmc rmc : rmcs) {
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
    public List<Rmc> getAllRMCs() throws DatabaseException {
        return readAll(Rmc.class);
    }

    @Override
    public List<String> getUnassociatedRmcs(String rmcID) throws DatabaseException {
        List<Rmc> rmcs = getRMCs("");
        List<String> user = new ArrayList<>();
        for (Rmc rmc : rmcs) {
            user.add(rmc.getAssociatedUser());
        }
        return user;
    }

}