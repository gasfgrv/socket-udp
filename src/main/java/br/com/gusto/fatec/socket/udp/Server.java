package br.com.gusto.fatec.socket.udp;

import br.com.gusto.fatec.socket.udp.utils.GeneralException;
import br.com.gusto.fatec.socket.udp.utils.ValidPackets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.file.Paths;

public class Server {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        try (var socket = new DatagramSocket(15678)) {
            LOGGER.info("Socket ouvindo na porta 15678"); //Socket servidor
            conectarComCliente(socket);
        } catch (IOException e) {
            throw new GeneralException("Erro ao iniciar o servidor", e);
        }
    }

    private static void conectarComCliente(DatagramSocket socket) {
        var receiver = receberPacote(socket);
        LOGGER.info("[Cliente]: {}:{}", receiver.getAddress(), receiver.getPort());
        converterArrayDeBytesEmImagem(receiver);
    }

    private static DatagramPacket receberPacote(DatagramSocket socket) {
        try {
            var buffer = new byte[14274]; // buffer para receber o arquivo, 14274 é o tamanho da imagem
            var receiver = new DatagramPacket(buffer, buffer.length); //receber o pacote
            var checksum = ValidPackets.valid(buffer);
            socket.receive(receiver);
            LOGGER.info("Imagem recebida - Checksum: {}}", checksum);
            return receiver;
        } catch (IOException e) {
            throw new GeneralException("Erro ao receber o pacote", e);
        }
    }

    private static void converterArrayDeBytesEmImagem(DatagramPacket receiver) {
        try (var input = new ByteArrayInputStream(receiver.getData())) {
            var bImage2 = ImageIO.read(input);
            var img = Paths.get("src", "main", "resources", "img", "javaCopy.jpg").toFile();
            LOGGER.info("Gerando imagem: {}", img.getAbsolutePath());
            ImageIO.write(bImage2, "jpg", img);
            LOGGER.info("{} gerada", img.getAbsolutePath());
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

}
