package richk.RMS;

import richk.RMS.database.DatabaseException;
import richk.RMS.database.DatabaseManager;

public class Session {
    protected DatabaseManager databaseManager;

    public Session() throws DatabaseException {
        databaseManager = new DatabaseManager();
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }


}
