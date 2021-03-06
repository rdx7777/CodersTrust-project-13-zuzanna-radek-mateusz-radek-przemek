package pl.coderstrust.database;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import pl.coderstrust.configuration.InFileDatabaseProperties;
import pl.coderstrust.helpers.FileHelper;
import pl.coderstrust.model.Invoice;

@Repository
@ConditionalOnProperty(name = "pl.coderstrust.database", havingValue = "in-file")
public class InFileDatabase implements Database {

    private Logger log = LoggerFactory.getLogger(InFileDatabase.class);
    private String filePath;
    private ObjectMapper mapper;
    private FileHelper fileHelper;
    private AtomicLong nextId;

    @Autowired
    public InFileDatabase(InFileDatabaseProperties inFileDatabaseProperties, ObjectMapper mapper, FileHelper fileHelper) throws IOException {
        this.filePath = inFileDatabaseProperties.getFilePath();
        this.mapper = mapper;
        this.fileHelper = fileHelper;
        init();
    }

    private void init() throws IOException {
        if (!fileHelper.exists(filePath)) {
            fileHelper.create(filePath);
        }
        nextId = new AtomicLong(getLastInvoiceId());
    }

    private long getLastInvoiceId() throws IOException {
        String lastInvoiceAsJson = fileHelper.readLastLine(filePath);
        if (lastInvoiceAsJson == null) {
            return 0;
        }
        Invoice invoice = deserializeJsonToInvoice(lastInvoiceAsJson);
        if (invoice == null) {
            return 0;
        }
        return invoice.getId();
    }

    @Override
    public synchronized Invoice save(Invoice invoice) throws DatabaseOperationException {
        if (invoice == null) {
            log.error("Attempt to save null invoice.");
            throw new IllegalArgumentException("Passed invoice cannot be null.");
        }
        try {
            if (invoice.getId() == null || !isInvoiceExist(invoice.getId())) {
                return insertInvoice(invoice);
            }
            return updateInvoice(invoice);
        } catch (IOException e) {
            String message = "An error occurred during saving invoice.";
            log.error(message, e);
            throw new DatabaseOperationException(message, e);
        }
    }

    private Invoice insertInvoice(Invoice invoice) throws IOException {
        Invoice insertedInvoice = Invoice.builder()
            .withId(nextId.incrementAndGet())
            .withNumber(invoice.getNumber())
            .withIssuedDate(invoice.getIssuedDate())
            .withDueDate(invoice.getDueDate())
            .withSeller(invoice.getSeller())
            .withBuyer(invoice.getBuyer())
            .withEntries(invoice.getEntries())
            .build();
        fileHelper.writeLine(filePath, mapper.writeValueAsString(insertedInvoice));
        return insertedInvoice;
    }

    private Invoice updateInvoice(Invoice invoice) throws DatabaseOperationException, IOException {
        fileHelper.replaceLine(filePath, mapper.writeValueAsString(invoice), getPositionInDatabase(invoice.getId()));
        return invoice;
    }

    @Override
    public synchronized void delete(Long id) throws DatabaseOperationException {
        if (id == null) {
            log.error("Attempt to delete invoice providing null id.");
            throw new IllegalArgumentException("Passed id cannot be null.");
        }
        try {
            fileHelper.removeLine(filePath, getPositionInDatabase(id));
        } catch (IOException e) {
            String message = "An error occurred during deleting invoice.";
            log.error(message, e);
            throw new DatabaseOperationException(message, e);
        }
    }

    @Override
    public Optional<Invoice> getById(Long id) throws DatabaseOperationException {
        if (id == null) {
            log.error("Attempt to get invoice by id providing null id.");
            throw new IllegalArgumentException("Passed id cannot be null.");
        }
        try {
            try (Stream<Invoice> stream = getInvoices()) {
                return stream.filter(invoice -> invoice.getId().equals(id))
                    .findFirst();
            }
        } catch (IOException e) {
            String message = "An error occurred during getting invoice by id.";
            log.error(message, e);
            throw new DatabaseOperationException(message, e);
        }
    }

    @Override
    public Optional<Invoice> getByNumber(String number) throws DatabaseOperationException {
        if (number == null) {
            log.error("Attempt to get invoice by number providing null number.");
            throw new IllegalArgumentException("Passed id cannot be null.");
        }
        try {
            try (Stream<Invoice> stream = getInvoices()) {
                return stream
                    .filter(invoice -> invoice.getNumber().equals(number))
                    .findFirst();
            }
        } catch (IOException e) {
            String message = "An error occurred during getting invoice by number.";
            log.error(message, e);
            throw new DatabaseOperationException(message, e);
        }
    }

    @Override
    public Collection<Invoice> getAll() throws DatabaseOperationException {
        try {
            try (Stream<Invoice> stream = getInvoices()) {
                return stream.collect(Collectors.toList());
            }
        } catch (IOException e) {
            String message = "An error occurred during getting all invoices.";
            log.error(message, e);
            throw new DatabaseOperationException(message, e);
        }
    }

    @Override
    public synchronized void deleteAll() throws DatabaseOperationException {
        try {
            fileHelper.clear(filePath);
        } catch (IOException e) {
            String message = "An error occurred during deleting all invoices.";
            log.error(message, e);
            throw new DatabaseOperationException(message, e);
        }
    }

    @Override
    public boolean exists(Long id) throws DatabaseOperationException {
        if (id == null) {
            log.error("Attempt to check if invoice exists providing null id.");
            throw new IllegalArgumentException("Passed id cannot be null.");
        }
        try {
            return isInvoiceExist(id);
        } catch (IOException e) {
            String message = "An error occurred during checking if invoice exists.";
            log.error(message, e);
            throw new DatabaseOperationException(message, e);
        }
    }

    @Override
    public long count() throws DatabaseOperationException {
        try {
            try (Stream<Invoice> stream = getInvoices()) {
                return stream.count();
            }
        } catch (IOException e) {
            String message = "An error occurred during getting number of invoices.";
            log.error(message, e);
            throw new DatabaseOperationException(message, e);
        }
    }

    @Override
    public Collection<Invoice> getByIssueDate(LocalDate startDate, LocalDate endDate) throws DatabaseOperationException {
        if (startDate == null) {
            log.error("Attempt to get invoices from date interval without providing start date");
            throw new IllegalArgumentException("Start date cannot be null");
        }
        if (endDate == null) {
            log.error("Attempt to get invoices from date interval without providing end date");
            throw new IllegalArgumentException("End date cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            log.error("Attempt to get invoices from date interval when passed start date is after end date");
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        try {
            try (Stream<Invoice> stream = getInvoices()) {
                return stream
                    .filter(invoice -> invoice.getIssuedDate().compareTo(startDate) >= 0 && invoice.getIssuedDate().compareTo(endDate) <= 0)
                    .collect(Collectors.toList());
            }
        } catch (IOException e) {
            String message = "An error occurred during getting invoices filtered by issued date";
            log.error(message, e);
            throw new DatabaseOperationException(message);
        }
    }

    private Invoice deserializeJsonToInvoice(String json) {
        try {
            return mapper.readValue(json, Invoice.class);
        } catch (IOException e) {
            return null;
        }
    }

    private boolean isInvoiceExist(long id) throws IOException {
        try (Stream<Invoice> stream = getInvoices()) {
            return stream
                .anyMatch(invoice -> invoice.getId().equals(id));
        }
    }

    private Stream<Invoice> getInvoices() throws IOException {
        return fileHelper.readLines(filePath)
            .map(this::deserializeJsonToInvoice)
            .filter(Objects::nonNull);
    }

    private int getPositionInDatabase(Long id) throws IOException, DatabaseOperationException {
        AtomicInteger index = new AtomicInteger();
        try (Stream<Invoice> stream = getInvoices()) {
            Optional<Invoice> optionalInvoice = stream
                .filter(i -> {
                    if (!i.getId().equals(id)) {
                        index.getAndIncrement();
                        return false;
                    }
                    return true;
                })
                .findFirst();
            if (optionalInvoice.isEmpty()) {
                log.error("Attempt to operate on a non existing invoice.");
                throw new DatabaseOperationException(String.format("There was no invoice in database with id: %s", id));
            }
        }
        return index.incrementAndGet();
    }
}
