package de.adorsys.smartanalytics.pers.api;

import de.adorsys.smartanalytics.api.RulesStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(callSuper = false)
@Document
public class StatusEntity extends RulesStatus {

    public static final String STATUS_ID = "status";

    @Id
    private String id;
}
