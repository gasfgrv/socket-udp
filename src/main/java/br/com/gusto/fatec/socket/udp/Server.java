package br.com.gusto.fatec.socket.udp;

import br.com.gusto.fatec.socket.udp.utils.GeneralException;
import br.com.gusto.fatec.socket.udp.utils.ValidPackets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.file.Paths;

public class Server {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket(15678)) {
            LOGGER.info("Socket ouvindo na porta 15678"); //Socket servidor
            conectarComCliente(socket);
        } catch (IOException e) {
            throw new GeneralException("Erro ao iniciar o servidor", e);
        }
    }

    private static void conectarComCliente(DatagramSocket socket) {
        DatagramPacket receiver = receberPacote(socket);
        logDadosCliente(receiver);
        converterArrayDeBytesEmImagem(receiver);
    }

    private static DatagramPacket receberPacote(DatagramSocket socket) {
        try {
            byte[] buffer = new byte[14274]; // buffer para receber o arquivo, 14274 é o tamanho da imagem
            DatagramPacket receiver = new DatagramPacket(buffer, buffer.length); //receber o pacote
            socket.receive(receiver);
            logChecksum(buffer);
            return receiver;
        } catch (IOException e) {
            throw new GeneralException("Erro ao receber o pacote", e);
        }
    }

    private static void logDadosCliente(DatagramPacket receiver) {
        String clientData = String.format("[Cliente]: %s:%d", receiver.getAddress(), receiver.getPort());
        LOGGER.info(clientData);
    }

    private static void logChecksum(byte[] buffer) {
        String msg = String.format("Imagem recebida - Checksum: %s", ValidPackets.valid(buffer));
        LOGGER.info(msg);
    }

    private static void converterArrayDeBytesEmImagem(DatagramPacket receiver) {
        try {
            ByteArrayInputStream input = new ByteArrayInputStream(receiver.getData());
            BufferedImage bImage2 = ImageIO.read(input);

            File img = Paths.get("src", "main", "resources", "img", "javaCopy.jpg").toFile();

            String mgsGenerateImage = String.format("Gerando imagem: %s", img.getAbsolutePath());
            LOGGER.info(mgsGenerateImage);

            ImageIO.write(bImage2, "jpg", img);

            String mgsGeneratedImage = String.format("%s gerada", img.getAbsolutePath());
            LOGGER.info(mgsGeneratedImage);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

}
