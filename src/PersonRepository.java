import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.Optional;
public class PersonRepository {
    private List<Person> people;

    public PersonRepository() {
        people = new ArrayList<>();
    }

    public boolean validateUser(Person person) {
        return true;
    }

    public void registerUser(Person person) {
        people.add(person);
    }

    public List<Person> getUsers() {
        return people;
    }

    public Optional<Person> getUserById(Integer id) {
        Predicate<Person> findByIdPredicate = (person -> Objects.equals(id, person.getId()));
        return people.stream().filter(findByIdPredicate).findFirst();
    }

    public boolean deleteUserById(Integer id) {
        Predicate<Person> findByIdPredicate = (person -> Objects.equals(id, person.getId()));
        return people.removeIf(findByIdPredicate);
    }

    public void updateUser(Person personToUpdate) {
        Predicate<Person> findByIdPredicate = (person -> Objects.equals(personToUpdate.getId(), person.getId()));
        Optional<Person> userFound = people.stream().filter(findByIdPredicate).findFirst();
        if (userFound.isPresent()) {
            Person person = userFound.get();
            person.setEmail(personToUpdate.getEmail());
            person.setPassword(personToUpdate.getPassword());
            person.setUsername(personToUpdate.getUsername());
        }
    }
}
