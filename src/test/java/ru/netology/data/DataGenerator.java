package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataGenerator {
    private DataGenerator() {
    }

    private static final Faker faker = new Faker(new Locale("en"));

    @Value
    public static class CardInfo {
        private String number;
        private String status;
    }

    public static CardInfo getFirstCardInfo() {
        return new CardInfo("4444 4444 4444 4441", "APPROVED");
    }

    public static CardInfo getSecondCardInfo() {
        return new CardInfo("4444 4444 4444 4442", "DECLINED");
    }

    public static String generateСurrentMonth() {
        String month = String.valueOf(LocalDate.now().getMonth().getValue());
        return month;
    }

    public static String generateСurrentYear() {
        String year = Year.now().format(DateTimeFormatter.ofPattern("uu"));
        return year;
    }

    public static String generateName() {
        String name = faker.name().lastName().concat(" ") + faker.name().firstName();
        return name;
    }

    public static String generateCVC() {
        String cvc = faker.numerify("###");
        return cvc;
    }

}
