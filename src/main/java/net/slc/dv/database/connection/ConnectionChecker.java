package net.slc.dv.database.connection;

import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import net.slc.dv.helper.ObservableVariable;

public class ConnectionChecker {

    private final Connect connect;

    @Getter
    private final ObservableVariable<Boolean> isConnected;

    public ConnectionChecker() {
        this.connect = Connect.getConnection();
        this.isConnected = new ObservableVariable<>(null);

        this.checkConnectionChange();
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });

        executorService.scheduleAtFixedRate(this::checkConnection, 0, 1000, TimeUnit.MILLISECONDS);
    }

    private void checkConnectionChange() {
        this.isConnected.addListener(value -> {
            if (value.equals(true)) {
                return;
            }
        });
    }

    private void checkConnection() {
        try {
            if (connect.getConnect() == null || !connect.getConnect().isValid(5)) {
                this.connect.reconnect();
                isConnected.setValue(false);
                return;
            }

            isConnected.setValue(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
