package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;


@ExtendWith(MockitoExtension.class)
class BookKeeperTest {
    static ClientData clientData;
    static BookKeeper bookKeeper;
    static Product product;
    static InvoiceRequest invoiceRequest;
    static RequestItem requestItem;

    @Mock
    static TaxPolicy taxPolicy;

    @BeforeAll
    static void setUp() {
        bookKeeper = new BookKeeper(new InvoiceFactory());
        clientData = new ClientData(Id.generate(), "Tomasz");
        product = new Product(Id.generate(), new Money(10.20), "Cheese", ProductType.FOOD);
        requestItem = new RequestItem(product.generateSnapshot(), 1, product.getPrice());
    }

    @BeforeEach
    void setUpBeforeEach() {
        invoiceRequest = new InvoiceRequest(clientData);
        when(taxPolicy.calculateTax(ProductType.FOOD, product.getPrice()))
                .thenReturn(new Tax(new Money(5), "test"));
    }

    @Test
    void invoiceItemsSize() {
        invoiceRequest.add(requestItem);
        assertEquals(1, bookKeeper.issuance(invoiceRequest, taxPolicy).getItems().size());
    }

    @Test
    void invoicePriceGross() {
        invoiceRequest.add(requestItem);
        assertEquals(product.getPrice().add(taxPolicy.calculateTax(ProductType.FOOD, product.getPrice()).getAmount()),
                bookKeeper.issuance(invoiceRequest, taxPolicy).getItems().get(0).getGros());
    }

    @Test
    void invoicePriceNet() {
        invoiceRequest.add(requestItem);
        assertEquals(product.getPrice(),  bookKeeper.issuance(invoiceRequest, taxPolicy).getItems().get(0).getNet());
    }

    @Test
    void invoiceCalculateTax() {
        invoiceRequest.add(requestItem);
        invoiceRequest.add(requestItem);
        bookKeeper.issuance(invoiceRequest, taxPolicy);
        verify(taxPolicy, times(2)).calculateTax(ProductType.FOOD, product.getPrice());
    }

    @Test
    void invoiceCalculateTaxOne() {
        invoiceRequest.add(requestItem);
        bookKeeper.issuance(invoiceRequest, taxPolicy);
        verify(taxPolicy, times(1)).calculateTax(ProductType.FOOD, product.getPrice());
    }

    @Test
    void invoiceCalculateTaxThree() {
        invoiceRequest.add(requestItem);
        invoiceRequest.add(requestItem);
        invoiceRequest.add(requestItem);
        bookKeeper.issuance(invoiceRequest, taxPolicy);
        verify(taxPolicy, times(3)).calculateTax(ProductType.FOOD, product.getPrice());
    }


}
