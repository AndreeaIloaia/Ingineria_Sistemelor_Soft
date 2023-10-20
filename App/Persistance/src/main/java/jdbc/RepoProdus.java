package jdbc;

import domain.Produs;
import interfaces.IRepoProdus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import validator.ValidatorProdus;

import java.util.ArrayList;
import java.util.List;

public class RepoProdus implements IRepoProdus {
    private static final Logger logger = LogManager.getLogger(RepoProdus.class);
    private ValidatorProdus validatorProdus;

    public ValidatorProdus getValidatorProdus() {
        return validatorProdus;
    }

    public void setValidatorProdus(ValidatorProdus validatorProdus) {
        this.validatorProdus = validatorProdus;
    }

    public RepoProdus() {
        logger.info("Constructor finished!");
    }

    @Override
    public Produs cauta(Integer id) {
        initialize();
        Produs c = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                String query = "from Produs c where c.id = :id_prod";
                c = (Produs) session.createQuery(query)
                        .setInteger("id_prod", id)
                        .setMaxResults(1)
                        .uniqueResult();
                transaction.commit();
            } catch (RuntimeException ex) {
                if (transaction != null)
                    transaction.rollback();
            } finally {
                close();
            }
        }
        return c;
    }

    static SessionFactory sessionFactory;

    static void initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    static void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }

    }

    @Override
    public void save(Produs produs) {
        initialize();
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.save(produs);
                transaction.commit();
            } catch (RuntimeException e) {
                if (transaction != null) {
                    transaction.rollback();
                }
            } finally {
                close();
            }
        }
    }

    @Override
    public void delete(Integer id) {
        initialize();
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

                Produs crit = session.createQuery("from Produs where id=" + String.valueOf(id), Produs.class)
                        .setMaxResults(1)
                        .uniqueResult();
                System.err.println("Stergem produsul " + crit.getId());
                session.delete(crit);
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            } finally {
                close();
            }
        }
    }

    @Override
    public Produs findOne(Integer id) {
        initialize();
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Produs p =
                        session.createQuery("from Produs where id=" + String.valueOf(id), Produs.class)
                                .getSingleResult();
                tx.commit();
                return p;
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            } finally {
                close();
            }
        }
        return null;
    }

    @Override
    public void update(Integer id, Produs p) {
        initialize();
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Produs produs = (Produs) session.load(Produs.class, id);
                produs.setCantitate(p.getCantitate());
                produs.setDenumire(p.getDenumire());
                produs.setPret(p.getPret());
                session.update(produs);
                tx.commit();

            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            } finally {
                close();
            }
        }
    }

    @Override
    public Iterable<Produs> getAll() {
        initialize();
        List<Produs> produse = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                produse =
                        session.createQuery("from Produs", Produs.class).
                                //  setFirstResult(1).setMaxResults(5).
                                        list();
                System.out.println(produse.size() + " message(s) found:");
                for (Produs produs : produse) {
                    System.out.println(produs.getId() + ' ' + produs.getDenumire() + ' ' + produs.getCantitate() + ' ' + produs.getPret());
                }
                transaction.commit();
            } catch (RuntimeException ex) {
                if (transaction != null)
                    transaction.rollback();
            } finally {
                close();
            }
        }
        return produse;

    }
}
