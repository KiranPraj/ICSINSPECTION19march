package com.srj.icsinspection.handler;

import java.util.HashMap;

public interface SyncHandler {

    public void syncCallback();
    public void cancelsyncCallback();
    public void completedSyncCallback();
    public void syncIRAgainCallback();
    public void syncIRANgainCallback();
}
