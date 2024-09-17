/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package fourierAnim;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.ArrayList;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.layout.VBox;

import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Stefan
 */
public class DialogController implements Initializable {

    @FXML
    private TextField txtMaxFreq;
    @FXML
    private TableView<FourierCoeff> tabCoeff;
    @FXML
    private TableColumn<FourierCoeff, Integer> colFreq;
    @FXML
    private TableColumn<FourierCoeff, Double> colRadius;
    @FXML
    private TableColumn<FourierCoeff, Double> colAngle;
    @FXML
    private Button btnUp;
    @FXML
    private Button btnDown;
    @FXML
    private Button btnClose;
    @FXML
    private VBox dialogPane;

    @FXML
    private void btnUpAction(ActionEvent event) {
        int maxFreq;
        
        System.out.println("txtMaxFreq: " + txtMaxFreq.getText());
        maxFreq = Integer.parseInt(txtMaxFreq.getText());
        maxFreq++;
        txtMaxFreq.setText(Integer.toString(maxFreq));
    }

    @FXML
    private void btnDownAction(ActionEvent event) {
        int maxFreq;
        
        System.out.println("txtMaxFreq: " + txtMaxFreq.getText());
        maxFreq = Integer.parseInt(txtMaxFreq.getText());
        if (maxFreq > 1) {
            maxFreq--;
            txtMaxFreq.setText(Integer.toString(maxFreq));
        }
    }

    @FXML
    private void btnCloseAction(ActionEvent event) {
        dialogPane.getScene().getWindow().hide();
    }

    @FXML
    private void colFreqCommit(CellEditEvent<FourierCoeff, Integer> event) {
        System.out.println("colFreq.onCommit()");
    }

    @FXML
    private void colRadiusCommit(CellEditEvent<FourierCoeff, Double> event) {
        ((FourierCoeff) event.getTableView().getItems().get(
                event.getTablePosition().getRow())
                ).setRadius(event.getNewValue());
    }

    @FXML
    private void colAngleCommit(CellEditEvent<FourierCoeff, Double> event) {
        ((FourierCoeff) event.getTableView().getItems().get(
                event.getTablePosition().getRow())
                ).setAngle(event.getNewValue());
    }
    
    public class FourierCoeff {
        private IntegerProperty freq;
        private DoubleProperty  radius;
        private DoubleProperty  angle;
        
        public FourierCoeff(Integer freq, Double radius, Double angle) {
            this.freq   = new SimpleIntegerProperty(this, "freq", freq);
            this.radius = new SimpleDoubleProperty(this, "radius", radius);
            this.angle  = new SimpleDoubleProperty(this, "angle", angle);
        }
        
        public void setFreq(Integer freq)        { this.freq.set(freq); }
        public Integer getFreq() { return freq.get(); }
        public IntegerProperty freqProperty()   { return freq; }

        public void setRadius(Double radius) { this.radius.set(radius); }
        public Double getRadius() { return radius.get(); }
        public DoubleProperty  radiusProperty() { return radius; }

        public void setAngle(Double angle)   { this.angle.set(angle); }
        public Double getAngle() { return angle.get(); }
        public DoubleProperty  angleProperty()  { return angle; }        
    }


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ArrayList<FourierCoeff> coeffList = new ArrayList<FourierCoeff>();
        coeffList.add(new FourierCoeff(-4, -0.0390431, -90.0));
        coeffList.add(new FourierCoeff(-3, -0.2342587, -90.0));
        coeffList.add(new FourierCoeff(-2, -0.1952156, -90.0));
        coeffList.add(new FourierCoeff(-1, +0.9760778, +90.0));
        coeffList.add(new FourierCoeff( 0, -0.0060066, -90.0));
        coeffList.add(new FourierCoeff( 1, +0.0390431, +90.0));
        coeffList.add(new FourierCoeff( 2, -0.1952156, -90.0));
        coeffList.add(new FourierCoeff( 3, +0.0780862, +90.0));
        coeffList.add(new FourierCoeff( 4, -0.0390431, -90.0));
                
        ObservableList<FourierCoeff> obsCoeffList = FXCollections.observableArrayList(coeffList);
        
//        Callback<TableColumn<FourierCoeff, Double>, TableCell<FourierCoeff, Double>> cellFactory =
//                new Callback<TableColumn<FourierCoeff, Double>, TableCell<FourierCoeff, Double>>() {
//                    public TableCell<FourierCoeff, Double> call(TableColumn<FourierCoeff, Double> p) {
//                        return new EditingCell();
//                    }
//                };
        
        txtMaxFreq.setText("2");
        
        tabCoeff.setEditable(true);
        tabCoeff.setItems(obsCoeffList);

        colFreq.setEditable(true);
        colRadius.setEditable(true);
        colAngle.setEditable(true);

        colFreq.setCellValueFactory(new Callback<CellDataFeatures<FourierCoeff, Integer>, ObservableValue<Integer>>() {
            @SuppressWarnings("unchecked")
            public ObservableValue<Integer> call(CellDataFeatures<FourierCoeff, Integer> p) {
                return new ReadOnlyObjectWrapper(p.getValue().getFreq());
            }
        });
        
        colRadius.setCellValueFactory(
                new PropertyValueFactory<FourierCoeff, Double>("radius"));
        
        colAngle.setCellValueFactory(
                new PropertyValueFactory<FourierCoeff, Double>("angle"));
    }    

//    class EditingCell extends TableCell<FourierCoeff, Double> {
//        private TextField textField;
//        
//        public EditingCell() {}
//        
//        @Override
//        public void startEdit() {
//            super.startEdit();
//            if (textField == null) {
//                createTextField();
//            }
//            setText(null);
//            setGraphic(textField);
//            textField.selectAll();
//        }
//        
//        private void createTextField() {
//            textField = new TextField(getString());
//            textField.setOnKeyReleased(new EventHandler<KeyEvent>() {
//                @Override
//                public void handle(KeyEvent ev) {
//                    if (ev.getCode() == KeyCode.ENTER) {
//                        commitEdit(textField.getText());
//                    } else if (ev.getCode() == KeyCode.ESCAPE) {
//                        cancelEdit();
//                    }
//                }
//            });
//        }
//        
//        private String getString() {
//            return getItem() == null ? "" : getItem().toString();
//        }   
//    }
}
