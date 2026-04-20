import PlaneManagement.Parking;
import PlaneManagement.Plane;
import PlaneManagement.PlaneType;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class ParkingReservationController {

    @FXML
    private ComboBox<Plane> planeComboBox;

    @FXML
    private DatePicker dateFromPicker;

    @FXML
    private DatePicker dateToPicker;

    @FXML
    private TextField parkingPlaceField;

    @FXML
    private Label feedbackLabel;

    private Stage stage;
    private ParkingReservationResult result;

    @FXML
    public void initialize() {
        seedPlanesIfNeeded();
        planeComboBox.getItems().setAll(Plane.getRegistrovaneLietadla());
        if (!planeComboBox.getItems().isEmpty()) {
            planeComboBox.getSelectionModel().selectFirst();
        }

        LocalDate today = LocalDate.now();
        dateFromPicker.setValue(today);
        dateToPicker.setValue(today.plusDays(1));
        parkingPlaceField.setText("1");
        feedbackLabel.setText("");
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public ParkingReservationResult getResult() {
        return result;
    }

    @FXML
    private void onConfirm() {
        Plane selectedPlane = planeComboBox.getValue();
        if (selectedPlane == null) {
            feedbackLabel.setText("Vyberte lietadlo.");
            return;
        }

        LocalDate fromDate = dateFromPicker.getValue();
        LocalDate toDate = dateToPicker.getValue();
        if (fromDate == null || toDate == null) {
            feedbackLabel.setText("Vyberte datum od aj datum do.");
            return;
        }

        int parkingId;
        try {
            parkingId = Integer.parseInt(parkingPlaceField.getText().trim());
            if (parkingId <= 0) {
                feedbackLabel.setText("Parkovacie miesto musi byt kladne cislo.");
                return;
            }
        } catch (NumberFormatException ex) {
            feedbackLabel.setText("Zadajte platne cislo parkovacieho miesta.");
            return;
        }

        LocalDateTime dateFrom = LocalDateTime.of(fromDate, LocalTime.MIN);
        LocalDateTime dateTo = LocalDateTime.of(toDate, LocalTime.MAX);

        Parking reservation = new Parking(parkingId, dateFrom, dateTo, selectedPlane);
        try {
            if (!reservation.overDostupnost()) {
                feedbackLabel.setText("Miesto nie je dostupne, zmente termin alebo miesto.");
                return;
            }
            reservation.ulozRezervaciu();
            result = new ParkingReservationResult(selectedPlane, dateFrom, dateTo, parkingId);
            stage.close();
        } catch (IllegalStateException ex) {
            feedbackLabel.setText(ex.getMessage());
        }
    }

    private void seedPlanesIfNeeded() {
        List<Plane> registered = Plane.getRegistrovaneLietadla();
        if (!registered.isEmpty()) {
            return;
        }
        new Plane(1, PlaneType.PRIVATE, 8).ulozLietadlo();
        new Plane(2, PlaneType.COMMERCIAL, 220).ulozLietadlo();
        new Plane(3, PlaneType.MILITARY, 30).ulozLietadlo();
    }
}
