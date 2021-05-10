package br.com.gusto.fatec.socket.udp.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ValidPackets {
    private ValidPackets() {}

    public static String valid(byte[] data) throws NoSuchAlgorithmException {
        var hash = new StringBuilder();
        var md = MessageDigest.getInstance("MD5");
        md.update(data);
        for (byte b : md.digest()) hash.append(String.format("%02X", b));
        return hash.toString();
    }
}
