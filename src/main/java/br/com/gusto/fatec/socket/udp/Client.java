package br.com.gusto.fatec.socket.udp;

import br.com.gusto.fatec.socket.udp.utils.GeneralException;
import br.com.gusto.fatec.socket.udp.utils.ValidPackets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.file.Paths;

public class Client {

    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) {
        var data = converteImagemEmArrayDeBytes();
        enviaPacote(data);
    }

    private static byte[] converteImagemEmArrayDeBytes() {
        try (var output = new ByteArrayOutputStream()) {
            var img = Paths.get("src", "main", "resources", "img", "java.png").toFile();
            var read = ImageIO.read(img);
            ImageIO.write(read, "jpg", output);
            return output.toByteArray();
        } catch (IOException e) {
            throw new GeneralException("Erro ao converter a imagem", e);
        }
    }

    private static void enviaPacote(byte[] data) {
        try (var socket = new DatagramSocket()) {
            var address = InetAddress.getLocalHost();
            var send = new DatagramPacket(data, data.length, address, 15678);
            var checksum = ValidPackets.valid(data);
            LOGGER.info("[Servidor] {}:15678", address); // dados do envio
            LOGGER.info("Enviando imagem - Checksum: {}", checksum); // checksum para validar pacote
            socket.send(send); // enviar pacote
        } catch (Exception e) {
            throw new GeneralException("Erro ao enviar o pacote", e);
        }
    }

}
