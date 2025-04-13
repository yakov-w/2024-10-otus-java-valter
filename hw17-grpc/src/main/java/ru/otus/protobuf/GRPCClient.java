package ru.otus.protobuf;

import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"squid:S106", "squid:S2142"})
public class GRPCClient {
    private static final Logger log = LoggerFactory.getLogger(GRPCClient.class);

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;

    private static AtomicLong serverVal = new AtomicLong(0L);

    public static void main(String[] args) throws InterruptedException {
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        var stub = CalculateServiceGrpc.newBlockingStub(channel);
        stub.initRange(RangeMessage.newBuilder().setBegin(0L).setEnd(30L).build());

        log.info("numbers Client is starting...");

        var latch = new CountDownLatch(1);
        var newStub = CalculateServiceGrpc.newStub(channel);
        newStub.getNumber(Empty.getDefaultInstance(), new StreamObserver<NumberMessage>() {
            @Override
            public void onNext(NumberMessage nm) {
                serverVal.set(nm.getId());
                log.info("\tnew value:{}", serverVal.get());
            }

            @Override
            public void onError(Throwable t) {
                log.error("", t);
            }

            @Override
            public void onCompleted() {
                log.info("\n\nЯ все!");
                latch.countDown();
            }
        });

        long currentValue = 0L;
        long prevVal = 0L;
        for (int i = 0; i < 50; i++) {
            long tmp = serverVal.get();
            if (prevVal != tmp) {
                prevVal = tmp;
                currentValue += prevVal;
            }
            log.info("currentValue:{}", ++currentValue);
            Thread.sleep(1000L);
        }

        latch.await();

        channel.shutdown();
    }
}
