package domain;

public class Produs implements Entity<Integer> {
    private int id;
    private String denumire;
    private int cantitate;
    private int pret;

    public Produs(int id, String denumire, int pret, int cantitate) {
        this.id = id;
        this.denumire = denumire;
        this.cantitate = cantitate;
        this.pret = pret;
    }

    public Produs() {

    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public int getCantitate() {
        return cantitate;
    }

    public void setCantitate(int cantitate) {
        this.cantitate = cantitate;
    }

    public int getPret() {
        return pret;
    }

    public void setPret(int pret) {
        this.pret = pret;
    }

    @Override
    public String toString() {
        return "Produs{" +
                "id=" + id +
                ", denumire='" + denumire + '\'' +
                ", cantitate=" + cantitate +
                ", pret=" + pret +
                '}';
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }
}
