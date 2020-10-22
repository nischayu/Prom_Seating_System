import java.util.ArrayList;
import java.util.Objects;

public class Student {

    private String name;
    private String  id;
    private ArrayList<Student> partners;
    private ArrayList<String> accommodations;
    private boolean paid;

    Student(String name, String  id, ArrayList<Student> partners) {
        this.name = name;
        this.id = id;
        this.partners = partners;
        accommodations = new ArrayList<String>();
    }

    Student(String name, String id) {
        this.name = name;
        this.id = id;
        partners = new ArrayList<>();
        accommodations = new ArrayList<String>();
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public boolean hasPaid() {
        return paid;
    }

    public ArrayList<Student> getPartners() {
        return partners;
    }

    public void setPartners(ArrayList<Student> partners) {
        this.partners = partners;
    }

    public void setPaid(boolean hasPaid) {
        this.paid = hasPaid;
    }

    public void setAccommodations (ArrayList<String> accommodations) {
        this.accommodations = accommodations;
    }

    public ArrayList<String> getAccommodations() {
        return accommodations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return  Objects.equals(name, student.name) &&
                Objects.equals(id, student.id);
    }
}
