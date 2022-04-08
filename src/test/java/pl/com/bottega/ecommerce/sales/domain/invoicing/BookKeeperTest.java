package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductDataBuilder;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Random;

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
    void setUp(){
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

    @Test
    void invokeCalculateTaxTwice(){
        when(invoiceFactoryMock.create(clientMock)).thenReturn(new Invoice(Id.generate(), clientMock));
        BookKeeper bookKeeper = new BookKeeper(invoiceFactoryMock);
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientMock);
        invoiceRequest.add(requestItem);
        invoiceRequest.add(requestItem);
        when(taxPolicyMock.calculateTax(any(), any())).thenReturn(taxMock);
        bookKeeper.issuance(invoiceRequest, taxPolicyMock);
        verify(taxPolicyMock, times(2)).calculateTax(any(), any());
    }

    @Test
    void invokeCalculateTaxZeroTimes(){
        when(invoiceFactoryMock.create(clientMock)).thenReturn(new Invoice(Id.generate(), clientMock));
        BookKeeper bookKeeper = new BookKeeper(invoiceFactoryMock);
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientMock);
        bookKeeper.issuance(invoiceRequest, taxPolicyMock);
        verify(taxPolicyMock, times(0)).calculateTax(any(), any());
    }

    @Test
    void emptyInvoice(){
        when(invoiceFactoryMock.create(clientMock)).thenReturn(new Invoice(Id.generate(), clientMock));
        BookKeeper bookKeeper = new BookKeeper(invoiceFactoryMock);
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientMock);
        assertEquals(0, bookKeeper.issuance(invoiceRequest, taxPolicyMock).getItems().size());
    }

    @Test
    void multipleLinesInvoice(){
        Random rand = new Random();
        int lines = Math.abs(rand.nextInt() % 20);

        when(invoiceFactoryMock.create(clientMock)).thenReturn(new Invoice(Id.generate(), clientMock));
        BookKeeper bookKeeper = new BookKeeper(invoiceFactoryMock);
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientMock);
        for(int i = 0; i < lines; i++){
            invoiceRequest.add(requestItem);
        }
        when(taxPolicyMock.calculateTax(any(), any())).thenReturn(taxMock);
        assertEquals(lines, bookKeeper.issuance(invoiceRequest, taxPolicyMock).getItems().size());

    }

    @Test
    void multipleCalculateTaxInvokes(){
        Random rand = new Random();
        int lines = Math.abs(rand.nextInt() % 20);

        when(invoiceFactoryMock.create(clientMock)).thenReturn(new Invoice(Id.generate(), clientMock));
        BookKeeper bookKeeper = new BookKeeper(invoiceFactoryMock);
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientMock);
        for(int i = 0; i < lines; i++){
            invoiceRequest.add(requestItem);
        }
        when(taxPolicyMock.calculateTax(any(), any())).thenReturn(taxMock);
        bookKeeper.issuance(invoiceRequest, taxPolicyMock);
        verify(taxPolicyMock, times(lines)).calculateTax(any(), any());
    }
}
