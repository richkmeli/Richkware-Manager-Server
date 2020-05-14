package it.richkmeli.rms.data.entity.rmc;

import it.richkmeli.rms.data.entity.rmc.model.Rmc;
import it.richkmeli.rms.data.entity.rmc.model.RmcId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RmcRepository extends JpaRepository<Rmc, RmcId> {

    void deleteRmcByRmcIdAndAssociatedUser_Email(String rmcId, String associatedUser);
    void deleteRmcByRmcId(String rmcId);
    void deleteAllByAssociatedUser_Email(String associatedUser);
    boolean existsRmcByRmcIdAndAssociatedUser_Email(String rmcId, String associatedUser);
    boolean existsRmcByRmcId(String rmcId);
    List<Rmc> findAllByAssociatedUser_Email(String associatedUser);
    List<Rmc> findAllByRmcId(String rmcId);
    List<Rmc> findAllByAssociatedUserIsNull();

}
