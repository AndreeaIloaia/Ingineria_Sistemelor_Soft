package domain;

public class Detalii implements Entity<Integer> {
    private int id;

    @Override
    public void setId(Integer integer) {
        id = integer;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id_comanda;
    private int id_produs;
    private int cantitate;

    public Detalii(int id_comanda, int id_produs) {
        this.id_comanda = id_comanda;
        this.id_produs = id_produs;
    }

    public Detalii(int id_comanda, int id_produs, int cantitate) {
        this.id_comanda = id_comanda;
        this.id_produs = id_produs;
        this.cantitate = cantitate;
    }

    public Detalii(int id, int id_comanda, int id_produs, int cantitate) {
        this.id = id;
        this.id_comanda = id_comanda;
        this.id_produs = id_produs;
        this.cantitate = cantitate;
    }

    public Detalii(){}

    public int getCantitate() {
        return cantitate;
    }

    public void setCantitate(int cantitate) {
        this.cantitate = cantitate;
    }

    public int getId_comanda() {
        return id_comanda;
    }

    public void setId_comanda(int id_comanda) {
        this.id_comanda = id_comanda;
    }

    public int getId_produs() {
        return id_produs;
    }

    public void setId_produs(int id_produs) {
        this.id_produs = id_produs;
    }
}
