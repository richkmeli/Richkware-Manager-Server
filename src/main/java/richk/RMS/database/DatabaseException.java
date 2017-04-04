package richk.RMS.database;

import richk.RMS.model.ModelException;

@SuppressWarnings("serial")
public class DatabaseException extends ModelException {

    public DatabaseException(Exception exception) {
        super(exception);
    }

}