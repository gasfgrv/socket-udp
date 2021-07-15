package br.com.gusto.fatec.socket.udp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static br.com.gusto.fatec.socket.udp.utils.ValidPackets.valid;
import static java.lang.String.format;
import static javax.imageio.ImageIO.read;
import static javax.imageio.ImageIO.write;

public class Client {
    private static final Logger LOGGER = Logger.getLogger(Client.class.getSimpleName());

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        var img = new File("img/java.png");
        byte[] data = converteImagemEmArrayDeBytes(img);
        enviaPacote(data);
    }

    private static byte[] converteImagemEmArrayDeBytes(File img) throws IOException {
        var output = new ByteArrayOutputStream();
        write(read(img), "jpg", output);
        byte[] data = output.toByteArray();
        return data;
    }

    private static void enviaPacote(byte[] data) {
        try (var socket = new DatagramSocket()) {
            var address = InetAddress.getLocalHost();
            var send = new DatagramPacket(data, data.length, address, 15678);
            var dataSend = format("[Servidor] %s:15678", address);
            LOGGER.info(dataSend); // dados do envio
            LOGGER.info(format("Enviando imagem - Checksum: %s", valid(data))); // checksum para validar pacote
            socket.send(send); // enviar pacote
        } catch (Exception e) {
            LOGGER.warning(e.getMessage());
        }
    }

}
