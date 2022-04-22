package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductDataPublicBuilder;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.math.BigDecimal;

class TestClass {
    ClientData clientData = new ClientData(Id.generate(), "test");
    Money testMoney = new Money(new BigDecimal(10));
    ProductData testProductData = ProductDataPublicBuilder.getProductData();
    RequestItem item = new RequestItem(testProductData, 1, testMoney);
    Tax tax = new Tax(testMoney, "test1");
    InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);

    public TestClass() {
    }

    public TestClass(ClientData clientData, Money testMoney, ProductData testProductData, RequestItem item, InvoiceRequest invoiceRequest) {
        this.clientData = clientData;
        this.testMoney = testMoney;
        this.testProductData = testProductData;
        this.item = item;
        this.invoiceRequest = invoiceRequest;
    }

    public Tax getTax() {
        return tax;
    }

    public void setTax(Tax tax) {
        this.tax = tax;
    }

    public void addItemToRequest() {
        invoiceRequest.add(item);
    }

    public ClientData getClientData() {
        return clientData;
    }

    public void setClientData(ClientData clientData) {
        this.clientData = clientData;
    }

    public Money getTestMoney() {
        return testMoney;
    }

    public void setTestMoney(Money testMoney) {
        this.testMoney = testMoney;
    }

    public ProductData getTestProductData() {
        return testProductData;
    }

    public void setTestProductData(ProductData testProductData) {
        this.testProductData = testProductData;
    }

    public RequestItem getItem() {
        return item;
    }

    public void setItem(RequestItem item) {
        this.item = item;
    }

    public InvoiceRequest getInvoiceRequest() {
        return invoiceRequest;
    }

    public void setInvoiceRequest(InvoiceRequest invoiceRequest) {
        this.invoiceRequest = invoiceRequest;
    }
}
