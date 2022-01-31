package br.com.gusto.fatec.socket.udp.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ValidPackets {
    private ValidPackets() {}

    public static String valid(byte[] data) {
        try {
            StringBuilder hash = new StringBuilder();

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data);

            for (byte b : md.digest()) {
                hash.append(String.format("%02X", b));
            }

            return hash.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new GeneralException("Erro ao validar o pacote", e);
        }
    }
}
