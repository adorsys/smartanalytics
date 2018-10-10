package de.adorsys.smartanalytics.pers.api;

import de.adorsys.smartanalytics.api.config.GroupConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(callSuper = false)
@Document
public class BookingGroupConfigEntity extends GroupConfig {

    public static final String CONTAINER_ID = "groups";

    @Id
    private String id;
}
