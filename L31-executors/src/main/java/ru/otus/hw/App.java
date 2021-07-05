package ru.otus.hw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    private static final int MIN = 1;
    private static final int MAX = 10;
    private int currentInt = 1;
    private int count = 0;
    private int step;

    private String threadNameCurr = "writer-2";

    public static void main(String[] args) {
        new App().go();
    }

    private void go() {
        new Thread(this::action, "writer-1").start();
        new Thread(this::action, "writer-2").start();
    }

    private synchronized void action() {

        while (!Thread.currentThread().isInterrupted()) {
            if(count > 1) {
                count = 0;
                currentInt += step;
            }

            try {
                while (threadNameCurr.equals(Thread.currentThread().getName())) {
                    this.wait();
                }

                threadNameCurr = Thread.currentThread().getName();

                logger.info("{}: {}", threadNameCurr, currentInt);
                count++;

                if(currentInt == MIN) step = 1;
                if(currentInt == MAX) step = -1;

                notifyAll();
                sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void sleep(int seconds) {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(seconds));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}