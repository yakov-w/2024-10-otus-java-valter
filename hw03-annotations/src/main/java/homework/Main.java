package homework;

import homework.real.RunningTests;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws ClassNotFoundException {
        Set<String> testClasses = Set.of("homework.SomeClassTestOne", "homework.SomeClassTestTwo");
        RunningTests rt = new RunningTests(testClasses);

        LOGGER.info("Begin execution TESTS!");
        rt.run();
        LOGGER.info("End execution TESTS!");
    }
}
