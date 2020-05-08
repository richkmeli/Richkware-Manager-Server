package it.richkmeli.rms.data.model.rmc;

import it.richkmeli.jframework.orm.DatabaseException;
import it.richkmeli.rms.data.model.device.DeviceDatabaseSpringManager;
import it.richkmeli.rms.data.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RmcDatabaseSpringManager implements RmcDatabaseModel {
    private static RmcRepository rmcRepository;

    public static RmcDatabaseSpringManager getInstance() {
        return new RmcDatabaseSpringManager(rmcRepository);
    }

    @Autowired
    public RmcDatabaseSpringManager(RmcRepository rmcRepository){
        this.rmcRepository = rmcRepository;
    }

    @Override
    public Rmc addRMC(Rmc client) throws DatabaseException {
        return rmcRepository.save(client);
    }

    @Override
    public Rmc editRMC(Rmc client) throws DatabaseException {
        return rmcRepository.save(client);
    }

    @Override
    public void removeRMCs(String associatedUser) throws DatabaseException {
        rmcRepository.deleteAllByAssociatedUser(associatedUser);
    }

    @Override
    public void removeRMC(String rmcId) throws DatabaseException {
        rmcRepository.deleteRmcByRmcId(rmcId);
    }

    @Override
    public void removeRmcUserPair(Rmc client) throws DatabaseException {
        rmcRepository.deleteRmcByRmcIdAndAssociatedUser(client.getRmcId(),client.getAssociatedUser());
    }

    @Override
    public boolean checkRmcUserPair(Rmc client) throws DatabaseException {
        return false;
    }

    @Override
    public boolean checkRmc(String rmcID) throws DatabaseException {
        return false;
    }

    @Override
    public List<Rmc> getRMCs(String user) throws DatabaseException {
        return null;
    }

    @Override
    public List<Rmc> getAssociatedUsers(String rmcId) throws DatabaseException {
        return null;
    }

    @Override
    public List<Rmc> getAllRMCs() throws DatabaseException {
        return null;
    }

    @Override
    public List<String> getUnassociatedRmcs(String rmcID) throws DatabaseException {
        return null;
    }
}
