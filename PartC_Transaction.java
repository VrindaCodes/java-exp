import jakarta.persistence.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.context.annotation.*;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(name = "accounts")
class Account {
    @Id
    private int id;
    private String name;
    private double balance;
    public Account() {}
    public Account(int id, String name, double balance) {
        this.id = id; this.name = name; this.balance = balance;
    }
    public int getId() { return id; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
}

@Repository
class AccountDao {
    private final SessionFactory sessionFactory;
    public AccountDao(SessionFactory sessionFactory) { this.sessionFactory = sessionFactory; }
    public Account getAccount(int id) {
        return sessionFactory.getCurrentSession().get(Account.class, id);
    }
    public void updateAccount(Account acc) {
        sessionFactory.getCurrentSession().update(acc);
    }
}

@Service
class AccountService {
    private final AccountDao dao;
    public AccountService(AccountDao dao) { this.dao = dao; }
    @Transactional
    public void transferMoney(int fromId, int toId, double amount) {
        Account from = dao.getAccount(fromId);
        Account to = dao.getAccount(toId);
        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + amount);
        dao.updateAccount(from);
        dao.updateAccount(to);
        System.out.println("Transaction successful: " + amount + " transferred!");
    }
}

@Configuration
@ComponentScan(basePackages = ".")
@EnableTransactionManagement
class AppConfig {
    @Bean
    public SessionFactory sessionFactory() {
        return new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Account.class).buildSessionFactory();
    }
    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sf) {
        return new HibernateTransactionManager(sf);
    }
    @Bean
    public AccountDao accountDao(SessionFactory sf) {
        return new AccountDao(sf);
    }
    @Bean
    public AccountService accountService(AccountDao dao) {
        return new AccountService(dao);
    }
}

public class PartC_Transaction {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        AccountService service = context.getBean(AccountService.class);
        service.transferMoney(1, 2, 500.0);
        context.close();
    }
}
