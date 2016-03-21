package scheduler.app.repositories;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.ext.h2.H2Connection;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import scheduler.config.HibernateTestConfig;

import java.sql.SQLException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AbstractIntegrationTest.Config.class, HibernateTestConfig.class})
@DbUnitConfiguration(databaseConnection = "testConnection")
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class
})
public abstract class AbstractIntegrationTest {

    protected final static TestUser USER_CURRY = new TestUser(1L, "Steph Curry");
    protected final static TestUser USER_IBAKA = new TestUser(2L, "Serge Ibaka");
    protected final static TestUser USER_DURANT = new TestUser(3L, "Kevin Durant");

    @Configuration
    @EnableJpaRepositories("scheduler.app.repositories")
    @ComponentScan({
            "scheduler.app.repositories",
            "scheduler.app.services",
            "scheduler.app.converters"
    })
    public static class Config {
        @Bean
        public org.dbunit.ext.h2.H2Connection testConnection(final DriverManagerDataSource dataSource) throws DatabaseUnitException, SQLException {
            H2Connection connection = new H2Connection(dataSource.getConnection(), HibernateTestConfig.TEST_SCHEMA_NAME);

            DatabaseConfig config = connection.getConfig();
            config.setProperty(DatabaseConfig.FEATURE_BATCHED_STATEMENTS, true);

            return connection;
        }
    }

    @BeforeClass
    public static void setUp() throws SQLException {
    }

    @AfterClass
    public static void tearDown() {
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class TestUser {
        private Long id;
        private String userName;
    }
}