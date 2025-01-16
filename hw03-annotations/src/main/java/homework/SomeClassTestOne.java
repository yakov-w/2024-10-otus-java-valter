package homework;

import homework.annotation.After;
import homework.annotation.Before;
import homework.annotation.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unchecked")
public class SomeClassTestOne {

    private static final Logger LOGGER = LoggerFactory.getLogger(SomeClassTestOne.class);

    @Before
    void before() {
        LOGGER.info("@Before. ");
        LOGGER.info("Экземпляр тестового класса: {}", Integer.toHexString(hashCode()));
    }

    @After
    void after() {
        LOGGER.info("@After. ");
        LOGGER.info("Экземпляр тестового класса: {}", Integer.toHexString(hashCode()));
    }

    @Test
    void goodTest1() {
        LOGGER.info("@Test. goodTest1");
        LOGGER.info("Экземпляр тестового класса: {}", Integer.toHexString(hashCode()));
    }

    @Test
    void goodTest2() {
        LOGGER.info("@Test. goodTest2");
        LOGGER.info("Экземпляр тестового класса: {}", Integer.toHexString(hashCode()));
    }

    @Test
    void badTest1() throws Exception {
        LOGGER.info("@Test. badTest1");
        LOGGER.info("Экземпляр тестового класса: {}", Integer.toHexString(hashCode()));
        throw new Exception("Шеф, все пропало!");
    }

    @Test
    void badTest2() {
        LOGGER.info("@Test. badTest2");
        LOGGER.info("Экземпляр тестового класса: {}", Integer.toHexString(hashCode()));
        int a = 115/0;
    }
}
