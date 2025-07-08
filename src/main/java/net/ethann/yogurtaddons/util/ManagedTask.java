package net.ethann.yogurtaddons.util;

public abstract class ManagedTask extends Task {
    @Override
    protected boolean shouldRegisterTask() {
        return true;
    }
}
