package ru.otus.protobuf.service;

import io.grpc.stub.StreamObserver;
import java.util.concurrent.atomic.AtomicLong;
import ru.otus.protobuf.CalculateServiceGrpc;
import ru.otus.protobuf.Empty;
import ru.otus.protobuf.NumberMessage;
import ru.otus.protobuf.RangeMessage;

@SuppressWarnings({"squid:S2142"})
public class CalculateServiceImpl extends CalculateServiceGrpc.CalculateServiceImplBase {

    private final AtomicLong begin = new AtomicLong(0);
    private final AtomicLong end = new AtomicLong(0);

    @Override
    public void initRange(RangeMessage request, StreamObserver<NumberMessage> responseObserver) {
        begin.set(request.getBegin());
        end.set(request.getEnd());
        responseObserver.onNext(id2NumberMessage(begin.get()));
        responseObserver.onCompleted();
    }

    @Override
    public void getNumber(Empty request, StreamObserver<NumberMessage> responseObserver) {

        while (begin.get() != end.get()) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            responseObserver.onNext(id2NumberMessage(begin.incrementAndGet()));
        }

        responseObserver.onCompleted();
    }

    private NumberMessage id2NumberMessage(long id) {
        return NumberMessage.newBuilder().setId(id).build();
    }
}
