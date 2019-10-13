package pl.coderstrust.database;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import pl.coderstrust.database.sql.model.SqlModelMapper;
import pl.coderstrust.database.sql.model.SqlModelMapperImpl;
import pl.coderstrust.generators.InvoiceGenerator;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-sql-test.properties")
class SqlDatabaseIT {

    @Autowired
    JdbcTemplate jdbcTemplate;
    SqlModelMapper sqlModelMapper;
    SqlDatabase database;

    @BeforeEach
    void setUp() {
        sqlModelMapper = new SqlModelMapperImpl();
        database = new SqlDatabase(jdbcTemplate, sqlModelMapper);
    }

    @BeforeEach
    void createSqlDatabase() {
//        jdbcTemplate.execute(dropDatabaseIfExists());
        jdbcTemplate.execute(createDatabase());
        jdbcTemplate.execute(dropAllTables());
//        jdbcTemplate.execute("DROP TABLE IF EXISTS company CASCADE");
//        jdbcTemplate.execute("DROP TABLE IF EXISTS invoice CASCADE");
//        jdbcTemplate.execute("DROP TABLE IF EXISTS invoice_entry CASCADE");
//        jdbcTemplate.execute("DROP TABLE IF EXISTS invoice_entries CASCADE");
        jdbcTemplate.execute(createTableCompany());
        jdbcTemplate.execute(createTableInvoice());
        jdbcTemplate.execute(alterTableInvoice());
        jdbcTemplate.execute(createTableInvoiceEntry());
        jdbcTemplate.execute(createTableInvoiceEntries());
    }

    private static String dropAllTables() {
        return "DROP TABLE IF EXISTS company, invoice, invoice_entry, invoice_entries CASCADE";
    }

//    private static String dropDatabaseIfExists() {
//        return "DROP DATABASE IF EXISTS invoices1";
//    }

    private static String createDatabase() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
            .append("SELECT 'CREATE DATABASE invoices1' ")
            .append("WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'invoices1')");
        return stringBuilder.toString();
    }

    private static String createTableCompany() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
            .append("CREATE TABLE IF NOT EXISTS company ")
            .append("(")
            .append("id BIGSERIAL, ")
            .append("name VARCHAR(255), ")
            .append("address VARCHAR(255), ")
            .append("tax_id VARCHAR(255), ")
            .append("account_number VARCHAR(255), ")
            .append("phone_number VARCHAR(255), ")
            .append("email VARCHAR(255), ")
            .append("CONSTRAINT company_pkey PRIMARY KEY (id)")
            .append(")");
        return stringBuilder.toString();
    }

    private static String createTableInvoice () {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
            .append("CREATE TABLE IF NOT EXISTS invoice ")
            .append("(")
            .append("id BIGSERIAL, ")
            .append("number VARCHAR(255), ")
            .append("issued_date DATE, ")
            .append("due_date DATE, ")
            .append("seller_id bigint, ")
            .append("buyer_id bigint, ")
            .append("CONSTRAINT invoice_pkey PRIMARY KEY (id), ")
            .append("FOREIGN KEY (seller_id) REFERENCES company(id), ")
            .append("FOREIGN KEY (buyer_id) REFERENCES company(id)")
            .append(")");
        return stringBuilder.toString();
    }

    private static String alterTableInvoice() {
        return "ALTER TABLE invoice OWNER TO postgres";
    }

    private static String createTableInvoiceEntry() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
            .append("CREATE TABLE IF NOT EXISTS invoice_entry ")
            .append("(")
            .append("id BIGSERIAL, ")
            .append("description VARCHAR(255), ")
            .append("quantity bigint, ")
            .append("price numeric(19, 2), ")
            .append("net_value numeric(19, 2), ")
            .append("gross_value numeric(19, 2), ")
            .append("vat_rate int, ")
            .append("CONSTRAINT invoice_entry_pkey PRIMARY KEY (id)")
            .append(")");
        return stringBuilder.toString();
    }

    private static String createTableInvoiceEntries() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
//            .append("CREATE TABLE invoice_entries ")
            .append("CREATE TABLE IF NOT EXISTS invoice_entries ")
            .append("(")
            .append("invoice_id bigint, ")
            .append("entries_id bigint, ")
            .append("FOREIGN KEY (invoice_id) REFERENCES invoice(id) ON DELETE CASCADE ON UPDATE CASCADE, ")
            .append("FOREIGN KEY (entries_id) REFERENCES invoice_entry(id) ON DELETE CASCADE ON UPDATE CASCADE")
            .append(")");
        return stringBuilder.toString();
    }

//    @BeforeEach
//    void deleteAllDataFromDatabase() {
//        jdbcTemplate.execute("DELETE FROM invoice_entries");
//        jdbcTemplate.execute("DELETE FROM invoice_entry");
//        jdbcTemplate.execute("DELETE FROM invoice");
//        jdbcTemplate.execute("DELETE FROM company");
//    }

    @Test // ok
    void shouldSaveInvoice() throws DatabaseOperationException {
        //given
        pl.coderstrust.model.Invoice invoiceToSave = InvoiceGenerator.getRandomInvoiceWithSpecificIdCompaniesAndEntriesWithSubsequentIdsStartingFrom(1L, 1L, 1L);

        //when
        pl.coderstrust.model.Invoice savedInvoice = database.save(invoiceToSave);

        //then
        assertEquals(invoiceToSave, savedInvoice);
//        assertEquals(1L, savedInvoice.getId());
//        assertEquals(invoiceToSave.getNumber(), savedInvoice.getNumber());
//        assertEquals(invoiceToSave.getIssuedDate(), savedInvoice.getIssuedDate());
//        assertEquals(invoiceToSave.getDueDate(), savedInvoice.getDueDate());
//        assertEquals(1L, savedInvoice.getSeller().getId());
//        assertEquals(invoiceToSave.getSeller().getName(), savedInvoice.getSeller().getName());
//        assertEquals(invoiceToSave.getSeller().getAddress(), savedInvoice.getSeller().getAddress());
//        assertEquals(invoiceToSave.getSeller().getTaxId(), savedInvoice.getSeller().getTaxId());
//        assertEquals(invoiceToSave.getSeller().getAccountNumber(), savedInvoice.getSeller().getAccountNumber());
//        assertEquals(invoiceToSave.getSeller().getPhoneNumber(), savedInvoice.getSeller().getPhoneNumber());
//        assertEquals(invoiceToSave.getSeller().getEmail(), savedInvoice.getSeller().getEmail());
//        assertEquals(2L, savedInvoice.getBuyer().getId());
//        assertEquals(invoiceToSave.getBuyer().getName(), savedInvoice.getBuyer().getName());
//        assertEquals(invoiceToSave.getBuyer().getAddress(), savedInvoice.getBuyer().getAddress());
//        assertEquals(invoiceToSave.getBuyer().getTaxId(), savedInvoice.getBuyer().getTaxId());
//        assertEquals(invoiceToSave.getBuyer().getAccountNumber(), savedInvoice.getBuyer().getAccountNumber());
//        assertEquals(invoiceToSave.getBuyer().getPhoneNumber(), savedInvoice.getBuyer().getPhoneNumber());
//        assertEquals(invoiceToSave.getBuyer().getEmail(), savedInvoice.getBuyer().getEmail());
//        long j =1;
//        for (int i = 0; i < invoiceToSave.getEntries().size(); i++) {
//            assertEquals(j++, savedInvoice.getEntries().get(i).getId());
//            assertEquals(invoiceToSave.getEntries().get(i).getDescription(), savedInvoice.getEntries().get(i).getDescription());
//            assertEquals(invoiceToSave.getEntries().get(i).getQuantity(), savedInvoice.getEntries().get(i).getQuantity());
//            assertEquals(invoiceToSave.getEntries().get(i).getPrice(), savedInvoice.getEntries().get(i).getPrice());
//            assertEquals(invoiceToSave.getEntries().get(i).getNetValue(), savedInvoice.getEntries().get(i).getNetValue());
//            assertEquals(invoiceToSave.getEntries().get(i).getGrossValue(), savedInvoice.getEntries().get(i).getGrossValue());
//            assertEquals(invoiceToSave.getEntries().get(i).getVatRate(), savedInvoice.getEntries().get(i).getVatRate());
//        }
    }

    @Test // ok
    void shouldUpdateInvoice() throws DatabaseOperationException {
        //given
        pl.coderstrust.model.Invoice invoiceToSave = InvoiceGenerator.getRandomInvoiceWithSpecificIdCompaniesAndEntriesWithSubsequentIdsStartingFrom(1L, 1L, 1L);
        database.save(invoiceToSave);
        pl.coderstrust.model.Invoice invoiceToUpdate = InvoiceGenerator.getRandomInvoiceWithSpecificIdCompaniesAndEntriesWithSubsequentIdsStartingFrom(1L, 3L, 6L);

        //when
        pl.coderstrust.model.Invoice updatedInvoice = database.save(invoiceToUpdate);

        //then
        assertEquals(invoiceToUpdate, updatedInvoice);
    }

    @Test //ok
    void saveMethodShouldThrowExceptionForNullInvoice() {
        assertThrows(IllegalArgumentException.class, () -> database.save(null));
    }

    @Test //ok
    void shouldDeleteInvoice() throws DatabaseOperationException {
        //given
        pl.coderstrust.model.Invoice invoiceToSave = InvoiceGenerator.getRandomInvoice();
        pl.coderstrust.model.Invoice savedInvoice = database.save(invoiceToSave);
        Long savedInvoiceId = savedInvoice.getId();

        //when
        database.delete(savedInvoiceId);
        boolean result = database.exists(savedInvoiceId);

        //then
        assertFalse(result);
    }

    @Test //ok
    void deleteMethodShouldThrowExceptionForNullId() {
        assertThrows(IllegalArgumentException.class, () -> database.delete(null));
    }

    @Test //ok
    void deleteMethodShouldThrowExceptionDuringDeletingNotExistingInvoice() {
        assertThrows(DatabaseOperationException.class, () -> database.delete(100L));
    }

    @Test // prawdopodobnie problem z ZERAMI po kropce dziesiętnej ?????????????????????????????????????????????????????
    void shouldReturnInvoiceById() throws DatabaseOperationException {
        //given
        pl.coderstrust.model.Invoice invoiceToSave = InvoiceGenerator.getRandomInvoiceWithSpecificIdCompaniesAndEntriesWithSubsequentIdsStartingFrom(1L, 1L, 1L);
        pl.coderstrust.model.Invoice savedInvoice = database.save(invoiceToSave);
//        Long savedInvoiceId = savedInvoice.getId();

        //when
        Optional<pl.coderstrust.model.Invoice> result = database.getById(savedInvoice.getId());

        //then
        assertTrue(result.isPresent());
        assertEquals(savedInvoice, result.get());
    }

    @Test //ok
    void shouldReturnEmptyOptionalWhileGettingNonExistingInvoiceById() throws DatabaseOperationException {
        //when
        Optional<pl.coderstrust.model.Invoice> result = database.getById(100L);

        //then
        assertTrue(result.isEmpty());
    }

    @Test //ok
    void getByIdMethodShouldThrowExceptionForNullId() {
        assertThrows(IllegalArgumentException.class, () -> database.getById(null));
    }

    @Test //konieczne porównywanie wpisów bez id-ków, lub budowanie (WSZYSTKIEGO) z pobranymi id-kami...
    void shouldReturnInvoiceByNumber() throws DatabaseOperationException {
        //given
        pl.coderstrust.model.Invoice invoiceToSave = InvoiceGenerator.getRandomInvoice();
        pl.coderstrust.model.Invoice savedInvoice = database.save(invoiceToSave);
        String savedInvoiceNumber = savedInvoice.getNumber();

        //when
        Optional<pl.coderstrust.model.Invoice> result = database.getByNumber(savedInvoiceNumber);

        //then
        assertTrue(result.isPresent());
        assertEquals(savedInvoice, result.get());
    }

    @Test //ok
    void shouldReturnEmptyOptionalWhileGettingNonExistingInvoiceByNumber() throws DatabaseOperationException {
        //when
        Optional<pl.coderstrust.model.Invoice> result = database.getByNumber("1");

        //then
        assertTrue(result.isEmpty());
//        verify(database).getByNumber("1");
    }

    @Test //ok
    void getByNumberMethodShouldThrowExceptionForNullNumber() {
        assertThrows(IllegalArgumentException.class, () -> database.getByNumber(null));
    }

    @Test //konieczne porównywanie wpisów bez id-ków, lub budowanie (WSZYSTKIEGO) z pobranymi id-kami...
    void shouldReturnAllInvoices() throws DatabaseOperationException {
        //given
        pl.coderstrust.model.Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
        pl.coderstrust.model.Invoice savedInvoice1 = database.save(invoice1);
        pl.coderstrust.model.Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
        pl.coderstrust.model.Invoice savedInvoice2 = database.save(invoice2);
        List<pl.coderstrust.model.Invoice> invoices = List.of(savedInvoice1, savedInvoice2);

        //when
        Collection<pl.coderstrust.model.Invoice> result = database.getAll();

        //then
        assertEquals(invoices, result);
    }

    @Test //ok
    void shouldReturnTrueForExistingInvoice() throws DatabaseOperationException {
        //given
        pl.coderstrust.model.Invoice invoiceToSave = InvoiceGenerator.getRandomInvoice();
        pl.coderstrust.model.Invoice savedInvoice = database.save(invoiceToSave);
        Long savedInvoiceId = savedInvoice.getId();

        //when
        boolean result = database.exists(savedInvoiceId);

        //then
        assertTrue(result);
    }

    @Test //ok
    void shouldReturnFalseForNonExistingInvoice() throws DatabaseOperationException {
        //when
        boolean result = database.exists(1L);

        //then
        assertFalse(result);
    }

    @Test //ok
    void existsMethodShouldThrowExceptionForNullId() {
        assertThrows(IllegalArgumentException.class, () -> database.exists(null));
    }

    @Test //ok
    void shouldReturnNumberOfInvoices() throws DatabaseOperationException {
        //given
        database.save(InvoiceGenerator.getRandomInvoice());
        database.save(InvoiceGenerator.getRandomInvoice());

        //when
        Long result = database.count();

        //then
        assertEquals(2L, result);
    }

    @Test //ok
    void shouldDeleteAllInvoices() throws DatabaseOperationException {
        //given
        database.save(InvoiceGenerator.getRandomInvoice());
        database.save(InvoiceGenerator.getRandomInvoice());

        //when
        database.deleteAll();

        //then
        assertEquals(0L, database.count());
    }

    @Test //TUTAJ w wynikach są zera po kropce dziesiętnej ORAZ POMIESZANA KOLEJNOSC
    void shouldReturnInvoicesFilteredByIssueDate() throws DatabaseOperationException {
        //given
        LocalDate startDate = LocalDate.of(2019, 8, 24);
        pl.coderstrust.model.Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithSpecificIssuedDate(startDate);
        pl.coderstrust.model.Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithSpecificIssuedDate(startDate.plusDays(1L));
        pl.coderstrust.model.Invoice invoice3 = InvoiceGenerator.getRandomInvoiceWithSpecificIssuedDate(startDate.plusDays(2L));
        pl.coderstrust.model.Invoice invoice4 = InvoiceGenerator.getRandomInvoiceWithSpecificIssuedDate(startDate.plusDays(4L));
        pl.coderstrust.model.Invoice savedInvoice1 = database.save(invoice1);
        pl.coderstrust.model.Invoice savedInvoice2 = database.save(invoice2);
        pl.coderstrust.model.Invoice savedInvoice3 = database.save(invoice3);
        database.save(invoice4);
        List<pl.coderstrust.model.Invoice> filteredInvoices = List.of(savedInvoice1, savedInvoice2, savedInvoice3);

        //when
        Collection<pl.coderstrust.model.Invoice> filteredInvoicesResult = database.getByIssueDate(startDate, startDate.plusDays(2L));

        //then
        assertEquals(filteredInvoices, filteredInvoicesResult);
    }

    @ParameterizedTest //ok
    @MethodSource("invalidIssuedDateArgumentsAndExceptionMessages")
    void getByIssuedDateMethodShouldThrowExceptionWhenInvalidArgumentsArePassed(LocalDate startDate, LocalDate endDate, String message) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> database.getByIssueDate(startDate, endDate));
        assertEquals(message, exception.getMessage());
    }

    private static Stream<Arguments> invalidIssuedDateArgumentsAndExceptionMessages() {
        return Stream.of(
            Arguments.of(null, null, "Start date cannot be null"),
            Arguments.of(null, LocalDate.of(2018, 8, 31), "Start date cannot be null"),
            Arguments.of(LocalDate.of(2019, 8, 22), null, "End date cannot be null"),
            Arguments.of(LocalDate.of(2019, 8, 22), LocalDate.of(2018, 8, 31), "Start date cannot be after end date"),
            Arguments.of(LocalDate.of(2019, 2, 28), LocalDate.of(2019, 1, 31), "Start date cannot be after end date"),
            Arguments.of(LocalDate.of(2019, 2, 28), LocalDate.of(2009, 3, 31), "Start date cannot be after end date")
        );
    }
}
