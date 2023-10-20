package serv;

import domain.Comanda;
import domain.Produs;
import domain.ProduseDto;

import java.util.List;

public interface IServices {
    void login(String username, String password, IObserver obs) throws ServException;
    void logout(String username, String password, IObserver obs) throws ServException;
    void loginAgent(String username, String password, IObserver obs) throws ServException;
    void logoutAgent(String username, String password, IObserver obs) throws ServException;

    List<Produs> getAll() throws ServException;
    List<Comanda> getAllComenzi() throws ServException;
    List<ProduseDto> getProduseComanda(int id_comanda) throws ServException;

    boolean adaugaProdusComanda(int id_cmd, int id_produs, int cantitate) throws ServException;

    boolean adaugaProdus(int id, String denumire, int pret, int cantitate);
    boolean deleteProdus(int id);
    boolean updateProdus(int id_produs, int id, String denumire, int pret, int cantitate);

    boolean adaugaComanda(int id, String client, String date) throws ServException;
    boolean deleteComanda(Integer id);
}
