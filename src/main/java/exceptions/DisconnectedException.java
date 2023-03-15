package exceptions;

import java.io.IOException;

public class DisconnectedException extends Throwable {

    public DisconnectedException(String message) {
        super(message);
    }
}
