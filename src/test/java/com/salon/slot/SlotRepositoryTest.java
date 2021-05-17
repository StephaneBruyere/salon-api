package com.salon.slot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsEqual.equalTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.salon.domain.model.SalonServiceDetail;
import com.salon.domain.model.Slot;
import com.salon.domain.model.SlotStatus;
import com.salon.domain.repository.SalonServiceDetailRepo;
import com.salon.domain.repository.SlotRepo;
import com.salon.mocks.TestDbInitializationService;

@DataJpaTest
class SlotRepositoryTest {

    @Autowired
    SlotRepo slotRepository;

    @Autowired
    SalonServiceDetailRepo salonServiceDetailRepository;

    @BeforeEach
    public void before() {
        TestDbInitializationService testDB = new TestDbInitializationService(salonServiceDetailRepository, slotRepository);
        testDB.initDb();
    }

    @Test
    void countShouldBeGreaterThanZero() {
        List<Slot> slots = (List<Slot>) slotRepository.findAll();
        assertThat(slots.size(), greaterThan(0));
    }

    @Test
    void findAllBySlotForGreaterThanEqualAndSlotForLessThanEqualAndAvailableServicesContainingAndStatusShouldGiveExactValueForAvailableSlots() {
        LocalDate localDate = LocalDate.now().plusDays(2);
        List<Slot> results = getSlotsOnDayForService(getASalonServiceDetail(), localDate);
        assertResults(localDate, results);
    }

    private void assertResults(LocalDate localDate, List<Slot> results) {
        Long validCount = getValidCountFromResults(localDate, results);
        assertThat(results.size(), equalTo(validCount.intValue()));
    }

    private Long getValidCountFromResults(LocalDate localDate, List<Slot> results) {
        return results.stream()
                .filter(slot -> slot.getStatus().equals(SlotStatus.AVAILABLE))
                .filter(slot -> slot.getSlotFor().getDayOfMonth() == localDate.getDayOfMonth())
                .count();
    }

    private SalonServiceDetail getASalonServiceDetail() {
        List<SalonServiceDetail> services = (List<SalonServiceDetail>) salonServiceDetailRepository.findAll();
        return services.get(0);
    }

    @Test
    void findAllBySlotForGreaterThanEqualAndSlotForLessThanEqualAndAvailableServicesContainingAndStatusShouldGiveExactValueForSomeAvailableSlots() {
        SalonServiceDetail salonServiceDetail = getASalonServiceDetail();
        LocalDate localDate = LocalDate.now().plusDays(2);
        List<Slot> slotsOnDayForService = getSlotsOnDayForService(salonServiceDetail, localDate);
        slotsOnDayForService.stream()
                .limit(2)
                .forEach(this::saveSlotWithConfirmed);
        List<Slot> results = getSlotsOnDayForService(getASalonServiceDetail(), localDate);
        assertResults(localDate, results);
        assertThat(results.size(), not(slotsOnDayForService.size()));
    }

    private void saveSlotWithConfirmed(Slot slot) {
        slot.setStatus(SlotStatus.CONFIRMED);
        slotRepository.save(slot);
    }

    private List<Slot> getSlotsOnDayForService(SalonServiceDetail salonServiceDetail, LocalDate localDate) {
        LocalDateTime startDate = localDate.atTime(0, 1);
        LocalDateTime endDate = localDate.atTime(23, 59);
        return slotRepository.findAllBySlotForGreaterThanEqualAndSlotForLessThanEqualAndAvailableServicesContainingAndStatus(startDate, endDate, salonServiceDetail, SlotStatus.AVAILABLE);
    }
}