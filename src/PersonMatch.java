package src;

public class PersonMatch implements Comparable<PersonMatch> {
    Person person;
    double correlation;

    public PersonMatch(Person person, double correlation) {
        this.person = person;
        this.correlation = correlation;
    }

    public int compareTo(PersonMatch other) {
        return Double.compare(this.correlation, other.correlation);
    }
}