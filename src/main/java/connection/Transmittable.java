package connection;

import java.io.Closeable;
import java.io.IOException;

public interface Transmittable {
    byte[] receiveBytes() throws IOException;
    void sendBytes(byte[] bytes) throws IOException;
}
