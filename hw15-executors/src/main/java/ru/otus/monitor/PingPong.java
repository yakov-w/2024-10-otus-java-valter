package ru.otus.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingPong {
    private static final Logger logger = LoggerFactory.getLogger(PingPong.class);
    private String last = "two";
    private int cont = 0;
    private boolean inc = true;

    private synchronized void action(String message) {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                while (last.equals(message)) {
                    this.wait();
                }
                if (message.equals("one")) {
                    if (cont >= 10) {
                        inc = false;
                    } else if (cont <= 1) {
                        inc = true;
                    }
                    cont = inc ? cont + 1 : cont - 1;
                }

                logger.info("{}", cont);
                last = message;
                sleep();
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        PingPong pingPong = new PingPong();
        new Thread(() -> pingPong.action("one")).start();
        new Thread(() -> pingPong.action("two")).start();
    }

    private static void sleep() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
