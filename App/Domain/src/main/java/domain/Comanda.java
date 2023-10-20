package domain;

import java.time.LocalDate;

public class Comanda implements Entity<Integer> {
    private int id;
    private String client;
    private int suma;
    private String date;

    public Comanda(int id, String client, int suma, String date) {
        this.id = id;
        this.client = client;
        this.suma = suma;
        this.date = date;
    }

    public Comanda(){}

    @Override
    public void setId(Integer integer) {
        this.id = integer;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public int getSuma() {
        return suma;
    }

    public void setSuma(int suma) {
        this.suma = suma;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Comanda{" +
                "id=" + id +
                ", client='" + client + '\'' +
                ", suma=" + suma +
                ", date=" + date +
                '}';
    }
}
