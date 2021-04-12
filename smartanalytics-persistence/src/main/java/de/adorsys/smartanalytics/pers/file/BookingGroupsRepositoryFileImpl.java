package de.adorsys.smartanalytics.pers.file;

import de.adorsys.smartanalytics.api.config.GroupConfig;
import de.adorsys.smartanalytics.pers.api.BookingGroupConfigEntity;
import de.adorsys.smartanalytics.pers.spi.BookingGroupRepositoryIf;
import de.adorsys.smartanalytics.pers.utils.ImportUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Profile({"file-persistence"})
@Service
public class BookingGroupsRepositoryFileImpl implements BookingGroupRepositoryIf {

    private static final String BOOKING_GROUPS_YML = "booking-groups.yml";

    @Value("${smartanalytics.conf.path:#{systemProperties['java.io.tmpdir']}}")
    private File confDir;

    private BookingGroupConfigEntity bookingGroupConfigEntity;

    @PostConstruct
    public void postConstruct() throws IOException {
        File categoriesFile = new File(confDir, BOOKING_GROUPS_YML);
        if (categoriesFile.exists()) {
            bookingGroupConfigEntity = ImportUtils.readAsYaml(categoriesFile, BookingGroupConfigEntity.class);
        }
    }

    @Override
    public Optional<BookingGroupConfigEntity> getBookingGroups() {
        return Optional.ofNullable(bookingGroupConfigEntity);
    }

    @Override
    public void saveBookingGroups(GroupConfig groupsContainer) {
        bookingGroupConfigEntity = new BookingGroupConfigEntity();
        BeanUtils.copyProperties(groupsContainer, bookingGroupConfigEntity);

        try {
            ImportUtils.writeObjectToYaml(new File(confDir, BOOKING_GROUPS_YML), bookingGroupConfigEntity);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}

