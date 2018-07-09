package de.adorsys.smartanalytics.pers.api;

import de.adorsys.smartanalytics.api.BookingGroupConfig;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class BookingGroupConfigEntity extends BookingGroupConfig {

    public static final String CONTAINER_ID = "groups";

    @Id
    private String id;
}