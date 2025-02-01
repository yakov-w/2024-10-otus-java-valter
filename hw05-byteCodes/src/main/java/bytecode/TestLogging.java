package bytecode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLogging implements TestLoggingInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestLogging.class);

    @Log
    @Override
    public void calculation(int param) {
        LOGGER.info("{}", Math.pow(param, param));
    }

    @Log
    @Override
    public void calculation(int param, int param2) {
        LOGGER.info("{}", param ^ param2);
    }

    @Log
    @Override
    public void calculation(int param, String mes) {
        LOGGER.info("{} - {}", mes, param);
    }

    @Override
    public void calculation(String mes, String mes2) {
        LOGGER.info("{} - {}", mes, mes2);
    }
}