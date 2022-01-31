package br.com.gusto.fatec.socket.udp.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeneralException extends RuntimeException {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralException.class);

    public GeneralException(String message, Throwable cause) {
        super(message, cause);
        LOGGER.error(message, cause);
    }
}
