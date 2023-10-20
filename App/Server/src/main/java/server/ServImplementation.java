package server;

import domain.*;
import jdbc.*;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import serv.IObserver;
import serv.IServices;
import serv.ServException;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServImplementation implements IServices {
    private RepoAdmin repoAdmin;
    private RepoProdus repoProdus;
    private RepoAgent repoAgent;
    private RepoComanda repoComanda;
    private RepoDetalii repoDetalii;
    private List<IObserver> loggedAdmini;
    private List<IObserver> loggedAgenti;

    public ServImplementation() {
        ApplicationContext factory = new ClassPathXmlApplicationContext("App.xml");
        this.repoAdmin = factory.getBean(RepoAdmin.class);
        this.repoProdus = factory.getBean(RepoProdus.class);
        this.repoAgent = factory.getBean(RepoAgent.class);
        this.repoComanda = factory.getBean(RepoComanda.class);
        this.repoDetalii = factory.getBean(RepoDetalii.class);
        loggedAdmini = new ArrayList<>();
        loggedAgenti = new ArrayList<>();
    }

    @Override
    public synchronized void login(String username, String password, IObserver client) throws ServException {
        Admin adminFound = repoAdmin.cauta(username, password);
        if (adminFound != null) {
            if (loggedAdmini.contains(adminFound)) {
                throw new ServException("Admin already logged in!");
            }
            loggedAdmini.add(client);
        } else {
            throw new ServException("Autentification failed!");
        }
    }

    @Override
    public synchronized void loginAgent(String username, String password, IObserver obs) throws ServException {
        Agent agentFound = repoAgent.cauta(username, password);
        if (agentFound != null) {
            if (loggedAgenti.contains(agentFound)) {
                throw new ServException("Agent already logged in!");
            }
            loggedAgenti.add(obs);
        } else {
            throw new ServException("Autentification failed!");
        }
    }

    @Override
    public void logout(String username, String password, IObserver obs) throws ServException {
        //TODO
    }


    @Override
    public void logoutAgent(String username, String password, IObserver obs) throws ServException {
        //TODO
    }

    @Override
    public List<Produs> getAll() throws ServException {
        List<Produs> produse = new ArrayList<>();
        List<Produs> lista = (List<Produs>) repoProdus.getAll();
        for (Produs m : lista) {
            produse.add(m);
        }
        return produse;
    }

    @Override
    public List<Comanda> getAllComenzi() throws ServException {
        List<Comanda> comenzi = new ArrayList<>();
        for(Comanda c : repoComanda.getAll()){
            comenzi.add(c);
        }
        return comenzi;
    }

    @Override
    public List<ProduseDto> getProduseComanda(int id_cmd) throws ServException {
        List<Integer> id_produse = new ArrayList<>();
        List<Integer> cantitati = new ArrayList<>();
        boolean odd = false;
        for(int i: repoDetalii.cauta(id_cmd)){
            if(!odd){
                id_produse.add(i);
                odd = true;
            }
            else{
                cantitati.add(i);
                odd = false;
            }
        }
        List<ProduseDto> produse = new ArrayList<>();
        for(int i: id_produse){
            Produs p = repoProdus.cauta(i);
            ProduseDto pDTO = new ProduseDto(p.getId(), p.getDenumire(), p.getPret(), cantitati.get(id_produse.indexOf(i)));
            produse.add(pDTO);
        }
        return produse;
    }

    @Override
    public boolean adaugaProdusComanda(int id_cmd, int id_produs, int cantitate) throws ServException {
        Comanda c = repoComanda.cauta(id_cmd);
        if(c != null) {
            Detalii detaliu = new Detalii(id_cmd, id_produs, cantitate);
            repoDetalii.save(detaliu);
            Produs p = repoProdus.cauta(id_produs);
            int suma = cantitate * p.getPret();
            c.setSuma(c.getSuma() + suma);
            repoComanda.update(id_cmd, c);
            p.setCantitate(p.getCantitate() - cantitate);
            repoProdus.update(id_produs, p);
            notify(p);
            return true;
        }
        return false;
    }

    @Override
    public boolean adaugaProdus(int id, String denumire, int pret, int cantitate) {
        try {
            if (repoProdus.cauta(id) == null) {
                repoProdus.save(new Produs(id, denumire, pret, cantitate));
                notify(new Produs(id, denumire, pret, cantitate));
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteProdus(int id) {
        try {
            if (repoProdus.cauta(id) != null) {
                notify(repoProdus.cauta(id));
                repoProdus.delete(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateProdus(int id_produs, int id, String denumire, int pret, int cantitate) {
        try {
            if (repoProdus.cauta(id) != null) {
                repoProdus.update(id_produs, new Produs(id, denumire, pret, cantitate));
                notify(new Produs(id_produs, denumire, pret, cantitate));
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean adaugaComanda(int id, String client, String date) throws ServException {
        if(repoComanda.cauta(id) == null){
            Comanda cmd = new Comanda(id, client, 0, date);
            repoComanda.save(cmd);
            notifyCmd(cmd);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteComanda(Integer id) {
        try {
            if (repoComanda.cauta(id) != null) {
                notifyCmd(repoComanda.cauta(id));
                repoComanda.delete(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private final int defaultThreadsNo = 5;

    private void notify(Produs produs) throws ServException {
        ExecutorService executorService = Executors.newFixedThreadPool(defaultThreadsNo);

        for (IObserver obs : loggedAdmini) {
            executorService.execute(() ->
            {
                try {
                    obs.updateProduse(produs);
                    System.out.println("Notify produse");
                } catch (ServException | RemoteException e) {
                    System.err.println("Error notifying " + e);
                }
            });
        }
        executorService.shutdown();
    }


    private void notifyCmd(Comanda comanda) throws ServException {
        ExecutorService executorService = Executors.newFixedThreadPool(defaultThreadsNo);

        for (IObserver obs : loggedAdmini) {
            executorService.execute(() ->
            {
                try {
                    obs.updateComanda(comanda);
                    System.out.println("Notify comenzi");
                } catch (ServException | RemoteException e) {
                    System.err.println("Error notifying " + e);
                }
            });
        }
        executorService.shutdown();
    }
}

