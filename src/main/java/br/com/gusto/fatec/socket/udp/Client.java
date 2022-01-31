package br.com.gusto.fatec.socket.udp;

import br.com.gusto.fatec.socket.udp.utils.GeneralException;
import br.com.gusto.fatec.socket.udp.utils.ValidPackets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.file.Paths;

public class Client {

    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) {
        byte[] data = converteImagemEmArrayDeBytes();
        enviaPacote(data);
    }

    private static byte[] converteImagemEmArrayDeBytes() {
        try {
            File img = Paths.get("src","main","resources", "img", "java.png").toFile();
            BufferedImage read = ImageIO.read(img);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(read, "jpg", output);
            return output.toByteArray();
        } catch (IOException e) {
            throw new GeneralException("Erro ao converter a imagem", e);
        }
    }

    private static void enviaPacote(byte[] data) {
        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress address = InetAddress.getLocalHost();
            DatagramPacket send = new DatagramPacket(data, data.length, address, 15678);

            String dataSend = String.format("[Servidor] %s:15678", address);
            LOGGER.info(dataSend); // dados do envio


            String format = String.format("Enviando imagem - Checksum: %s", ValidPackets.valid(data)); // checksum para validar pacote
            LOGGER.info(format);

            socket.send(send); // enviar pacote
        } catch (Exception e) {
            throw new GeneralException("Erro ao enviar o pacote", e);
        }
    }

}
