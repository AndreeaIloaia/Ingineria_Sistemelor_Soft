package jdbc;

import domain.Agent;
import interfaces.IRepoAgent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class RepoAgent implements IRepoAgent {
    private static final Logger logger = LogManager.getLogger(IRepoAgent.class);

    public RepoAgent() {
        logger.info("Constructor finished!");
    }
    @Override
    public Agent cauta(String user, String pass) {
        initialize();
        Agent c = new Agent();
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                String query = "from Agent c where c.nume like :user and c.parola like :pass";
                c = (Agent) session.createQuery(query)
                        .setString("user", user)
                        .setString("pass", pass)
                        .setMaxResults(1)
                        .uniqueResult();
                transaction.commit();
            } catch (RuntimeException ex) {
                if (transaction != null)
                    transaction.rollback();
            }
            finally {
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
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    static void close(){
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }

    }

    @Override
    public void save(Agent agent) {

    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public Agent findOne(Integer integer) {
        return null;
    }

    @Override
    public void update(Integer integer, Agent agent) {

    }

    @Override
    public Iterable<Agent> getAll() {
        return null;
    }
}
