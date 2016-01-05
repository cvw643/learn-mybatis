import domain.Person;
import mapper.PersonMapper;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.util.Set;


/**
 * Created by hyq on 2016/1/5.
 */
public class Application {
    private static final String dbServer;
    private static final int dbPort;
    private static final String dbName;
    private static final String dbUsername;
    private static final String dbPassword;

    static {
        dbServer = "localhost";
        dbPort = 3306;
        dbName = "test";
        dbUsername = "root";
        dbPassword = "123456";
    }

    public static void main(String[] args) {
        deleteAllPersons();
        printAllPersons();

        addPerson(1, "黄", "玉强", 36);
        addPerson(2, "卢", "文", 33);
        addPerson(3, "黄", "歆瑶", 4);
        printAllPersons();

        deletePerson(1);
        printAllPersons();
    }


    private static SqlSessionFactory getSqlSessionFactory() {
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        String dbUrl = String.format("jdbc:mysql://%s:%d/%s?useUnicode=true&characterEncoding=utf8", dbServer, dbPort, dbName);
        DataSource dataSource = new PooledDataSource("com.mysql.jdbc.Driver", dbUrl, dbUsername, dbPassword);
        Environment environment = new Environment("dev", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.getTypeAliasRegistry().registerAliases("domain");
        configuration.getMapperRegistry().addMappers("mapper");
        return new SqlSessionFactoryBuilder().build(configuration);
    }

    private static void deleteAllPersons() {
        SqlSession sqlSession = getSqlSessionFactory().openSession(true);
        PersonMapper personMapper = sqlSession.getMapper(PersonMapper.class);
        personMapper.deleteAllPersons();
    }

    private static void deletePerson(int id) {
        SqlSession sqlSession = getSqlSessionFactory().openSession(true);
        PersonMapper personMapper = sqlSession.getMapper(PersonMapper.class);
        personMapper.deletePerson(id);
    }

    private static void addPerson(long id, String firstName, String lastName, int age) {
        Person person = new Person();
        person.setId(id);
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setAge(age);

        SqlSession sqlSession = getSqlSessionFactory().openSession(true);
        PersonMapper personMapper = sqlSession.getMapper(PersonMapper.class);
        personMapper.insertPerson(person);
    }

    private static void printAllPersons() {
        SqlSession sqlSession = getSqlSessionFactory().openSession(true);
        PersonMapper personMapper = sqlSession.getMapper(PersonMapper.class);
        Set<Person> persons = personMapper.selectAllPersons();
        System.out.println("========");
        System.out.println("All Persons:");
        for (Person person : persons) {
            System.out.println(person);
        }
    }
}
