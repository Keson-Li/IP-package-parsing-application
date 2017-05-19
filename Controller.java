import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
//import javafx.event.KeyEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.util.PcapPacketArrayList;

import javax.sound.midi.MidiDevice;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * Created by Keson, A00955081 on 3/22/2017.
 */
public class Controller implements Initializable {



    @FXML private MenuItem openFileMenuItem;
    @FXML private HBox openFileHBox;
    @FXML private Label label1;
    @FXML private Label label2;
    @FXML private Label label3;
    @FXML private Label label4;
    @FXML private Label label5;
    @FXML private Label labelCurrentFile;
    @FXML private BarChart arrivalTiemBarChart;
    @FXML private BarChart frameLengthBarChart;
    @FXML private BarChart payloadBarChart;
    @FXML private PieChart protocalPieChart;
    @FXML private TableView<PacketParser> statisticTable;
    //...............table part.............
    @FXML private TableColumn<PacketParser,Long> frame;
    @FXML private TableColumn<PacketParser,String> source_port;
    @FXML private TableColumn<PacketParser,String>dest_port;
    @FXML private TableColumn<PacketParser,String> arrivalTime;
    @FXML private TableColumn<PacketParser,Integer> frameLength;
    @FXML private TableColumn<PacketParser,Integer> packetLength;
    @FXML private TableColumn<PacketParser,Integer> payloadLength;
    @FXML private TableColumn<PacketParser,Long> sequence;








//    @FXML private CategoryAxis arrivalTimeCateAxis;



    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert openFileHBox != null : "fx:id=\"openFileHBox\" was not injected.";
        assert openFileMenuItem != null : "fx:id=\"openFileMenuItem\" was not injected.";
        assert label1 != null : "fx:id=\"label1\" was not injected.";
        assert label2 != null : "fx:id=\"label2\" was not injected.";
        assert label3 != null : "fx:id=\"label3\" was not injected.";
        assert label4 != null : "fx:id=\"label4\" was not injected.";
        assert label5 != null : "fx:id=\"label5\" was not injected.";
        assert labelCurrentFile != null : "fx:id=\"labelcurrentFile\" was not injected.";
        assert arrivalTiemBarChart != null : "fx:id=\"arrivalTiemBarChart\" was not injected.";
        assert frameLengthBarChart != null : "fx:id=\"frameLengthBarChart\" was not injected.";
        assert payloadBarChart != null : "fx:id=\"payloadBarChart\" was not injected.";
        assert protocalPieChart != null : "fx:id=\"protocalPieChart\" was not injected.";
        assert statisticTable != null : "fx:id=\"statisticTable\" was not injected.";
        assert frame != null : "fx:id=\"frame\" was not injected.";
        assert source_port != null : "fx:id=\"source_port\" was not injected.";
        assert dest_port != null : "fx:id=\"dest_port\" was not injected.";
        assert arrivalTime != null : "fx:id=\"arrivalTime\" was not injected.";
        assert frameLength != null : "fx:id=\"frameLength\" was not injected.";
        assert packetLength != null : "fx:id=\"packetLength\" was not injected.";
        assert payloadLength != null : "fx:id=\"payloadLength\" was not injected.";
        assert sequence != null : "fx:id=\"sequence\" was not injected.";








    }
    private String chooseFile() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("PCAP File", "*.pcap")
        );

        if (selectedFile != null) {
            String filePath = selectedFile.getPath().toString();
            return filePath;
        }else {
            return null;
        }



    }


    public void openFileAction(ActionEvent event) throws ExceptionReadingPcapFiles,IOException {
        String filePath = chooseFile();
        if (filePath !=null){
            openFile(filePath);
        }
    }
    public void openFilelabel()throws ExceptionReadingPcapFiles,IOException{
        String filePath = chooseFile();
        if (filePath !=null){
            openFile(filePath);
        }


    }
    public void openCurrentFile()throws ExceptionReadingPcapFiles,IOException{
        if (label1.getText() !=null){
            openFile(labelCurrentFile.getText());
        }

    }
    public void openFirstFile()throws ExceptionReadingPcapFiles,IOException{
        if (label1.getText() !=null){
            openFile(label1.getText());
        }

    }
    public void openSecondFile()throws ExceptionReadingPcapFiles,IOException{
        if (label2.getText() !=null){
            openFile(label2.getText());
        }

    }
    public void openThirdFile()throws ExceptionReadingPcapFiles,IOException{
        if (label3.getText() !=null){
            openFile(label3.getText());
        }

    }
    public void openForthFile()throws ExceptionReadingPcapFiles,IOException{
        if (label4.getText() !=null){
            openFile(label4.getText());
        }

    }
    public void openFifthFile()throws ExceptionReadingPcapFiles,IOException{
        if (label5.getText() !=null){
            openFile(label5.getText());
        }

    }
    private void openFile(String path) throws ExceptionReadingPcapFiles{
        PcapFileReader fileReader = new PcapFileReader();
        FileParser fileStatistic = new FileParser(fileReader.readOfflineFiles(path));
        updateArrivalTimeBarChart(fileStatistic.getArrivalTime());
        updateFrameLength(fileStatistic.getFrameLength());
        updateProtocalPieChart(fileStatistic);
        updatePayloadLength(fileStatistic.getPayloadLength());
        labelCurrentFile.setText(path);
        updateStatisticTable(fileReader.readOfflineFiles(path));

    }
    private void updateArrivalTimeBarChart(ArrayList<String> arrivalTime) {

        TreeMap<String, Integer> uniqueTime = new TreeMap<String, Integer>();

        for (int i = 0; i < arrivalTime.size(); i++) {
            if (uniqueTime.containsKey(arrivalTime.get(i))) {
                uniqueTime.put(arrivalTime.get(i), uniqueTime.get(arrivalTime.get(i)) + 1);
            } else {
                uniqueTime.put(arrivalTime.get(i), 1);
            }
        }

        arrivalTiemBarChart.setTitle("Packet Count at Specified Time");
        XYChart.Series series = new XYChart.Series();

        for (Map.Entry<String, Integer> rt: uniqueTime.entrySet()) {
            String k = rt.getKey();
            Integer v = rt.getValue();

            series.getData().add(new XYChart.Data(k,v));
        }

        arrivalTiemBarChart.getData().add(series);
//        arrivalTiemBarChart.setPrefSize(centerPane.getWidth(), centerPane.getHeight());

//        centerPane.setContent(barChart);
    }

    private void updateFrameLength(ArrayList<Integer > frameLength) {

        TreeMap<Integer, Integer> frameLengthQty = new TreeMap();
//        ArrayList<Integer> frames = reader.getFrameLen();

        for (int i = 0; i < frameLength.size(); i++) {
            if (frameLengthQty.containsKey(frameLength.get(i))) {
                frameLengthQty.put(frameLength.get(i), frameLengthQty.get(frameLength.get(i)) + 1);
            } else {
                frameLengthQty.put(frameLength.get(i), 1);
            }
        }

        frameLengthBarChart.setTitle("Frame Length Statistics");
        XYChart.Series series = new XYChart.Series();

        for (Map.Entry<Integer, Integer> rt: frameLengthQty.entrySet()) {
            Integer k = rt.getKey();
            String ky = k.toString();
            Integer v = rt.getValue();

            series.getData().add(new XYChart.Data(ky,v));
        }

        frameLengthBarChart.getData().add(series);
    }

    private void updateProtocalPieChart(FileParser file) {

        int udpQty = file.getNumUDP();
        int tcpQty = file.getNumTCP();
        int artQty = file.getNumARP();
        int total = file.getNumConnection();
        int othersQty = total-udpQty-tcpQty-artQty;

        int tcpP = tcpQty * 100 / total;
        int udpP = udpQty * 100 / total;
        int arpp = artQty * 100 / total;
        int othersp = othersQty * 100/total;

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("TCP("+tcpQty + ") " + tcpP + "%", tcpQty),
                new PieChart.Data("UDP("+udpQty + ")" + udpP + "%", udpQty),
                new PieChart.Data("OTHERS("+othersQty + ")" + othersp + "%", othersQty),
                new PieChart.Data("ARP("+artQty + ")" + arpp + "%", artQty));
        protocalPieChart.setData(pieChartData);
    }

    public void updatePayloadLength(ArrayList<Integer> payLoadLength) {
//        ArrayList<Integer> pl = reader.getPayloadLength();
        TreeMap<Integer, Integer> count = new TreeMap();
        double averageLength = 0;

        for (int i = 0; i < payLoadLength.size(); i++) {
            averageLength += payLoadLength.get(i);

            if(count.containsKey(payLoadLength.get(i))) {
                count.put(payLoadLength.get(i), count.get(payLoadLength.get(i)) + 1);
            } else {
                count.put(payLoadLength.get(i), 1);
            }
        }

        double al = Math.round(averageLength / payLoadLength.size());

        payloadBarChart.setTitle("Payload Size Count");
        XYChart.Series series = new XYChart.Series();

        for (Map.Entry<Integer, Integer> rt: count.entrySet()) {
            Integer k = rt.getKey();
            String ke = String.valueOf(k);
            Integer v = rt.getValue();

            series.getData().add(new XYChart.Data(ke,v));
        }

        payloadBarChart.getData().add(series);
//        VBox vbox = new VBox();
//        String alToString = String.valueOf(al);
//        Label av = new Label("Average Payload Size: " + alToString);
//        av.setStyle("-fx-font: 24px arial");
//        vbox.getChildren().add(av);
//        barChart.setPrefSize(centerPane.getWidth(), centerPane.getHeight());
//        vbox.getChildren().add(barChart);
//        centerPane.setContent(vbox);
    }

    public void updateStatisticTable(PcapPacketArrayList packets) {
        frame.setCellValueFactory (new PropertyValueFactory<PacketParser, Long>("framNO"));
        dest_port.setCellValueFactory (new PropertyValueFactory<PacketParser, String>("destIP"));
        source_port.setCellValueFactory (new PropertyValueFactory<PacketParser, String>("sourceIP"));
        arrivalTime.setCellValueFactory(new PropertyValueFactory<PacketParser, String>("arrivalTime"));
        frameLength.setCellValueFactory(new PropertyValueFactory<PacketParser, Integer>("frameLength"));
        packetLength.setCellValueFactory(new PropertyValueFactory<PacketParser, Integer>("packetLength"));
        payloadLength.setCellValueFactory(new PropertyValueFactory<PacketParser, Integer>("payLoadLength"));
        sequence.setCellValueFactory(new PropertyValueFactory<PacketParser, Long>("sequence"));

        ObservableList<PacketParser> data = FXCollections.observableArrayList();
        for (PcapPacket packet : packets) {
            PacketParser packetInfo = new PacketParser();
            packetInfo.parser(packet);
            data.add(packetInfo);
        }
        statisticTable.getItems().setAll(data);

//        System.out.println(data);
//
//        TableColumn emailCol = new TableColumn("Email");
//        emailCol.setMinWidth(200);
//        emailCol.setCellValueFactory(
//                new PropertyValueFactory<Person, String>("email"));
//
//        table.setItems(data);
//        table.getColumns().addAll(firstNameCol, lastNameCol, emailCol);
    }
 /*   private void setHistoryFile(String path){
        for (int i =4; i>=1;i--){
            System.out.println(i);
            while (!historyFileList[i-1].getText().isEmpty()){
                historyFileList[i].setText(historyFileList[i-1].getText());
            }

        }
        historyFileList[0].setText(path);

    }*/

}
