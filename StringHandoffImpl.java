package com.abc.handoff;

import com.abc.pp.stringhandoff.*;
import com.programix.thread.*;

public class StringHandoffImpl implements StringHandoff {

    private String bin;

    public StringHandoffImpl() {
        bin = " ";
    }

    @Override
    public synchronized void pass(String msg, long msTimeout)
        throws InterruptedException, TimedOutException, ShutdownException, IllegalStateException {
        long msEndTime = System.currentTimeMillis() + msTimeout;
        if (msTimeout == 0L) {
            // Receiver first: drop off message, notify all
            if(bin.equals(" ")){
                bin = msg;
                notifyAll();
            }
            // Passer First: drop off message and wait until receiver arrives
            else {
                bin = msg;
                wait();
            }
        }
        else {
            long msRemaining = msEndTime - System.currentTimeMillis();
            //if (msRemaining < 1L) throw TimedOutException;
            wait(msRemaining);
            // Receiver first: drop off message, notify all
            if(bin.equals(" ")){
                bin = msg;
                notifyAll();
            }
            // Passer First: drop off message and wait until receiver arrives
            else {
                bin = msg;
                wait();
            }
        }
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
            // Passer first: just go and pick up message
            if (! bin.equals(" ") ) {
                msg = bin;
                return msg;
            }
            // Receiver first: wait until passer arrives
            else {
                notifyAll();
                while (bin.equals(" ")) wait();
                msg = bin;
                return msg;
            }
        }
        else {
            long msRemaining = msEndTime - System.currentTimeMillis();
            //if (msRemaining < 1L) return "unsucessful";
            wait(msRemaining);
            // Passer first: just go and pick up message
            if (! bin.equals(" ") ) {
                msg = bin;
                return msg;
            }
            // Receiver first: wait until passer arrives
            else {
                notifyAll();
                while (bin.equals(" ")) wait();
                msg = bin;
                return msg;
            }
        }

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
