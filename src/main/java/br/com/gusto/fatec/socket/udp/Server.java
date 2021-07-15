package br.com.gusto.fatec.socket.udp;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import static br.com.gusto.fatec.socket.udp.utils.ValidPackets.valid;
import static java.lang.String.format;
import static javax.imageio.ImageIO.read;
import static javax.imageio.ImageIO.write;

public class Server {
    private static final Logger LOGGER = Logger.getLogger(Server.class.getSimpleName());

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        var img = new File("img/javacopy.png"); // arquivo destino
        LOGGER.info("Socket ouvindo na porta 15678"); //Socket servidor

        try (var socket = new DatagramSocket(15678)) {
            conectarComCliente(img, socket);
        }
    }

    private static void conectarComCliente(File img, DatagramSocket socket) throws IOException, NoSuchAlgorithmException {
        var buffer = new byte[14274]; // buffer para receber o arquivo, 14274 é o tamanho da imagem
        DatagramPacket receiver = receberPacote(socket, buffer);
        logDadosCliente(receiver);
        logChecksum(buffer);
        converterArrayDeBytesEmImagem(img, receiver);
    }

    private static DatagramPacket receberPacote(DatagramSocket socket, byte[] buffer) throws IOException {
        var receiver = new DatagramPacket(buffer, buffer.length); //receber o pacote
        socket.receive(receiver);
        return receiver;
    }

    private static void logDadosCliente(DatagramPacket receiver) {
        var clientData = format("[Cliente]: %s:%d", receiver.getAddress(), receiver.getPort());
        LOGGER.info(clientData);
    }

    private static void logChecksum(byte[] buffer) throws NoSuchAlgorithmException {
        LOGGER.info(format("Imagem recebida - Checksum: %s", valid(buffer)));
    }

    private static void converterArrayDeBytesEmImagem(File img, DatagramPacket receiver) throws IOException {
        byte[] buffer;
        var mgsGenerateImage = format("Gerando imagem: %s", img.getAbsolutePath());
        LOGGER.info(mgsGenerateImage);

        buffer = receiver.getData();
        var input = new ByteArrayInputStream(buffer);
        var bImage2 = read(input);

        write(bImage2, "jpg", img);

        var mgsGeneratedImage = format("%s gerada", img.getAbsolutePath());
        LOGGER.info(mgsGeneratedImage);
    }

}
