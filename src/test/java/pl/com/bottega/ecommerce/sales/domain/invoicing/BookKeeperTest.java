package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
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
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductDataPublicBuilder;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;


@ExtendWith(MockitoExtension.class)
class BookKeeperTest {
    @Mock
    TaxPolicy taxPolicy;


    @BeforeEach
    void setUp() throws Exception {

    }

    //żądanie wydania faktury z jedną pozycją powinno zwrócić fakturę z jedną pozycją
    @Test
    void TestCase1() {
        ClientData clientData = new ClientData(Id.generate(), "test");
        Money testMoney = new Money(new BigDecimal(10));
        ProductData testProductData = ProductDataPublicBuilder.getProductData();
        RequestItem item = new RequestItem(testProductData, 1, testMoney);

        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);
        invoiceRequest.add(item);
        when(taxPolicy.calculateTax(any(), any())).thenReturn(new Tax(testMoney, "test1"));

        BookKeeper bk = new BookKeeper(new InvoiceFactory());
        assertEquals(1, bk.issuance(invoiceRequest, taxPolicy).getItems().size());
    }

}
