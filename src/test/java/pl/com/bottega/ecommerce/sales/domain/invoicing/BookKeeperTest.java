package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
class BookKeeperTest {
    private TestClass testClass;
    @Mock
    TaxPolicy taxPolicy;

    @BeforeEach
    void setUp() throws Exception {
        this.testClass = new TestClass();
    }

    //żądanie wydania faktury z jedną pozycją powinno zwrócić fakturę z jedną pozycją
    @Test
    void singleRequestShouldReturnSingleInvoice() {
        when(taxPolicy.calculateTax(any(), any())).thenReturn(this.testClass.getTax());
        testClass.addItemToRequest();

        BookKeeper bk = new BookKeeper(new InvoiceFactory());
        assertEquals(1, bk.issuance(this.testClass.getInvoiceRequest(), taxPolicy).getItems().size());
    }

    @Test
    void doubleRequestShouldCallCalculateTaxTwice(){
        when(taxPolicy.calculateTax(any(), any())).thenReturn(this.testClass.getTax());
        testClass.addItemToRequest();
        testClass.addItemToRequest();

        BookKeeper bk = new BookKeeper(new InvoiceFactory());
        bk.issuance(testClass.getInvoiceRequest(),taxPolicy);
        Mockito.verify(taxPolicy, times(2)).calculateTax(any(), any());
    }

}
