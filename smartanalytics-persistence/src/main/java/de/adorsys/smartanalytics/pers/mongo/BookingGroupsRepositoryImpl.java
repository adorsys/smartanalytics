package de.adorsys.smartanalytics.pers.mongo;

import de.adorsys.smartanalytics.api.config.GroupConfig;
import de.adorsys.smartanalytics.pers.api.BookingGroupConfigEntity;
import de.adorsys.smartanalytics.pers.spi.BookingGroupRepositoryIf;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Profile({"mongo-persistence", "fongo"})
@Service
public class BookingGroupsRepositoryImpl implements BookingGroupRepositoryIf {

    @Autowired
    private BookingGroupsRepositoryMongodb groupsRepository;

    @Override
    public Optional<BookingGroupConfigEntity> getBookingGroups() {
        return groupsRepository.findById(BookingGroupConfigEntity.CONTAINER_ID);
    }

    @Override
    public void saveBookingGroups(GroupConfig groupsContainer) {
        BookingGroupConfigEntity containerEntity = new BookingGroupConfigEntity();
        BeanUtils.copyProperties(groupsContainer, containerEntity);

        containerEntity.setId(BookingGroupConfigEntity.CONTAINER_ID);
        containerEntity.setChangeDate(LocalDate.now());

        groupsRepository.deleteById(BookingGroupConfigEntity.CONTAINER_ID);
        groupsRepository.save(containerEntity);
    }
}

