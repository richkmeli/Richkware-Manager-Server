package it.richkmeli.rms.web.v2.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String email) {
        super("Could not find user " + email);
    }
}
