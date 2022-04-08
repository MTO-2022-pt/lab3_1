package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductDataBuilder;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Date;

@ExtendWith(MockitoExtension.class)
class BookKeeperTest {

    @Mock
    InvoiceFactory invoiceFactoryMock;
    @Mock
    TaxPolicy taxPolicyMock;

    ClientData clientMock;
    Tax taxMock;
    RequestItem requestItem;


    @BeforeEach
    void setUp() throws Exception {
        invoiceFactoryMock = mock(InvoiceFactory.class);
        taxPolicyMock = mock(TaxPolicy.class);
        clientMock = new ClientData(new Id("1"), "tester");
        requestItem = new RequestItem(new ProductDataBuilder().build(), 1, new Money(100));
        taxMock = new Tax(new Money(100), "testTax");
    }

    @Test
    void invoiceWithOnePosition(){
        when(invoiceFactoryMock.create(clientMock)).thenReturn(new Invoice(Id.generate(), clientMock));
        BookKeeper bookKeeper = new BookKeeper(invoiceFactoryMock);
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientMock);
        invoiceRequest.add(requestItem);
        when(taxPolicyMock.calculateTax(any(), any())).thenReturn(taxMock);
        Invoice testInvoice = bookKeeper.issuance(invoiceRequest, taxPolicyMock);
        assertEquals(1, testInvoice.getItems().size());
    }

}
