package it.richkmeli.rms.data.entity.rmc;

import it.richkmeli.jframework.auth.data.exception.AuthDatabaseException;
import it.richkmeli.rms.data.entity.rmc.model.Rmc;
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
    public Rmc addRMC(Rmc client) throws AuthDatabaseException {
        return rmcRepository.save(client);
    }

    @Override
    public Rmc editRMC(Rmc client) throws AuthDatabaseException {
        return rmcRepository.save(client);
    }

    @Override
    public void removeRMCs(String associatedUser) throws AuthDatabaseException {
        rmcRepository.deleteAllByAssociatedUser_Email(associatedUser);
    }

    @Override
    public void removeRMC(String rmcId) throws AuthDatabaseException {
        rmcRepository.deleteRmcByRmcId(rmcId);
    }

    @Override
    public void removeRmcUserPair(Rmc client) throws AuthDatabaseException {
        rmcRepository.deleteRmcByRmcIdAndAssociatedUser_Email(client.getRmcId(),client.getAssociatedUser());
    }

    @Override
    public boolean checkRmcUserPair(Rmc client) throws AuthDatabaseException {
        return rmcRepository.existsRmcByRmcIdAndAssociatedUser_Email(client.getRmcId(),client.getAssociatedUser());
    }

    @Override
    public boolean checkRmc(String rmcID) throws AuthDatabaseException {
        return rmcRepository.existsRmcByRmcId(rmcID);
    }

    @Override
    public List<Rmc> getRMCs(String associatedUser) throws AuthDatabaseException {
        return rmcRepository.findAllByAssociatedUser_Email(associatedUser);
    }

    @Override
    public List<Rmc> getAssociatedUsers(String rmcId) throws AuthDatabaseException {
        return rmcRepository.findAllByRmcId(rmcId);
    }

    @Override
    public List<Rmc> getAllRMCs() throws AuthDatabaseException {
        return rmcRepository.findAll();
    }

    @Override
    public List<Rmc> getUnassociatedRmcs(String rmcID) throws AuthDatabaseException {
        return rmcRepository.findAllByAssociatedUserIsNull();
    }
}
