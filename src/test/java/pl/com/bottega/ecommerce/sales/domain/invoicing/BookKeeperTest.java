package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;


@ExtendWith(MockitoExtension.class)
class BookKeeperTest {
    static ClientData clientData;
    static BookKeeper bookKeeper;
    static Product product;
    static InvoiceRequest invoiceRequest;

    @Mock
    static TaxPolicy taxPolicy;


    @BeforeAll
    static void setUp() {
        bookKeeper = new BookKeeper(new InvoiceFactory());
        clientData = new ClientData(Id.generate(), "Tomasz");
        product = new Product(Id.generate(), new Money(10.20), "Cheese", ProductType.FOOD);
        when(taxPolicy.calculateTax(ProductType.FOOD, product.getPrice()))
                .thenReturn(new Tax(new Money(5), "test"));

    }

    @BeforeEach
    void setUpBeforeEach() {
        invoiceRequest = new InvoiceRequest(clientData);
    }

    @Test
    void invoiceItemsSize() {
        RequestItem requestItem = new RequestItem(product.generateSnapshot(), 1, product.getPrice());
        invoiceRequest.add(requestItem);
        assertEquals(1, bookKeeper.issuance(invoiceRequest, taxPolicy).getItems().size());
    }

}
