package gui;

import domain.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import serv.IObserver;
import serv.IServices;
import serv.ServException;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ControllerAdmin extends UnicastRemoteObject implements IObserver, Serializable {
    public TextField fieldID;
    public TextField fieldDenumire;
    public TextField fieldPret;
    public TextField fieldCantitate;
    public Button btnDelete;
    public Button btnUpdate;
    public Button btnAdd;
    private IServices server;
    private Admin admin;

    public TableView<Produs> tableView;
    public TableColumn<Object, Object> tableColumnDenumire;
    public TableColumn<Object, Object> tableColumnPret;
    public TableColumn<Object, Object> tableColumnCantitate;
    public TableColumn<Object, Object> tableColumnId;
    public Button btnLogOut;
    private int cmd;

    private ObservableList<Produs> produse = FXCollections.observableArrayList();

    public ControllerAdmin() throws RemoteException {
    }

    @FXML
    public void initialize() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnDenumire.setCellValueFactory(new PropertyValueFactory<>("denumire"));
        tableColumnPret.setCellValueFactory(new PropertyValueFactory<>("pret"));
        tableColumnCantitate.setCellValueFactory(new PropertyValueFactory<>("cantitate"));
        btnUpdate.setOnAction(e -> {
            Produs selected = tableView.getSelectionModel().getSelectedItem();
            if(selected == null){
                errorWindow("Nu ati selectat niciun produs");
            }
            else {
                String id_field = fieldID.getText();
                String denumire = fieldDenumire.getText();
                String pret_field = fieldPret.getText();
                String cant_field = fieldCantitate.getText();
                if (id_field.isEmpty() || denumire.isEmpty() || pret_field.isEmpty() || cant_field.isEmpty()) {
                    errorWindow("Introduceti datele inainte de a modifica");
                } else {
                    int id = Integer.parseInt(id_field);
                    int pret = Integer.parseInt(pret_field);
                    int cantitate = Integer.parseInt(cant_field);

                    //daca nu selecteaza nicio entitate, sa trimit id-ul din field
                    if (server.updateProdus(selected.getId(), id, denumire, pret, cantitate)) {
                        try {
                            cmd = 2;
                            updateProduse(new Produs(selected.getId(), denumire, pret, cantitate));
                            clearFileds();
                        } catch (ServException | RemoteException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        errorWindow("Nu s-a putut face actualizarea");
                    }
                }
            }

        });
        btnAdd.setOnAction(e -> {
            String id_field =  fieldID.getText();
            String denumire = fieldDenumire.getText();
            String pret_field = fieldPret.getText();
            String cant_field = fieldCantitate.getText();
            if (id_field.isEmpty() || denumire.isEmpty() || pret_field.isEmpty() || cant_field.isEmpty()) {
                errorWindow("Introduceti datele inainte de a adauga");
            }
            else {
                int id = Integer.parseInt(id_field);
                int pret = Integer.parseInt(pret_field);
                int cantitate = Integer.parseInt(cant_field);

                try {
                    cmd = 1;
                    boolean ok = server.adaugaProdus(id, denumire, pret, cantitate);
                    if (!ok) {
                        errorWindow("Produsul exista deja");
                    }
                    clearFileds();
                } catch (Exception se) {
                    errorWindow(se.getMessage());
                }
            }

        });
        btnDelete.setOnAction(e -> {
            Produs produs = tableView.getSelectionModel().getSelectedItem();
            if (produs != null) {
                try {
                    cmd = 3;
                    boolean ok = server.deleteProdus(produs.getId());
                    if(!ok){
                        errorWindow("Nu exista acest produs");
                    }
                    clearFileds();
                } catch (Exception se) {
                    errorWindow(se.getMessage());
                }
            }
            else {
                errorWindow("Nu ati selectat niciun produs");
            }
        });
        tableView.setItems(produse);
    }

    private void errorWindow(String title){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setTitle("Warning");
        alert.setContentText(title);
        alert.show();
    }

    private void clearFileds() {
        fieldID.clear();
        fieldCantitate.clear();
        fieldPret.clear();
        fieldDenumire.clear();
    }


    public void logout() {
        try {
            server.logout(admin.getNume(), admin.getParola(), this);
        } catch (ServException e) {
            System.out.println("Logout error " + e);
        }
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
        } catch (ServException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateProduse(Produs produs) throws ServException, RemoteException {
        if (cmd == 1) {
            upAdd(produs);
        }
        if (cmd == 2) {
            upUpdate(produs);
        }
        if (cmd == 3) {
            upDelete(produs);
        }
    }

    @Override
    public void updateComanda(Comanda comanda) throws ServException, RemoteException {

    }

    private void upDelete(Produs produs) {
        Produs selected = tableView.getSelectionModel().getSelectedItem();
        tableView.getItems().remove(selected);
    }

    private void upAdd(Produs produs) throws ServException, RemoteException {
        int i = -1;
        for (Produs p : tableView.getItems()) {
            i++;
        }
        Produs p_selected = new Produs(produs.getId() ,produs.getDenumire(), produs.getPret(), produs.getCantitate());
        tableView.getItems().add(p_selected);
    }

    private void upUpdate(Produs produs) throws ServException, RemoteException {
        int i = -1;
        int index = 0;
        for (Produs p : tableView.getItems()) {
            i++;
            if (p.getId() == produs.getId()) {
                index = i;
                break;
            }
        }
        Produs p_selected = tableView.getItems().get(index);
        if (p_selected != null) {
            p_selected.setDenumire(produs.getDenumire());
            p_selected.setPret(produs.getPret());
            p_selected.setCantitate(produs.getCantitate());
            tableView.getItems().set(index, p_selected);
        } else {
            errorWindow("nope");
        }
    }
}
