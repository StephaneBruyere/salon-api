package com.salon.mocks;

import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;

import com.salon.domain.model.SalonServiceDetail;
import com.salon.domain.model.Slot;
import com.salon.domain.model.SlotStatus;
import com.salon.domain.repository.SalonServiceDetailRepo;
import com.salon.domain.repository.SlotRepo;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
public class TestDbInitializationService {


    final static int DAYS_TO_POPULATE = 30;
    SalonServiceDetailRepo salonServiceDetailRepository;
    SlotRepo slotRepository;

    public TestDbInitializationService(SalonServiceDetailRepo salonServiceDetailRepository, SlotRepo slotRepository) {
        this.salonServiceDetailRepository = salonServiceDetailRepository;
        this.slotRepository = slotRepository;
    }

    public void initDb() {
        if (hasData() == false) {
            addDefaultPackageNames();
            addDefaultSlots();
        }
    }

    public boolean hasData() {
        return salonServiceDetailRepository.count() > 0;
    }


    @Transactional
    public void addDefaultPackageNames() {
        List<SalonServiceDetail> serviceDetails = getDefaultSalonServiceDetails();
        salonServiceDetailRepository.saveAll(serviceDetails);
    }

    List<SalonServiceDetail> getDefaultSalonServiceDetails() {
        List<SalonServiceDetail> serviceDetails = new ArrayList<>();
        serviceDetails.add(createFrom("Anti-aging Treatments"));
        serviceDetails.add(createFrom("Body Massages"));
        serviceDetails.add(createFrom("Body Treatments"));
        return serviceDetails;
    }

    private SalonServiceDetail createFrom(String service) {
        SalonServiceDetail salonServiceDetail = new SalonServiceDetail();
        salonServiceDetail.setDescription(service + " from AR Salon");
        salonServiceDetail.setName(service);
        salonServiceDetail.setPrice(getRandomNumberBetweenTenToTwoHundred());
        salonServiceDetail.setTimeInMinutes(getRandomMinutesBetweenThirtytoHundredTwenty());
        return salonServiceDetail;
    }

    private Long getRandomNumberBetweenTenToTwoHundred() {
        int min = 10;
        int max = 200;
        Random r = new Random();
        int i = r.nextInt((max - min) + 1) + min;
        return Long.valueOf(i);
    }

    private int getRandomMinutesBetweenThirtytoHundredTwenty() {
        int min = 3;
        int max = 12;
        Random r = new Random();
        int i = r.nextInt((max - min) + 1) + min;
        return i * 10;
    }

    @Transactional
    public void addDefaultSlots() {
        List<SalonServiceDetail> allSalonServices = (List<SalonServiceDetail>)salonServiceDetailRepository.findAll();
        Map<String, MockStylist> stylistServices = getStylistDetails(allSalonServices);
        log.info(stylistServices.size() + "");
        IntStream.rangeClosed(0, DAYS_TO_POPULATE)
                .mapToObj(value -> LocalDateTime.now().plusDays(value))
                .flatMap(createdDate -> getSlotsOnDate(stylistServices, createdDate))
                .forEach(slot -> saveSlot(slot));
    }

    private void saveSlot(Slot slot) {
        slotRepository.save(slot);
    }

    private Stream<Slot> getSlotsOnDate(Map<String, MockStylist> stylistServices, LocalDateTime dateTime) {
        List<String> stylists = getDefaultStylists();
        return stylists.stream().flatMap(name -> {
            return getSlotOnDate(stylistServices, dateTime, name).stream();
        });
    }

    private List<Slot> getSlotOnDate(Map<String, MockStylist> stylistServices, LocalDateTime dateTime, String name) {
        MockStylist mockStylist = stylistServices.get(name);
        return mockStylist.slots.stream().map(hour -> {
            return getSlotFrom(name, mockStylist, dateTime, hour);
        }).collect(Collectors.toList());
    }

    private Slot getSlotFrom(String name, MockStylist mockStylist, LocalDateTime dateTime, int hour) {

        LocalDateTime localDateTime = dateTime.toLocalDate().atTime(hour, 0);
        Slot slot = new Slot();
        slot.setAvailableServices(mockStylist.servicesAsSet());
        slot.setStatus(SlotStatus.AVAILABLE);
        slot.setSlotFor(localDateTime);
        slot.setStylistName(name);
        return slot;

    }


    Map<String, MockStylist> getStylistDetails(List<SalonServiceDetail> allSalonServices) {
        List<String> stylists = getDefaultStylists();
        List<List<SalonServiceDetail>> groupedServices = getChunkedSalonServices(allSalonServices, stylists);
        Map<String, MockStylist> stylistDetails = new HashMap<>();
        for (int i = 0; i < stylists.size(); i++) {

            MockStylist mockStylist = new MockStylist();
            String name = stylists.get(i);
            mockStylist.name = name;
            mockStylist.services = groupedServices.get(i);
            mockStylist.slots = getSlotsAt(i);

            log.info(mockStylist.toString());
            stylistDetails.put(name, mockStylist);
        }
        return stylistDetails;
    }


    List<List<SalonServiceDetail>> getChunkedSalonServices(List<SalonServiceDetail> allSalonServices, List<String> stylists) {
//        int groupSize = allSalonServices.size();
        int size = stylists.size();
        List<List<SalonServiceDetail>> groupedServices = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            groupedServices.add(allSalonServices.subList(0, allSalonServices.size() - 1));
        }
        return groupedServices;
    }

    List<String> getDefaultStylists() {
        String[] stylistsArray = {"George Wagner", "Clint Meyer"};
        return Arrays.asList(stylistsArray);
    }

    List<Integer> getSlotsAt(int index) {
        Integer[] slotTimings = {10, 11, 12};
        Integer[] eveningSlotTimings = {14, 15, 16};

        index = index % slotTimings.length;

        List<Integer> slotTimes = new ArrayList<>();
        slotTimes.add(slotTimings[index]);
        slotTimes.add(eveningSlotTimings[index]);

        return slotTimes;
    }

}
