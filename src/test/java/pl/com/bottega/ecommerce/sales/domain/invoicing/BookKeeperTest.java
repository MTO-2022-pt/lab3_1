package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Date;

@ExtendWith(MockitoExtension.class)
class BookKeeperTest {



    @BeforeEach
    void setUp() throws Exception {

    }

    @Test
    void test() {
        InvoiceFactory invoiceFactory = new InvoiceFactory();
        BookKeeper testKeeper = new BookKeeper(invoiceFactory);

        ClientData cData = new ClientData(new Id("1"), "tester");
        InvoiceRequest invoiceRequest = new InvoiceRequest(cData);
        //invoiceRequest.add(new RequestItem(new ProductData(new Id("2"), new Money(2), "testProd", ProductType.FOOD, new Date())));

        //Invoice correctInvoice  = testKeeper.issuance();

    }

}
