package com.abc.handoff;

import com.abc.pp.stringhandoff.*;
import com.programix.thread.*;

public class StringHandoffImpl implements StringHandoff {

    private String bin;

    public StringHandoffImpl() {
        bin = null;
    }

    @Override
    public synchronized void pass(String msg, long msTimeout)
        throws InterruptedException, TimedOutException, ShutdownException, IllegalStateException {
        long msEndTime = System.currentTimeMillis() + msTimeout;
        if (msTimeout == 0L) {
            bin = msg;
            notifyAll();
            wait();
        }
        else {
            long msRemaining = msEndTime - System.currentTimeMillis();
            //if (msRemaining < 1L) throw TimedOutException;
            wait(msRemaining);
            bin = msg;
            notifyAll();
            wait();
        }
        bin = null;
    }

    @Override
    public synchronized void pass(String msg) throws InterruptedException, ShutdownException, IllegalStateException {
        pass(msg, 0L);
    }

    @Override
    public synchronized String receive(long msTimeout)
        throws InterruptedException, TimedOutException, ShutdownException, IllegalStateException {
        long msEndTime = System.currentTimeMillis() + msTimeout;
        String msg = null;
        if (msTimeout == 0L) {
                notifyAll();
                while (bin == null) wait();
                msg = bin;
                notifyAll();
        }
        else {
            long msRemaining = msEndTime - System.currentTimeMillis();
            //if (msRemaining < 1L) return "unsucessful";
            wait(msRemaining);
            msg = bin;
            notifyAll();
        }
        return msg;

    }

    @Override
    public synchronized String receive() throws InterruptedException, ShutdownException, IllegalStateException {
        return receive(0L);
    }

    @Override
    public synchronized void shutdown() {

    }

    @Override
    public Object getLockObject() {
        return this;
    }
}
