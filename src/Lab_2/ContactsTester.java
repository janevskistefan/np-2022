package Lab_2;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ContactsTester {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        Faculty faculty = null;

        int rvalue = 0;
        long rindex = -1;

        DecimalFormat df = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            rvalue++;
            String operation = scanner.next();

            switch (operation) {
                case "CREATE_FACULTY": {
                    String name = scanner.nextLine().trim();
                    int N = scanner.nextInt();

                    Student[] students = new Student[N];

                    for (int i = 0; i < N; i++) {
                        rvalue++;

                        String firstName = scanner.next();
                        String lastName = scanner.next();
                        String city = scanner.next();
                        int age = scanner.nextInt();
                        long index = scanner.nextLong();

                        if ((rindex == -1) || (rvalue % 13 == 0))
                            rindex = index;

                        Student student = new Student(firstName, lastName, city,
                                age, index);
                        students[i] = student;
                    }

                    faculty = new Faculty(name, students);
                    break;
                }

                case "ADD_EMAIL_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String email = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addEmailContact(date, email);
                    break;
                }

                case "ADD_PHONE_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String phone = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addPhoneContact(date, phone);
                    break;
                }

                case "CHECK_SIMPLE": {
                    System.out.println("Average number of contacts: "
                            + df.format(faculty.getAverageNumberOfContacts()));

                    rvalue++;

                    String city = faculty.getStudent(rindex).getCity();
                    System.out.println("Number of students from " + city + ": "
                            + faculty.countStudentsFromCity(city));

                    break;
                }

                case "CHECK_DATES": {

                    rvalue++;

                    System.out.print("Latest contact: ");
                    Contact latestContact = faculty.getStudent(rindex)
                            .getLatestContact();
                    if (latestContact.getType().equals("Email"))
                        System.out.println(((EmailContact) latestContact)
                                .getEmail());
                    if (latestContact.getType().equals("Phone"))
                        System.out.println(((PhoneContact) latestContact)
                                .getPhone()
                                + " ("
                                + ((PhoneContact) latestContact).getOperator()
                                .toString() + ")");

                    if (faculty.getStudent(rindex).getEmailContacts().length > 0
                            && faculty.getStudent(rindex).getPhoneContacts().length > 0) {
                        System.out.print("Number of email and phone contacts: ");
                        System.out
                                .println(faculty.getStudent(rindex)
                                        .getEmailContacts().length
                                        + " "
                                        + faculty.getStudent(rindex)
                                        .getPhoneContacts().length);

                        System.out.print("Comparing dates: ");
                        int posEmail = rvalue
                                % faculty.getStudent(rindex).getEmailContacts().length;
                        int posPhone = rvalue
                                % faculty.getStudent(rindex).getPhoneContacts().length;

                        System.out.println(faculty.getStudent(rindex)
                                .getEmailContacts()[posEmail].isNewerThan(faculty
                                .getStudent(rindex).getPhoneContacts()[posPhone]));
                    }

                    break;
                }

                case "PRINT_FACULTY_METHODS": {
                    System.out.println("Faculty: " + faculty.toString());
                    System.out.println("Student with most contacts: "
                            + faculty.getStudentWithMostContacts().toString());
                    break;
                }

            }

        }

        scanner.close();
    }
}

abstract class Contact {
    public Date creation_date;

    Contact(String date) {
        creation_date = new Date();
        String[] parts_of_date = date.split("-");
        creation_date.setYear(Integer.parseInt(parts_of_date[0]));
        creation_date.setMonth(Integer.parseInt(parts_of_date[1]));
        creation_date.setDate(Integer.parseInt(parts_of_date[2]));
    }

    public boolean isNewerThan(Contact other) {
        return creation_date.after(other.creation_date);
    }

    public abstract String getType();

}

class EmailContact extends Contact {

    String email;

    EmailContact(String date, String email) {
        super(date);
        this.email = String.copyValueOf(email.toCharArray());
    }

    public String getEmail() {
        return String.copyValueOf(email.toCharArray());
    }

    @Override
    public String getType() {
        return "Email";
    }

    public String toString() {
        return "\"" + String.copyValueOf(email.toCharArray()) + "\"";
    }
}

enum Operator {
    VIP,
    ONE,
    TMOBILE
}

class PhoneContact extends Contact {
    String phone;

    PhoneContact(String date, String phone) {
        super(date);
        this.phone = String.copyValueOf(phone.toCharArray());
    }

    public String getPhone() {
        return String.copyValueOf(phone.toCharArray());
    }

    public Operator getOperator() {
        String op_num = phone.substring(0, 3);
        switch (op_num) {
            case "071":
            case "072":
            case "070":
                return Operator.TMOBILE;
            case "078":
            case "077":
                return Operator.VIP;
            case "075":
            case "076":
                return Operator.ONE;
        }
        // invalid phone number
        return null;
    }

    @Override
    public String getType() {
        return "Phone";
    }

    public String toString() {
        return "\"" + String.copyValueOf(phone.toCharArray()) + "\"";
    }
}

class Student {
    String firstName;
    String lastName;
    String city;
    int age;
    long index;

    ArrayList<Contact> contact_list;

    Student(String firstName, String lastName, String city, int age, long index) {
        this.firstName = String.copyValueOf(firstName.toCharArray());
        this.lastName = String.copyValueOf(lastName.toCharArray());
        this.city = String.copyValueOf(city.toCharArray());
        this.age = age;
        this.index = index;
        contact_list = new ArrayList<>();
    }

    public void addEmailContact(String date, String email) {
        contact_list.add(new EmailContact(date, email));
    }

    public void addPhoneContact(String name, String number) {
        contact_list.add(new PhoneContact(name, number));
    }

    public Contact[] getEmailContacts() {
        return get_contacts_by_type("Email");
    }

    public Contact[] getPhoneContacts() {
        return get_contacts_by_type("Phone");
    }

    public Contact[] get_contacts_by_type(String query) {
        List<Contact> contact_list_query = contact_list.stream()
                .filter(contact -> contact.getType().equals(query))
                .collect(Collectors.toList());
        Contact[] tempContacts = new Contact[contact_list_query.size()];
        for (int i = 0; i < contact_list_query.size(); i++)
            tempContacts[i] = contact_list_query.get(i);
        return tempContacts;
    }

    public String getCity() {
        return String.copyValueOf(city.toCharArray());
    }

    public String getFullName() {
        return String.copyValueOf(firstName.toCharArray()).toUpperCase() + " " + String.copyValueOf(lastName.toCharArray()).toUpperCase();
    }

    public long getIndex() {
        return index;
    }

    public Contact getLatestContact() {
        return contact_list.stream().reduce(new PhoneContact("1970-01-01", ""), (first_contact, second_contact) ->
                first_contact.creation_date.after(second_contact.creation_date) ? first_contact : second_contact);
    }

    public String toString() {
        StringBuilder temp_string = new StringBuilder();

        temp_string.append("{");
        temp_string.append("\"ime\"");
        temp_string.append(":");
        temp_string.append("\"").append(firstName).append("\"");
        temp_string.append(", ");

        temp_string.append("\"prezime\"");
        temp_string.append(":");
        temp_string.append("\"").append(lastName).append("\"");
        temp_string.append(", ");

        temp_string.append("\"vozrast\"");
        temp_string.append(":");
        temp_string.append(age);
        temp_string.append(", ");


        temp_string.append("\"grad\"");
        temp_string.append(":");
        temp_string.append("\"").append(city).append("\"");
        temp_string.append(", ");


        temp_string.append("\"indeks\"");
        temp_string.append(":");
        temp_string.append(index);
        temp_string.append(", ");


        temp_string.append("\"telefonskiKontakti\"");
        temp_string.append(":");
        temp_string.append(Arrays.toString(getPhoneContacts()));
        temp_string.append(", ");


        temp_string.append("\"emailKontakti\"");
        temp_string.append(":");
        temp_string.append(Arrays.toString(getEmailContacts()));

        temp_string.append("}");
        return temp_string.toString();
    }

}

class Faculty {
    String name;
    Student[] students;

    public Faculty(String name, Student[] students) {
        this.name = String.copyValueOf(name.toCharArray());
        this.students = Arrays.copyOfRange(students, 0, students.length);
    }

    public int countStudentsFromCity(String city_name) {
        return (int) Arrays.stream(students).filter(student -> student.getCity().equals(city_name)).count();
    }

    public double getAverageNumberOfContacts() {
        return Arrays.stream(students).mapToDouble(student -> student.contact_list.size()).average().getAsDouble();
    }

    public Student getStudentWithMostContacts() {
        Student student_with_max_contacts = Arrays.stream(students)
                .reduce(new Student("", "", "", 0, 0), (student, student2) -> student.contact_list.size() > student2.contact_list.size() ? student : student2);

        int students_with_same_number_of_friends = (int) Arrays.stream(students)
                .filter(student -> student.contact_list.size() == student_with_max_contacts.contact_list.size())
                .count();

        if (students_with_same_number_of_friends > 1) {
            return Arrays.stream(students)
                    .filter(student -> student.contact_list.size() == student_with_max_contacts.contact_list.size())
                    .reduce(new Student("", "", "", 0, 0), (student, student2) -> student.index > student2.index ? student : student2);
        }
        return student_with_max_contacts;
    }

    public String toString() {
        StringBuilder tempString = new StringBuilder();

        tempString.append("{\"fakultet\"");
        tempString.append(":");
        tempString.append("\"").append(name).append("\"");
        tempString.append(", ");

        tempString.append("\"studenti\"");
        tempString.append(":");
        tempString.append(Arrays.toString(students));
        tempString.append("}");

        return tempString.toString();
    }

    public Student getStudent(long index) {
        return Arrays.stream(students).filter(student -> student.getIndex() == index).findFirst().get();
    }
}