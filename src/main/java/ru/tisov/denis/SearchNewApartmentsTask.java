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
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SearchNewApartmentsTask {

    private static final Logger log = LoggerFactory.getLogger(SearchNewApartmentsTask.class);
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final RestTemplate restTemplate = restTemplate();
    private final Bot bot;

    @Value("${application.apartments.file}")
    private String apartmentsPath;

    public SearchNewApartmentsTask(Bot bot) {
        this.bot = bot;
    }

    @Scheduled(initialDelay = 0, fixedRate = 3_600_000)
    public void reportCurrentTime() throws IOException {
        LocalDate searchDate = LocalDate.now();

        Set<String> newIds = getResults(searchDate);

        while (searchDate.getMonth() != Month.JUNE) {
            searchDate = searchDate.plusDays(5);
            newIds.addAll(getResults(searchDate));
        }

        createFileIfNotExist();
        List<String> oldIds = Files.lines(Paths.get(apartmentsPath)).collect(Collectors.toList());
        newIds.removeAll(oldIds);

        if (newIds.isEmpty()) {
            bot.sendMessage("Не найдено новых квартир");
            return;
        }

        StringBuilder message = new StringBuilder("Новые квартиры на wunderflats.com: \n");

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
        String url = "https://wunderflats.com/api/regions/13.250200376,52.524710051;13.372938260,52.478208733/query?minAccommodates=1&maxPrice=100000&bbox=13.250200376,52.524710051%3B13.372938260,52.478208733&availableFrom=" + dateFormat.format(from) + "&itemsPerPage=30";
        WunderflatsResult results = restTemplate.getForObject(url, WunderflatsResult.class);
        return results != null ? results.getItems().stream().map(Item::getId).collect(Collectors.toSet()) : new HashSet<>();
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