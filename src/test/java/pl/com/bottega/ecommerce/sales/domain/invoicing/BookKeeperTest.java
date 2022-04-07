package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.client.Client;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

class BookKeeperTest {

    @Mock
    TaxPolicy taxPolicyMock;

    @Mock
    InvoiceFactory factoryMock;

    ClientData clientData;
    BookKeeper bookKeeper;
    InvoiceRequest invoiceRequest;

    ProductData[] productData;

    @BeforeEach
    void setUp(){

        taxPolicyMock = Mockito.mock(TaxPolicy.class);
        factoryMock = Mockito.mock(InvoiceFactory.class);

        clientData = new Client().generateSnapshot();
        bookKeeper = new BookKeeper(factoryMock);
        invoiceRequest = new InvoiceRequest(clientData);

        productData = new ProductData[]{
                (new Product(Id.generate(), Money.ZERO, "Standard", ProductType.STANDARD)).generateSnapshot(),
                (new Product(Id.generate(), Money.ZERO, "Drug", ProductType.DRUG)).generateSnapshot(),
                (new Product(Id.generate(), Money.ZERO, "Food", ProductType.FOOD)).generateSnapshot()
        };
    }

    @Test
    void myTest1_emptyInvoiceRequestTest(){
        Mockito.when(factoryMock.create(any(ClientData.class))).thenReturn(new Invoice(Id.generate(), clientData));
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicyMock);
        int size = invoice.getItems().size();
        assertEquals(size, 0);
    }

    @Test
    void testCase1_oneRequestItemTest(){
        Mockito.when(taxPolicyMock.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(new Tax(Money.ZERO, "Example Tax 1"));
        Mockito.when(factoryMock.create(any(ClientData.class))).thenReturn(new Invoice(Id.generate(), clientData));
        RequestItem requestItem = new RequestItem(productData[0], 1, Money.ZERO);
        invoiceRequest.add(requestItem);
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicyMock);
        int size = invoice.getItems().size();
        assertEquals(size, 1);
    }

    @Test
    void testCase2_twoRequestItemsTestWithValuesCheck(){
        Mockito.when(taxPolicyMock.calculateTax(any(ProductType.class), any(Money.class)))
                .thenReturn(new Tax(Money.ZERO, "Example Tax 2"))
                .thenReturn(new Tax(Money.ZERO, "Example Tax 3"));
        Mockito.when(factoryMock.create(any(ClientData.class))).thenReturn(new Invoice(Id.generate(), clientData));
        RequestItem requestItem1 = new RequestItem(productData[1], 2, Money.ZERO);
        RequestItem requestItem2 = new RequestItem(productData[2], 4, Money.ZERO);
        invoiceRequest.add(requestItem1);
        invoiceRequest.add(requestItem2);
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicyMock);
        int size = invoice.getItems().size();
        assertEquals(size, 2);
        assertEquals(invoice.getItems().get(0).getProduct(), requestItem1.getProductData());
        assertEquals(invoice.getItems().get(0).getQuantity(), requestItem1.getQuantity());
        assertEquals(invoice.getItems().get(1).getProduct(), requestItem2.getProductData());
        assertEquals(invoice.getItems().get(1).getQuantity(), requestItem2.getQuantity());
    }

    @Test
    void myTest2_threeRequestItemsCalledTwiceTest(){
        Mockito.when(taxPolicyMock.calculateTax(any(ProductType.class), any(Money.class)))
                .thenReturn(new Tax(Money.ZERO, "Example Tax 1"))
                .thenReturn(new Tax(Money.ZERO, "Example Tax 2"))
                .thenReturn(new Tax(Money.ZERO, "Example Tax 3"));
        Mockito.when(factoryMock.create(any(ClientData.class))).thenReturn(new Invoice(Id.generate(), clientData));
        RequestItem requestItem1 = new RequestItem(productData[0], 2, Money.ZERO);
        RequestItem requestItem2 = new RequestItem(productData[1], 4, Money.ZERO);
        RequestItem requestItem3 = new RequestItem(productData[2], 8, Money.ZERO);
        invoiceRequest.add(requestItem1);
        invoiceRequest.add(requestItem2);
        invoiceRequest.add(requestItem3);
        Invoice invoice1 = bookKeeper.issuance(invoiceRequest, taxPolicyMock);
        Invoice invoice2 = bookKeeper.issuance(invoiceRequest, taxPolicyMock);
        int size1 = invoice1.getItems().size();
        int size2 = invoice2.getItems().size();
        assertEquals(size1, size2, 3);
    }

    @Test
    void myTest3_fiveRepeatRequestItemsTest(){
        Mockito.when(taxPolicyMock.calculateTax(any(ProductType.class), any(Money.class)))
                .thenReturn(new Tax(Money.ZERO, "Example Tax 1"));
        Mockito.when(factoryMock.create(any(ClientData.class))).thenReturn(new Invoice(Id.generate(), clientData));
        RequestItem requestItem1 = new RequestItem(productData[0], 2, Money.ZERO);
        invoiceRequest.add(requestItem1);
        invoiceRequest.add(requestItem1);
        invoiceRequest.add(requestItem1);
        invoiceRequest.add(requestItem1);
        invoiceRequest.add(requestItem1);
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicyMock);
        int size = invoice.getItems().size();
        assertEquals(size, 5);
    }

}
