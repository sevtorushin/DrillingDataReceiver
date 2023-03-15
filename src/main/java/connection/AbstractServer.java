package connection;

import exceptions.DisconnectedException;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public abstract class AbstractServer implements Transmittable, Closeable {
    private final ServerSocket serverSocket;
    private final Socket socket;
    private InputStream inpStrm;
    private OutputStream outStrm;
    protected byte[] buf;
    private byte[] tempBuf = new byte[512];
    private final int DEFAULT_BUFFER_SIZE = 512;

    public AbstractServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        socket = serverSocket.accept();
        inpStrm = socket.getInputStream();
        outStrm = socket.getOutputStream();
        buf = new byte[DEFAULT_BUFFER_SIZE];
    }

    @Override
    public byte[] receiveBytes() throws DisconnectedException, IOException {
        inpStrm.read(buf);
        if (buf[0]==0)
            throw new DisconnectedException("Client is disconnected");
        System.arraycopy(buf, 0, tempBuf, 0, buf.length);
        Arrays.fill(buf, (byte) 0);
        return tempBuf;
    }

    @Override
    public void sendBytes(byte[] bytes) throws IOException {
            outStrm.write(bytes);
    }

    public Socket getSocket() {
        return socket;
    }

    public InputStream getInpStrm() {
        return inpStrm;
    }

    public void setInpStrm(InputStream inpStrm) {
        this.inpStrm = inpStrm;
    }

    public OutputStream getOutStrm() {
        return outStrm;
    }

    public void setOutStrm(OutputStream outStrm) {
        this.outStrm = outStrm;
    }

    @Override
    public void close() throws IOException {
        if (inpStrm != null)
            inpStrm.close();
        if (outStrm != null)
            outStrm.close();
        if (socket != null)
            socket.close();
        if (serverSocket != null)
            serverSocket.close();
    }
}
