package de.adorsys.smartanalytics.pers.api;

import de.adorsys.smartanalytics.api.config.ContractBlacklist;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class ContractBlacklistEntity extends ContractBlacklist {

    public static final String CONTAINER_ID = "contract-blacklist";

    @Id
    private String id;
}
