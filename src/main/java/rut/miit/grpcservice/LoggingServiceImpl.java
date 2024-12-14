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
        String logEntry = String.format("Action: %s, Entity: %s[%s], Description: %s, Time: %s",
                request.getAction(), request.getEntityType(), request.getEntityId(),
                request.getDescription(), request.getTimestamp());

        logger.info(logEntry);

        if ("Stats".equalsIgnoreCase(request.getAction())) {
            DailyReportGenerator reportGenerator = new DailyReportGenerator();
            reportGenerator.generateReport(request.getDescription(), request.getTimestamp());

            LogResponse response = LogResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Report generated and action logged successfully")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            LogResponse response = LogResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Action logged successfully")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}
