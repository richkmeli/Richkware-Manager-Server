package it.richkmeli.rms.data.model.rmc;


import it.richkmeli.jframework.orm.DatabaseException;
import it.richkmeli.jframework.orm.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class RmcDatabaseJframeworkManager extends DatabaseManager implements RmcDatabaseModel {

    public RmcDatabaseJframeworkManager() throws DatabaseException {
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
    public Rmc addRMC(Rmc client) throws DatabaseException {
        if (create(client)) {
            return client;
        } else {
            return null;
        }
    }

    @Override
    public Rmc editRMC(Rmc client) throws DatabaseException {
        //if (
                removeRmcUserPair(new Rmc("", client.getRmcId()));
        //) {
            return addRMC(client);
            //return false;
            //TODO: Fix update -> Anche le chiavi primarie devono essere modificabili
            // return update(client);
       /* } else {
            return null;
        }*/
    }

    @Override
    public void removeRMCs(String associatedUser) throws DatabaseException {
        List<Rmc> rmcs = getRMCs(associatedUser);
        boolean response = true;
        for (Rmc rmc : rmcs) {
            if (!delete(rmc)) {
                response = false;
            }
        }
        //return response;
    }

    @Override
    public void removeRMC(String rmcId) throws DatabaseException {
        List<Rmc> rmcs = getAssociatedUsers(rmcId);
        boolean response = true;
        for (Rmc rmc : rmcs) {
            //  if (!
            removeRmcUserPair(rmc);
            //  ) {
            //      response = false;
        }
    }
    //return response;
    //}

    @Override
    public void removeRmcUserPair(Rmc client) throws DatabaseException {
        delete(client);
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