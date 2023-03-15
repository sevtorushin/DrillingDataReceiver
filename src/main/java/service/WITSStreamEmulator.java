package service;

import exceptions.BuildObjectException;

import java.io.*;

public class WITSStreamEmulator {
    private byte[] buf;
    private int mark;

    public WITSStreamEmulator(File file) {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            buf = bis.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] buildBinaryObject() throws BuildObjectException {
        byte[] localBuf = new byte[512];
        int count = 0;
        for (int i = mark; ; i++) {
            if (i==buf.length) throw new BuildObjectException("Empty object. End of file reached");
            if (buf[i] != 38)
                mark++;
            else break;
        }
        for (int i = mark; i < mark + 512; i++) {
            if (i==buf.length) throw new BuildObjectException("Empty object. End of file reached");
            if (buf[i] == 33) {
                for (int j = 0; j < 4; j++) {
                    localBuf[count] = buf[i + j];
                    count++;
                    mark = i + j;
                }
                break;
            } else {
                localBuf[count] = buf[i];
                count++;
            }
        }
        return localBuf;
    }
}
