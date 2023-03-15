package connection;

import java.io.*;
import java.net.Socket;

public class TransferClient implements Closeable {
    private String hostName;
    private int port;
    private Socket clientSocket;
    private OutputStream os;

    public TransferClient(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
        try {
            this.clientSocket = new Socket(hostName, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public OutputStream getOutputStreamToServer() {
        if (!clientSocket.isClosed())
            try {
                os = clientSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return os;
    }

    public static void transferFromTo(InputStream inp, OutputStream out) {
        try (InputStream is = inp; OutputStream os = out){
            is.transferTo(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void transferFromTo(Object ob, File file) {
//        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
//            oos.writeObject(ob);
//            oos.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void transferFromTo(Object ob, OutputStream out) {
//        try (ObjectOutputStream oos = new ObjectOutputStream(out)) {
//            oos.writeObject(ob);
//            oos.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void transferFromTo(Object ob, Socket socket) {
//        try (ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
//            oos.writeObject(ob);
//            oos.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void transferFromTo(byte[] data, File file) {
//        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
//            bos.write(data);
//            bos.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void transferFromTo(byte[] data, OutputStream out) {
//        try (OutputStream os = out){
//            os.write(data);
//            os.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void transferFromTo(byte[] data, Socket socket) {
//        try (OutputStream os = socket.getOutputStream()){
//            os.write(data);
//            os.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void close() throws IOException {
        if (os != null)
            os.close();
        if (clientSocket != null)
            clientSocket.close();
    }
}
