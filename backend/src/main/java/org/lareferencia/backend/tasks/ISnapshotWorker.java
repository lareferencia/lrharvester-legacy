package org.lareferencia.backend.tasks;

import org.lareferencia.backend.domain.NationalNetwork;

public interface ISnapshotWorker extends Runnable{

    public void run();
    public void setNetwork(NationalNetwork network);
}