import org.springframework.context.annotation.*;

class Course {
    private String courseName;
    public Course(String courseName) { this.courseName = courseName; }
    public String getCourseName() { return courseName; }
}

class Student {
    private Course course;
    public Student(Course course) { this.course = course; }
    public void showDetails() {
        System.out.println("Student is enrolled in: " + course.getCourseName());
    }
}

@Configuration
class DIConfig {
    @Bean
    public Course course() {
        return new Course("Spring Framework with Hibernate");
    }
    @Bean
    public Student student() {
        return new Student(course());
    }
}

public class PartA_DI {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DIConfig.class);
        Student student = context.getBean(Student.class);
        student.showDetails();
        context.close();
    }
}
