package it.richkmeli.RMS.database;

import it.richkmeli.RMS.model.ModelException;

@SuppressWarnings("serial")
public class DatabaseException extends ModelException {

    public DatabaseException(Exception exception) {
        super(exception);
    }

}