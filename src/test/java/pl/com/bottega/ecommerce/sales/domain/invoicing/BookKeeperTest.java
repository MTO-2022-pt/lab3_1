package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

@ExtendWith(MockitoExtension.class)
class BookKeeperTest {

    private static ClientData clientData;
    private InvoiceRequest invoiceRequest;
    private static InvoiceFactory invoiceFactory;
    private static BookKeeper bookKeeper;

    @Mock
    private TaxPolicy taxPolicy;

    @BeforeAll
    static void setUp() throws Exception {
        clientData = new ClientData(Id.generate(), "Test");
        invoiceFactory = new InvoiceFactory();
        bookKeeper = new BookKeeper(invoiceFactory);
    }

    @BeforeEach
    void setUpInvoiceRequest() throws Exception {
        invoiceRequest = new InvoiceRequest(clientData);
    }

    @Test
    void shouldReturnInvoiceWithOneItemWhenRequestInvoiceWithOneItem() {
        Money money = new Money(200.00);
        Product product = new Product(Id.generate(), money, "Sushi", ProductType.FOOD);
        ProductData productData = product.generateSnapshot();
        
        invoiceRequest.add(new RequestItem(productData, 1, productData.getPrice()));

        when(taxPolicy.calculateTax(ProductType.FOOD, money)).thenReturn(new Tax(new Money(20.50), "testTax"));

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        assertEquals(1, invoice.getItems().size(), "Returned items count is not equal to requested");
    }

    @Test
    void shouldCallCalculateTaxTwoTimesWhenRequestInvoiceWithTwoItem() {
        Money money = new Money(200.00);
        Product product1 = new Product(Id.generate(), money, "Sushi", ProductType.FOOD);
        Product product2 = new Product(Id.generate(), money, "Hosomaki", ProductType.FOOD);

        ProductData productData1 = product1.generateSnapshot();
        ProductData productData2 = product2.generateSnapshot();

        invoiceRequest.add(new RequestItem(productData1, 1, productData1.getPrice()));
        invoiceRequest.add(new RequestItem(productData2, 1, productData2.getPrice()));

        when(taxPolicy.calculateTax(ProductType.FOOD, money)).thenReturn(new Tax(new Money(20.50), "testTax"));

        bookKeeper.issuance(invoiceRequest, taxPolicy);

        verify(taxPolicy, times(2)).calculateTax(ProductType.FOOD, money);
    }

}
