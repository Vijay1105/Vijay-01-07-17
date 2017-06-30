/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diskbackedmap.classes;

/**
 *
 * @author Vijay Daswani
 */

import com.diskbackedmap.utils.DefaultObjectConverter;
import com.diskbackedmap.utils.ObjectConverter;

import java.io.Serializable;

public class Conversion {
    private static final int poly = 0x1021;

    private static final int[] crcTable = new int[256];

    static {
        // initialise scrambler table
        for (int i = 0; i < 256; i++) {
            int fcs = 0;
            int d = i << 8;
            for (int k = 0; k < 8; k++) {
                if (((fcs ^ d) & 0x8000) != 0) {
                    fcs = (fcs << 1) ^ poly;
                } else {
                    fcs = (fcs << 1);
                }
                d <<= 1;
                fcs &= 0xffff;
            }
            crcTable[i] = fcs;
        }
    }

    public static final Conversion instance = new Conversion();

    private ObjectConverter os;

    public Conversion() {
        try{
            os = new DefaultObjectConverter();
        }catch(Exception e){
            System.err.println("Unable to create hessian object convertor, using the default convertor.");
            os = new DefaultObjectConverter();
        }
    }

    public byte[] intToBytes(int n) {
        byte[] b = new byte[4];
        for (int i = 0; i < b.length; i++) {
            b[3 - i] = (byte) (n >>> (i * 8));
        }
        return b;
    }

    public byte[] longToBytes(long n) {
        byte[] b = new byte[8];
        for (int i = 0; i < b.length; i++) {
            b[b.length - 1 - i] = (byte) (n >>> (i * 8));
        }
        return b;
    }

    public int byteToInt(byte[] b) {
        return byteToInt(b, 0);
    }
    public int byteToInt(byte[] b, int offset) {
        int n = 0;
        for (int i = offset; i < offset + 4; i++) {
            n <<= 8;
            n ^= (int) b[i] & 0xFF;
        }
        return n;
    }

    public long byteToLong(byte[] b) {
        long n = 0;
        for (int i = 0; i < 8; i++) {
            n <<= 8;
            n ^= (long) b[i] & 0xFF;
        }
        return n;
    }

    public byte[] serialize(Serializable object) throws Exception {
        return os.serialize(object);
    }

    public <T> T deserialize(byte[] buffer) {
        return (T) os.deserialize(buffer);
    }

    public byte[] shortToBytes(int n) {
        return shortToBytes((short) n);
    }
    public byte[] shortToBytes(short n) {
        byte[] b = new byte[2];
        for (int i = 0; i < b.length; i++) {
            b[1 - i] = (byte) (n >>> (i * 8));
        }
        return b;
    }

    public short byteToShort(byte[] b) {
        return byteToShort(b, 0);
    }
    public short byteToShort(byte[] b, int offset) {
        short n = 0;
        for (int i = offset; i < offset + 2; i++) {
            n <<= 8;
            n ^= (int) b[i] & 0xFF;
        }
        return n;
    }

    public static short crc16(byte[] ba) {
        int work = 0xffff;
        for (byte b : ba) {
            work = (crcTable[(b ^ (work >>> 8)) & 0xff] ^ (work << 8)) &
                    0xffff;
        }
        return (short) work;
    }
}

