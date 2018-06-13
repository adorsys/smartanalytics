package de.adorsys.smartanalytics.core;

import de.adorsys.smartanalytics.pers.api.StatusEntity;
import de.adorsys.smartanalytics.pers.spi.RuleRepositoryIf;
import de.adorsys.smartanalytics.pers.spi.StatusRepositoryIf;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static de.adorsys.smartanalytics.pers.api.StatusEntity.STATUS_ID;

@Service
public class StatusService {

    @Autowired
    private StatusRepositoryIf statusRepo;
    @Autowired
    private RuleRepositoryIf ruleRepository;

    public StatusEntity getStatus() {
        return statusRepo.findById(STATUS_ID).orElseGet(() -> {
            StatusEntity statusEntity = new StatusEntity();
            statusEntity.setId(STATUS_ID);
            statusEntity.setCountRules(ruleRepository.count());
            statusRepo.save(statusEntity);
            return statusEntity;
        });
    }

    public void rulesChanged() {
        rulesChanged(null);
    }

    public void rulesChanged(String version) {
        rulesChanged(version, ruleRepository.count());
    }

    private void rulesChanged(String version, long countRules) {
        StatusEntity status = getStatus();
        if (status.getVersion() == null && version == null) {
            status.setVersion(LocalDateTime.now().toString());
        } else if (status.getVersion() != null && version == null) {
            status.setVersion(StringUtils.substringBeforeLast(status.getVersion(), "_")  + "_" + LocalDateTime.now().toString());
        } else if (version != null) {
            status.setVersion(version);
        }

        status.setCountRules(countRules);
        status.setLastChangeDate(LocalDateTime.now());
        statusRepo.save(status);
    }

}
