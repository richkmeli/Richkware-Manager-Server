package it.richkmeli.rms.data.model.rmc;

import it.richkmeli.rms.data.model.rmc.Rmc;
import it.richkmeli.rms.data.model.rmc.RmcId;
import it.richkmeli.rms.data.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RmcRepository extends JpaRepository<Rmc, RmcId> {

    void deleteRmcByRmcIdAndAssociatedUser(String rmcId, String user);
    void deleteRmcByRmcId(String rmcId);
    void deleteAllByAssociatedUser(String user);
}
