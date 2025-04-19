package ru.otus.protobuf;

import io.grpc.ServerBuilder;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.service.CalculateServiceImpl;

@SuppressWarnings({"squid:S106"})
public class GRPCServer {
    private static final Logger log = LoggerFactory.getLogger(GRPCServer.class);

    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {
        var remoteDBService = new CalculateServiceImpl();

        var server =
                ServerBuilder.forPort(SERVER_PORT).addService(remoteDBService).build();
        server.start();
        log.info("server waiting for client connections...");
        server.awaitTermination();
    }
}
