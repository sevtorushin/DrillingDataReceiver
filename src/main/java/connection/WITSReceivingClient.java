package connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class WITSReceivingClient extends AbstractClient {
    private final int DEFAULT_BUFFER_SIZE = 512;

    public WITSReceivingClient(String hostName, int port) throws IOException {
        super(hostName, port);
        buf = new byte[DEFAULT_BUFFER_SIZE];
    }

    @Override
    public byte[] receiveBytes() {
        try {
            int count = 0;
            while (true) {
                byte b = (byte) getInpStrm().read();
                if (b != 33) {
                    buf[count] = b;
                    count++;
                } else {
                    for (int i = 0; i < 3; i++) {
                        buf[count] = b;
                        count++;
                        b = (byte) getInpStrm().read();
                    }
                    buf[count] = 10;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buf;
    }

    public String getTextData() {
        return new String(receiveBytes(), StandardCharsets.UTF_8);
    }
}
