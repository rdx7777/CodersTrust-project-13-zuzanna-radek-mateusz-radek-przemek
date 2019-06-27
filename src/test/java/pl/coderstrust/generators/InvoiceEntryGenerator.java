package pl.coderstrust.generators;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;

import pl.coderstrust.model.InvoiceEntry;
import pl.coderstrust.model.Vat;

public class InvoiceEntryGenerator {

    public static InvoiceEntry getRandomEntry() {
        Long id = IdGenerator.getNextId();
        String description = WordGenerator.getRandomWord();
        long quantity = ThreadLocalRandom.current().nextLong(1, 999);
        BigDecimal price = BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(1, 50));
        BigDecimal netValue = price.multiply(BigDecimal.valueOf(quantity));
        Vat vatRate = VatRateGenerator.getRandomVatRate(Vat.class);
        BigDecimal vatValue = netValue.multiply(BigDecimal.valueOf(vatRate.getValue())).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal grossValue = netValue.add(vatValue);

        return new InvoiceEntry(id, description, quantity, price, netValue, grossValue, vatRate);
    }
}