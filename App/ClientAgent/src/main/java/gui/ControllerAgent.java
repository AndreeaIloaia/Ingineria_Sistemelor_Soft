package gui;

import domain.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import serv.IObserver;
import serv.IServices;
import serv.ServException;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ControllerAgent extends UnicastRemoteObject implements IObserver, Serializable {
    public TableColumn<Object, Object> columnNo;
    public TableColumn<Object, Object> columnClient;
    public TableColumn<Object, Object> columnData;
    public TableColumn<Object, Object> columnSuma;

    public TableColumn<Object, Object> columnID;
    public TableColumn<Object, Object> columnDenumire;
    public TableColumn<Object, Object> columnCantitate;
    public TableColumn<Object, Object> columnPret;

    public TableColumn<Object, Object> mcID;
    public TableColumn<Object, Object> mcDenumire;
    public TableColumn<Object, Object> mcPret;
    public TableColumn<Object, Object> mcCantitate;

    public Label labelSuma;
    public Button btnDelete;
    public Button btnAdd;
    public Button btnAddCmd;
    public Button btnProduse;
    public Button btnLogout;


    public TextField textNrComanda;
    public TextField textClient;
    public TextField textNrCmd;
    public TextField textDate;
    public TextField textCantitate;


    private IServices server;

    public TabPane pane;
    public TableView<ProduseDto> tableViewMProduse;
    public TableView<Comanda> tableViewComenzi;
    public TableView<Produs> tableViewProduse;


    private ObservableList<Produs> produse = FXCollections.observableArrayList();
    private ObservableList<ProduseDto> produseComanda = FXCollections.observableArrayList();
    private ObservableList<Comanda> comenzi = FXCollections.observableArrayList();

    private int cmd;
    private int id_aux;

    public ControllerAgent() throws RemoteException {
    }

    @FXML
    public void initialize() {
        columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnDenumire.setCellValueFactory(new PropertyValueFactory<>("denumire"));
        columnPret.setCellValueFactory(new PropertyValueFactory<>("pret"));
        columnCantitate.setCellValueFactory(new PropertyValueFactory<>("cantitate"));

        mcID.setCellValueFactory(new PropertyValueFactory<>("id"));
        mcDenumire.setCellValueFactory(new PropertyValueFactory<>("denumire"));
        mcPret.setCellValueFactory(new PropertyValueFactory<>("pret"));
        mcCantitate.setCellValueFactory(new PropertyValueFactory<>("cantitate"));

        columnNo.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnClient.setCellValueFactory(new PropertyValueFactory<>("client"));
        columnSuma.setCellValueFactory(new PropertyValueFactory<>("suma"));
        columnData.setCellValueFactory(new PropertyValueFactory<>("date"));

        btnAdd.setOnAction(e -> {
            Produs produs = tableViewProduse.getSelectionModel().getSelectedItem();

            if (textNrComanda.getText().isEmpty() || textCantitate.getText().isEmpty()) {
                errorWindow("Introduceti datele inainte de a adauga in comanda");
            } else {
                int id = Integer.parseInt(textNrComanda.getText());
                int cantitate = Integer.parseInt(textCantitate.getText());
                try {
                    cmd = 1;
                    boolean ok = server.adaugaProdusComanda(id, produs.getId(), cantitate);
                    if (!ok) {
                        errorWindow("Nu puteti adauga produse intr-o comanda inexistenta");
                    }
                    clearFileds();
                    id_aux = id;
                    updateProduse(new Produs(produs.getId(), produs.getDenumire(), produs.getPret(), cantitate));
                } catch (Exception se) {
                    errorWindow(se.getMessage());
                }
            }

        });

        btnAddCmd.setOnAction(e -> {
            if (textNrCmd.getText().isEmpty() || textClient.getText().isEmpty() || textDate.getText().isEmpty()) {
                errorWindow("Introduceti datele inainte de a adauga o comanda");
            } else {
                int id = Integer.parseInt(textNrCmd.getText());
                String client = textClient.getText();
                String date = textDate.getText();
                try {
                    cmd = 1;
                    boolean ok = server.adaugaComanda(id, client, date);
                    if (!ok) {
                        errorWindow("Nu puteti adauga o comanda deja existenta");
                    }
                    clearFileds();
                    updateAdComanda(new Comanda(id, client, 0, date));
                } catch (Exception se) {
                    errorWindow(se.getMessage());
                }
            }

        });
        btnDelete.setOnAction(e -> {
            Comanda comanda = tableViewComenzi.getSelectionModel().getSelectedItem();
            if (comanda != null) {
                try {
                    cmd = 2;
                    boolean ok = server.deleteComanda(comanda.getId());
                    if (!ok) {
                        errorWindow("Nu exista acest produs");
                    }
                    updateComanda(new Comanda(comanda.getId(), "", 0, ""));
                    clearFileds();
                } catch (Exception se) {
                    errorWindow(se.getMessage());
                }
            } else {
                errorWindow("Nu ati selectat niciun produs");
            }
        });
        btnProduse.setOnAction(e -> {
            Comanda cmd = tableViewComenzi.getSelectionModel().getSelectedItem();
            if (cmd != null){
                try {
                    List<ProduseDto> p = server.getProduseComanda(cmd.getId());
                    produseComanda.setAll(p);
                } catch (ServException ex) {
                    ex.printStackTrace();
                }
            }
        });
        tableViewProduse.setItems(produse);
        tableViewComenzi.setItems(comenzi);
        tableViewMProduse.setItems(produseComanda);
    }

    private void errorWindow(String title) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setTitle("Warning");
        alert.setContentText(title);
        alert.show();
    }

    private void clearFileds() {
        textNrComanda.clear();
        textClient.clear();
        textNrCmd.clear();
        textDate.clear();
        textCantitate.clear();
    }


    public void setServer(IServices s) {
        server = s;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        });
    }

    private void initData() {
        try {
            Iterable<Produs> list = server.getAll();
            List<Produs> listDTO = new ArrayList<>();
            for (Produs m : list) {
                listDTO.add(m);
            }
            produse.setAll(listDTO);
            List<Comanda> cmd = new ArrayList<>();
            for (Comanda c : server.getAllComenzi()) {
                cmd.add(c);
            }
            comenzi.setAll(cmd);

        } catch (ServException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateProduse(Produs produs) throws ServException, RemoteException {
        if(cmd == 1){
            updateProduseComanda(produs);
            updateSuma(produs);
        }
    }

    @Override
    public void updateComanda(Comanda comanda) throws ServException, RemoteException {
        if(cmd == 1) {
            updateAdComanda(comanda);
        } else if(cmd == 2) {
            updateDelComanda(comanda);
        }
    }

    private void updateDelComanda(Comanda comanda) {
        Comanda selected = tableViewComenzi.getSelectionModel().getSelectedItem();
        tableViewComenzi.getItems().remove(selected);
    }

    private void updateAdComanda(Comanda comanda) {
        int i = -1;
        for (Comanda c : tableViewComenzi.getItems()) {
            i++;
        }
        Comanda c_selected = new Comanda(comanda.getId() ,comanda.getClient(), comanda.getSuma(), comanda.getDate());
        tableViewComenzi.getItems().add(c_selected);
    }

    private void updateSuma(Produs produs) {
        int i = -1;
        int index = 0;
        for (Comanda comanda :  tableViewComenzi.getItems()){
            i++;
            if(id_aux == comanda.getId()){
                index = i;
                break;
            }
        }
        Comanda c = tableViewComenzi.getItems().get(index);
        if(c != null){
            c.setId(c.getId());
            c.setClient(c.getClient());
            c.setDate(c.getDate());
            c.setSuma(c.getSuma() + produs.getPret() * produs.getCantitate());
            tableViewComenzi.getItems().set(index, c);
        } else {
            errorWindow("Eroare tehnica la update-ul sumei comenzii");
        }
    }

    private void updateProduseComanda(Produs produs) {
        int i = -1;
        int index = 0;
        for (Produs p : tableViewProduse.getItems()) {
            i++;
            if (p.getId() == produs.getId()) {
                index = i;
                break;
            }
        }
        Produs p_selected = tableViewProduse.getItems().get(index);
        if (p_selected != null) {
            p_selected.setDenumire(produs.getDenumire());
            p_selected.setPret(produs.getPret());
            p_selected.setCantitate(p_selected.getCantitate() - produs.getCantitate());
            tableViewProduse.getItems().set(index, p_selected);
        } else {
            errorWindow("nope");
        }
    }

}
