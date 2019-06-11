package ru.tisov.denis;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;
import ru.tisov.denis.dto.Item;
import ru.tisov.denis.dto.WunderflatsResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class SearchNewApartmentsTask {

    private static final Logger log = LoggerFactory.getLogger(SearchNewApartmentsTask.class);
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int people = 2;
    private static final int minRooms = 2;
    private static final int maxPrice = 120000;

    private final RestTemplate restTemplate = restTemplate();
    private final Bot bot;

    @Value("${application.apartments.file}")
    private String apartmentsPath;

    public SearchNewApartmentsTask(Bot bot) {
        this.bot = bot;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void reportCurrentTime() throws IOException {
        System.out.println("Start");
        LocalDate searchDate = LocalDate.of(2019, Month.NOVEMBER, 1);

        Set<String> newIds = getResults(searchDate);

        createFileIfNotExist();
        List<String> oldIds = Files.lines(Paths.get(apartmentsPath)).collect(Collectors.toList());
        newIds.removeAll(oldIds);

        if (newIds.isEmpty()) {
            if (LocalDateTime.now().atZone(ZoneId.of("GMT+2")).getHour() == 7) {
                bot.sendMessage("Доброе утро!");
            }
            if (LocalDateTime.now().atZone(ZoneId.of("GMT+2")).getHour() == 22) {
                bot.sendMessage("Спокойной ночи!");
            }
            return;
        }

        StringBuilder message = new StringBuilder("Новые квартиры: \n");

        int i = 0;
        for (String id : newIds) {
            message.append(i + 1).append(". ").append(toResultUrl(id)).append("\n");
            i++;
        }

        bot.sendMessage(message.toString());

        newIds.addAll(oldIds);
        Files.write(Paths.get(apartmentsPath), newIds);
    }

    private Set<String> getResults(LocalDate from) {
        Set<String> results = getResultsFromWunderflats(from);
        results.addAll(getResultsFromComingHome(from));
        return results;
    }

    private Set<String> getResultsFromWunderflats(LocalDate from) {
        String url = "https://wunderflats.com/api/regions/13.250200376,52.524710051;13.372938260,52.478208733/query?minAccommodates=" + people + "&maxPrice=" + maxPrice + "&&minRooms=" + minRooms + "bbox=13.250200376,52.524710051%3B13.372938260,52.478208733&availableFrom=" + dateFormat.format(from) + "&itemsPerPage=30";
        WunderflatsResult results = restTemplate.getForObject(url, WunderflatsResult.class);
        return results != null ? results.getItems().stream().map(Item::getId).filter(Objects::nonNull).collect(Collectors.toSet()) : new HashSet<>();
    }

    private Set<String> getResultsFromComingHome(LocalDate from) {
        return Collections.emptySet();
    }

    private RestTemplate restTemplate() {
        CloseableHttpClient httpClient
                = HttpClients.custom()
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory
                = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);

        return new RestTemplate(requestFactory);

    }

    private String toResultUrl(String id) {
        return "https://wunderflats.com/en/furnished-apartment/fantastic-lovely-flat/" + id;
    }

    private void createFileIfNotExist() {
        try {
            if (!Files.exists(Paths.get(apartmentsPath))) {
                Files.createFile(Paths.get(apartmentsPath));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}