package de.adorsys.smartanalytics.pers.api;

import de.adorsys.smartanalytics.api.Rule;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "smartanalytics-rules")
public class RuleEntity extends Rule {

    @Id
    private String id;
    @Indexed
    private String userId;
    @Indexed
    private List<String> searchIndex;

    public void updateSearchIndex() {
        searchIndex = new ArrayList<>();
        if (getCreditorId() != null) {
            searchIndex.add(getCreditorId());
        }

        if (getRuleId() != null) {
            searchIndex.add(getRuleId());
        }

        if (getReceiver() != null) {
            searchIndex.add(getReceiver());
        }
    }

}
