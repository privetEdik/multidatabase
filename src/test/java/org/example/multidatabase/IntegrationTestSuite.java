package org.example.multidatabase;



import org.example.multidatabase.integration.DatabaseFailureTests;
import org.example.multidatabase.integration.MultiDatabaseApplicationTests;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Custom Ordered Integration Tests")
@SelectClasses({
        MultiDatabaseApplicationTests.class,
        DatabaseFailureTests.class
})
public class IntegrationTestSuite {
}
