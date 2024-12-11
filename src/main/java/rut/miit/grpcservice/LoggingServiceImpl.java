package rut.miit.grpcservice;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rut.miit.grpcservice.LoggingServiceProto.LogRequest;
import rut.miit.grpcservice.LoggingServiceProto.LogResponse;


public class LoggingServiceImpl extends LoggingServiceGrpc.LoggingServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(LoggingServiceImpl.class);

    @Override
    public void logAction(LogRequest request, StreamObserver<LogResponse> responseObserver) {
        // Формируем строку лога
        String logEntry = String.format("Action: %s, Entity: %s[%s], User: %s, Time: %s",
                request.getAction(), request.getEntityType(), request.getEntityId(),
                request.getUserId(), request.getTimestamp());

        // Логируем в файл (через Logback)
        logger.info(logEntry);

        // Отправляем ответ клиенту
        LogResponse response = LogResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Action logged successfully")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}