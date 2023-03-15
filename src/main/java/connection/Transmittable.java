package connection;

import exceptions.DisconnectedException;

import java.io.Closeable;
import java.io.IOException;

public interface Transmittable {
    byte[] receiveBytes() throws IOException, DisconnectedException;
    void sendBytes(byte[] bytes) throws IOException;
}
