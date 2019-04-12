package de.adorsys.smartanalytics.core;

import de.adorsys.smartanalytics.pers.api.ConfigStatusEntity;
import de.adorsys.smartanalytics.pers.spi.StatusRepositoryIf;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static de.adorsys.smartanalytics.pers.api.ConfigStatusEntity.STATUS_ID;

@RequiredArgsConstructor
@Service
public class StatusService {

    private final StatusRepositoryIf statusRepo;

    public ConfigStatusEntity getStatus() {
        return statusRepo.findById(STATUS_ID).orElseGet(() -> {
            ConfigStatusEntity statusEntity = new ConfigStatusEntity();
            statusEntity.setId(STATUS_ID);
            statusRepo.save(statusEntity);
            return statusEntity;
        });
    }

    void rulesChanged() {
        rulesChanged(null);
    }

    void rulesChanged(String version) {
        ConfigStatusEntity status = getStatus();
        if (status.getRulesVersion() == null && version == null) {
            status.setRulesVersion(LocalDateTime.now().toString());
        } else if (status.getRulesVersion() != null && version == null) {
            status.setRulesVersion(StringUtils.substringBeforeLast(status.getRulesVersion(), "_") + "_" + LocalDateTime.now().toString());
        } else if (version != null) {
            status.setRulesVersion(version);
        }

        status.setLastChangeDate(LocalDateTime.now());
        statusRepo.save(status);
    }

    void groupConfigChanged(String version) {
        ConfigStatusEntity status = getStatus();
        if (status.getGroupConfigVersion() == null && version == null) {
            status.setGroupConfigVersion(LocalDateTime.now().toString());
        } else if (status.getGroupConfigVersion() != null && version == null) {
            status.setGroupConfigVersion(StringUtils.substringBeforeLast(status.getGroupConfigVersion(), "_") + "_" + LocalDateTime.now().toString());
        } else if (version != null) {
            status.setGroupConfigVersion(version);
        }

        status.setLastChangeDate(LocalDateTime.now());
        statusRepo.save(status);
    }

    void categoriesChanged(String version) {
        ConfigStatusEntity status = getStatus();
        if (status.getCategoriesVersion() == null && version == null) {
            status.setCategoriesVersion(LocalDateTime.now().toString());
        } else if (status.getCategoriesVersion() != null && version == null) {
            status.setCategoriesVersion(StringUtils.substringBeforeLast(status.getCategoriesVersion(), "_") + "_" + LocalDateTime.now().toString());
        } else if (version != null) {
            status.setCategoriesVersion(version);
        }

        status.setLastChangeDate(LocalDateTime.now());
        statusRepo.save(status);
    }

    void contractBlacklistChanged(String version) {
        ConfigStatusEntity status = getStatus();
        if (status.getContractBlackListVersion() == null && version == null) {
            status.setContractBlackListVersion(LocalDateTime.now().toString());
        } else if (status.getContractBlackListVersion() != null && version == null) {
            status.setContractBlackListVersion(StringUtils.substringBeforeLast(status.getContractBlackListVersion(),
                    "_") + "_" + LocalDateTime.now().toString());
        } else if (version != null) {
            status.setContractBlackListVersion(version);
        }

        status.setLastChangeDate(LocalDateTime.now());
        statusRepo.save(status);
    }

}
