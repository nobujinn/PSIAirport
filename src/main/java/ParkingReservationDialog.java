import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public final class ParkingReservationDialog {

    private static final AtomicBoolean FX_STARTED = new AtomicBoolean(false);

    private ParkingReservationDialog() {
    }

    public static ParkingReservationResult showAndWait() {
        ensureFxStarted();

        AtomicReference<ParkingReservationResult> resultRef = new AtomicReference<>();
        CountDownLatch finished = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(ParkingReservationDialog.class.getResource("/parking-reservation.fxml"));
                Parent root = loader.load();

                ParkingReservationController controller = loader.getController();

                Stage stage = new Stage();
                stage.setTitle("Rezervacia parkovacieho miesta pre lietadlo");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.setScene(new Scene(root));

                controller.setStage(stage);

                stage.setOnHidden(event -> {
                    resultRef.set(controller.getResult());
                    finished.countDown();
                });

                stage.show();
            } catch (IOException e) {
                throw new IllegalStateException("Nepodarilo sa nacitat FXML formular.", e);
            }
        });

        try {
            finished.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Vykreslenie formulara bolo prerusene.", e);
        }

        return resultRef.get();
    }

    private static void ensureFxStarted() {
        if (FX_STARTED.compareAndSet(false, true)) {
            CountDownLatch startup = new CountDownLatch(1);
            Platform.startup(startup::countDown);
            try {
                startup.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("JavaFX platforma sa nepodarilo inicializovat.", e);
            }
        }
    }
}
