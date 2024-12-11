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

    // Запуск gRPC сервера при старте Spring Boot приложения
    @Bean
    public CommandLineRunner startGrpcServer() {
        return args -> {
            server = ServerBuilder.forPort(9090)
                    .addService(new LoggingServiceImpl())  // Подключаем сервис
                    .build();

            // Запускаем gRPC сервер
            server.start();
            System.out.println("gRPC server started on port 9090");

            // Делаем так, чтобы приложение не завершалось
            server.awaitTermination();
        };
    }

    // Остановка gRPC сервера при завершении приложения
    @PreDestroy
    public void stopGrpcServer() {
        if (server != null) {
            System.out.println("Shutting down gRPC server...");
            server.shutdown();
        }
    }
}
