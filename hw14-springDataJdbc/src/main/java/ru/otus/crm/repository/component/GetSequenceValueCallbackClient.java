package ru.otus.crm.repository.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.relational.core.mapping.event.BeforeConvertCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.otus.crm.model.Client;

import java.sql.SQLException;

@Component
public class GetSequenceValueCallbackClient implements BeforeConvertCallback<Client> {

    private static final Logger LOG = LoggerFactory.getLogger(GetSequenceValueCallbackClient.class);

    private final JdbcTemplate jdbcTemplate;

    public GetSequenceValueCallbackClient(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Client onBeforeConvert(Client client) {
        if (client.getId() == null) {
            LOG.info("Get the next value from a database sequence and use it as the primary key");

            Long id = jdbcTemplate.query("SELECT nextval('client_SEQ')",
                    rs -> {
                        if (rs.next()) {
                            return rs.getLong(1);
                        } else {
                            throw new SQLException("Unable to retrieve value from sequence client_SEQ.");
                        }
                    });
            client.setId(id);
        }

        return client;
    }
}
