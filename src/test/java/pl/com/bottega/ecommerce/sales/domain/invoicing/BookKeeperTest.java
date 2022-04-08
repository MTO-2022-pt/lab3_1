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
    private static BookKeeper bookKeeper;
    
    @Mock
    private static TaxPolicy taxPolicy;

    @BeforeAll
    static void setUp() {
        clientData = new ClientData(Id.generate(), "franek");
        InvoiceFactory invoiceFactory = new InvoiceFactory();
        bookKeeper = new BookKeeper(invoiceFactory);
    }
    
    @BeforeEach
    void setUpBeforeEach() {
        invoiceRequest = new InvoiceRequest(clientData);
    }

    @Test
    void shouldReturnInvoiceWithOneItemWhenRequestInvoiceWithOneItem() {
        Money money = new Money(100);
        Product product = new Product(Id.generate(), money, "Marihuanen", ProductType.DRUG); // :)
        ProductData productData = product.generateSnapshot();
        RequestItem requestItem = new RequestItem(productData, 1, productData.getPrice());
        invoiceRequest.add(requestItem);
        
        when(taxPolicy.calculateTax(ProductType.DRUG, money)).thenReturn(new Tax(new Money(12.2), "testTax"));
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertEquals(1, invoice.getItems().size());    
    }

    @Test
    void shouldCallInvoiceTwoTimesWhenRequestInvoiceWithTwoItems() {
        Money money = new Money(100);
        Product product = new Product(Id.generate(), money, "Marihuanen", ProductType.DRUG); // :)
        Product product1 = new Product(Id.generate(), money, "Cocainen", ProductType.DRUG); // :)
        ProductData productData = product.generateSnapshot();
        ProductData productData1 = product1.generateSnapshot();
        RequestItem requestItem = new RequestItem(productData, 1, productData.getPrice());
        RequestItem requestItem1 = new RequestItem(productData1, 1, productData1.getPrice());
        invoiceRequest.add(requestItem);
        invoiceRequest.add(requestItem1);

        when(taxPolicy.calculateTax(ProductType.DRUG, money)).thenReturn(new Tax(new Money(12.2), "testTax"));
        bookKeeper.issuance(invoiceRequest, taxPolicy);
        verify(taxPolicy, times(2)).calculateTax(ProductType.DRUG, money);
    }

    @Test
    void shouldReturnInvoiceWithThreeItemWhenRequestInvoiceWithThreeItem() {
        Money money = new Money(100);
        Product product = new Product(Id.generate(), money, "Marihuanen", ProductType.DRUG); // :)
        Product product1 = new Product(Id.generate(), money, "Cocainen", ProductType.DRUG); // :)
        Product product2 = new Product(Id.generate(), money, "Mefedronen", ProductType.DRUG); // :)
        ProductData productData = product.generateSnapshot();
        ProductData productData1 = product1.generateSnapshot();
        ProductData productData2 = product2.generateSnapshot();
        RequestItem requestItem = new RequestItem(productData, 1, productData.getPrice());
        RequestItem requestItem1 = new RequestItem(productData1, 1, productData1.getPrice());
        RequestItem requestItem2 = new RequestItem(productData2, 1, productData2.getPrice());
        invoiceRequest.add(requestItem);
        invoiceRequest.add(requestItem1);
        invoiceRequest.add(requestItem2);

        when(taxPolicy.calculateTax(ProductType.DRUG, money)).thenReturn(new Tax(new Money(12.2), "testTax"));
        bookKeeper.issuance(invoiceRequest, taxPolicy);
        verify(taxPolicy, times(3)).calculateTax(ProductType.DRUG, money);
    }

    @Test
    void invoiceItemsSize(){
        Money money = new Money(100);
        Product product = new Product(Id.generate(), money, "Marihuanen", ProductType.DRUG); // :)
        ProductData productData = product.generateSnapshot();
        RequestItem requestItem = new RequestItem(productData, 1, productData.getPrice());
        invoiceRequest.add(requestItem);

        when(taxPolicy.calculateTax(ProductType.DRUG, money)).thenReturn(new Tax(new Money(12.2), "testTax"));
        assertEquals(1, bookKeeper.issuance(invoiceRequest, taxPolicy).getItems().size());
    }

    @Test
    void invoicePriceGross(){
        Money money = new Money(100);
        Product product = new Product(Id.generate(), money, "Marihuanen", ProductType.DRUG); // :)
        ProductData productData = product.generateSnapshot();
        RequestItem requestItem = new RequestItem(productData, 1, productData.getPrice());
        invoiceRequest.add(requestItem);

        when(taxPolicy.calculateTax(ProductType.DRUG, money)).thenReturn(new Tax(new Money(12.2), "testTax"));
        assertEquals(productData.getPrice().add(taxPolicy.calculateTax(ProductType.DRUG, productData.getPrice()).getAmount()), bookKeeper.issuance(invoiceRequest, taxPolicy).getItems().get(0).getGros());
    }

    @Test
    void invoicePriceNet(){
        Money money = new Money(100);
        Product product = new Product(Id.generate(), money, "Marihuanen", ProductType.DRUG); // :)
        ProductData productData = product.generateSnapshot();
        RequestItem requestItem = new RequestItem(productData, 1, productData.getPrice());
        invoiceRequest.add(requestItem);

        when(taxPolicy.calculateTax(ProductType.DRUG, money)).thenReturn(new Tax(new Money(12.2), "testTax"));
        assertEquals(productData.getPrice(), bookKeeper.issuance(invoiceRequest, taxPolicy).getItems().get(0).getNet());
    }
}
