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
    void test(){
        Mockito.when(taxPolicyMock.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(new Tax(Money.ZERO, "Example Tax 1"));
        Mockito.when(factoryMock.create(any(ClientData.class))).thenReturn(new Invoice(Id.generate(), clientData));
        RequestItem requestItem = new RequestItem(productData[0], 1, Money.ZERO);
        invoiceRequest.add(requestItem);
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicyMock);
        int size = invoice.getItems().size();
        assertEquals(size, 1);
    }

    @Test
    void test2(){
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

}
