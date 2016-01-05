package mapper;

import domain.Person;

import java.util.Set;

/**
 * Created by hyq on 2016/1/5.
 */
public interface PersonMapper {
    Set<Person> selectAllPersons();

    Person selectPerson(long id);

    void insertPerson(Person person);

    void deletePerson(long id);

    void deleteAllPersons();
}
