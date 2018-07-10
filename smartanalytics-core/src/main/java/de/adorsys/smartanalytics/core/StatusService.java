package de.adorsys.smartanalytics.core;

import de.adorsys.smartanalytics.pers.api.ConfigStatusEntity;
import de.adorsys.smartanalytics.pers.spi.RuleRepositoryIf;
import de.adorsys.smartanalytics.pers.spi.StatusRepositoryIf;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static de.adorsys.smartanalytics.pers.api.ConfigStatusEntity.STATUS_ID;

@Service
public class StatusService {

    @Autowired
    private StatusRepositoryIf statusRepo;

    public ConfigStatusEntity getStatus() {
        return statusRepo.findById(STATUS_ID).orElseGet(() -> {
            ConfigStatusEntity statusEntity = new ConfigStatusEntity();
            statusEntity.setId(STATUS_ID);
            statusRepo.save(statusEntity);
            return statusEntity;
        });
    }

    public void rulesChanged() {
        rulesChanged(null);
    }

    public void rulesChanged(String version) {
        ConfigStatusEntity status = getStatus();
        if (status.getRulesVersion() == null && version == null) {
            status.setRulesVersion(LocalDateTime.now().toString());
        } else if (status.getRulesVersion() != null && version == null) {
            status.setRulesVersion(StringUtils.substringBeforeLast(status.getRulesVersion(), "_")  + "_" + LocalDateTime.now().toString());
        } else if (version != null) {
            status.setRulesVersion(version);
        }

        status.setLastChangeDate(LocalDateTime.now());
        statusRepo.save(status);
    }

    public void groupConfigChanged(String version) {
        ConfigStatusEntity status = getStatus();
        if (status.getGroupConfigVersion() == null && version == null) {
            status.setGroupConfigVersion(LocalDateTime.now().toString());
        } else if (status.getGroupConfigVersion() != null && version == null) {
            status.setGroupConfigVersion(StringUtils.substringBeforeLast(status.getGroupConfigVersion(), "_")  + "_" + LocalDateTime.now().toString());
        } else if (version != null) {
            status.setGroupConfigVersion(version);
        }

        status.setLastChangeDate(LocalDateTime.now());
        statusRepo.save(status);
    }

    public void categoriesChanged(String version) {
        ConfigStatusEntity status = getStatus();
        if (status.getCategoriesVersion() == null && version == null) {
            status.setCategoriesVersion(LocalDateTime.now().toString());
        } else if (status.getCategoriesVersion() != null && version == null) {
            status.setCategoriesVersion(StringUtils.substringBeforeLast(status.getCategoriesVersion(), "_")  + "_" + LocalDateTime.now().toString());
        } else if (version != null) {
            status.setCategoriesVersion(version);
        }

        status.setLastChangeDate(LocalDateTime.now());
        statusRepo.save(status);
    }

    public void contractBlacklistChanged(String version) {
        ConfigStatusEntity status = getStatus();
        if (status.getContractBlackListVersion() == null && version == null) {
            status.setContractBlackListVersion(LocalDateTime.now().toString());
        } else if (status.getContractBlackListVersion() != null && version == null) {
            status.setContractBlackListVersion(StringUtils.substringBeforeLast(status.getContractBlackListVersion(), "_")  + "_" + LocalDateTime.now().toString());
        } else if (version != null) {
            status.setContractBlackListVersion(version);
        }

        status.setLastChangeDate(LocalDateTime.now());
        statusRepo.save(status);
    }

}
