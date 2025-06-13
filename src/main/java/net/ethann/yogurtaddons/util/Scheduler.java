package net.ethann.yogurtaddons.util;

public class Scheduler {
    private long timeOver = Long.MAX_VALUE;
    public Scheduler() {

    }

    public void schedule(long t) {
        timeOver = System.currentTimeMillis() + t;
    }

    public long timeLeft() {
        return timeOver - System.currentTimeMillis();
    }

    public boolean isOver() {
        return System.currentTimeMillis() >= timeOver;
    }
}
