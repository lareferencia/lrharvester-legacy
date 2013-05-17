package org.lareferencia.backend.tasks;


public interface ISnapshotWorker extends Runnable{

    public void run();
    public void setNetworkID(Long networkID);
    public void setSnapshotID(Long snapshotID);

}