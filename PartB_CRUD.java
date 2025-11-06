import jakarta.persistence.*;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;

@Entity
@Table(name = "students")
class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;
    public StudentEntity() {}
    public StudentEntity(String name, String email) { this.name = name; this.email = email; }
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

class HibernateUtil {
    private static final SessionFactory sessionFactory =
        new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(StudentEntity.class).buildSessionFactory();
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}

public class PartB_CRUD {
    public static void main(String[] args) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        StudentEntity s1 = new StudentEntity("Anshita", "anshita@gmail.com");
        session.save(s1);
        StudentEntity s = session.get(StudentEntity.class, 1);
        if (s != null) System.out.println("Fetched: " + s.getName() + " | " + s.getEmail());
        if (s != null) {
            s.setEmail("newmail@gmail.com");
            session.update(s);
        }
        if (s != null) session.delete(s);
        tx.commit();
        session.close();
        factory.close();
        System.out.println("CRUD operations completed!");
    }
}
