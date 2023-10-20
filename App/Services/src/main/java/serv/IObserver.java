package serv;

import domain.Comanda;
import domain.Produs;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IObserver extends Remote {
    void updateProduse(Produs produs) throws ServException, RemoteException;
    void updateComanda(Comanda comanda) throws ServException, RemoteException;
}
