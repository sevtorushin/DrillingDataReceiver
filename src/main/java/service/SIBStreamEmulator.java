package service;

import java.io.*;
import java.util.Arrays;

public class SIBStreamEmulator {
    private byte[] buf;
    private byte[] empty;
    private int mark;
    private int objectSize = 44;

    public SIBStreamEmulator(File file) {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            buf = bis.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        empty = new byte[objectSize];
        Arrays.fill(empty, (byte) 0);
    }

    public byte[] buildBinaryObject() {
        byte[] localBuf = new byte[objectSize];
        if (mark + objectSize > buf.length)
            return empty;
        System.arraycopy(buf, mark, localBuf, 0, objectSize);
        mark += objectSize;
        return localBuf;
    }
}
