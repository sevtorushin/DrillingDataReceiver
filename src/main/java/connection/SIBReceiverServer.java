package connection;

import exceptions.DisconnectedException;

import java.io.IOException;

public class SIBReceiverServer extends AbstractServer{
    private final int DEFAULT_BUFFER_SIZE = 44;

    public SIBReceiverServer(int port) throws IOException, DisconnectedException {
        super(port);
        buf = new byte[DEFAULT_BUFFER_SIZE];
        receiveBytes();
    }

    /**
     * Использовать так же для пропуска 1 байта в начале передачи по протоколу Sib Monitor
     * @param bufferSize
     * @return Buffer of bytes
     */
    public byte[] receiveBytes(int bufferSize) {
        try {
            buf = new byte[bufferSize];
            getInpStrm().read(buf);
            getInpStrm().skip(getInpStrm().available());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buf;
    }
}
