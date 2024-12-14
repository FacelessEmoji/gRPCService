package rut.miit.grpcservice;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import javax.annotation.PreDestroy;

@SpringBootApplication
public class GRpcServiceApplication {

    private Server server;

    public static void main(String[] args) {
        SpringApplication.run(GRpcServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner startGrpcServer() {
        return args -> {
            server = ServerBuilder.forPort(9090)
                    .addService(new LoggingServiceImpl())
                    .build();

            server.start();
            System.out.println("gRPC server started on port 9090");

            server.awaitTermination();
        };
    }

    @PreDestroy
    public void stopGrpcServer() {
        if (server != null) {
            System.out.println("Shutting down gRPC server...");
            server.shutdown();
        }
    }
}
