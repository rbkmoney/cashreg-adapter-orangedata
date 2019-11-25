package com.rbkmoney.adapter.orangedata;

import com.rbkmoney.adapter.cashreg.spring.boot.starter.constant.OptionalField;
import com.rbkmoney.damsel.cashreg.Cart;
import com.rbkmoney.damsel.cashreg.ItemsLine;
import com.rbkmoney.damsel.cashreg.provider.CashRegContext;
import com.rbkmoney.damsel.cashreg.provider.CashRegProviderSrv;
import com.rbkmoney.damsel.cashreg.provider.Session;
import com.rbkmoney.damsel.cashreg.provider.SourceCreation;
import com.rbkmoney.damsel.cashreg.type.Type;
import com.rbkmoney.damsel.cashreg_domain.AccountInfo;
import com.rbkmoney.damsel.cashreg_domain.PaymentInfo;
import com.rbkmoney.damsel.cashreg_domain.TaxMode;
import com.rbkmoney.damsel.domain.Cash;
import com.rbkmoney.damsel.domain.CurrencyRef;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = DEFINED_PORT, properties = {
        "keystore.certificate=classpath:ssl/orangedata.p12",
        "adapter-cashreg.url=http://localhost:9999/api/v2/",
})
@ContextConfiguration(classes = CashRegOrangeDataApplication.class)
public abstract class AbstractIntegrationTest {

    @Autowired
    protected CashRegProviderSrv.Iface handler;

    public CashRegContext makeCashRegContext(Type type) {
        CashRegContext context = new CashRegContext();
        context.setCashregId(TestData.CASHREG_ID);
        context.setSourceCreation(createSourceCreation());
        context.setAccountInfo(createAccountInfo());
        context.setOptions(createAdapterOptions());
        context.setSession(new Session().setType(type));
        return context;
    }

    protected Map<String, String> createAdapterOptions() {
        Map<String, String> options = new HashMap<>();
        options.put(OptionalField.LOGIN.getField(), TestData.LOGIN);
        options.put(OptionalField.PASS.getField(), TestData.PASS);
        options.put(OptionalField.PAYMENT_METHOD.getField(), TestData.PAYMENT_METHOD);
        options.put(OptionalField.PAYMENT_OBJECT.getField(), TestData.PAYMENT_OBJECT);
        options.put(OptionalField.GROUP.getField(), TestData.GROUP);
        options.put(OptionalField.COMPANY_NAME.getField(), TestData.COMPANY_NAME);
        options.put(OptionalField.COMPANY_ADDRESS.getField(), TestData.COMPANY_ADDRESS);
        options.put(OptionalField.PRIVATE_KEY.getField(), TestData.PRIVATE_KEY);
        options.put(OptionalField.KEY.getField(), TestData.KEY);
        return options;
    }

    protected SourceCreation createSourceCreation() {
        SourceCreation sourceCreation = new SourceCreation();
        sourceCreation.setPayment(createPaymentInfo());
        return sourceCreation;
    }

    protected AccountInfo createAccountInfo() {
        com.rbkmoney.damsel.cashreg_domain.LegalEntity legalEntity = new com.rbkmoney.damsel.cashreg_domain.LegalEntity();
        com.rbkmoney.damsel.cashreg_domain.RussianLegalEntity russianLegalEntity = new com.rbkmoney.damsel.cashreg_domain.RussianLegalEntity();

        russianLegalEntity.setActualAddress("ActualAddress");
        russianLegalEntity.setInn("INN");
        russianLegalEntity.setPostAddress("PostAddress");
        russianLegalEntity.setRegisteredName("RegisteredName");
        russianLegalEntity.setRepresentativeDocument("RepresentativeDocument");
        russianLegalEntity.setRepresentativeFullName("RepresentativeFullName");
        russianLegalEntity.setRepresentativePosition("RepresentativePosition");
        russianLegalEntity.setRegisteredNumber("RegisteredNumber");

        com.rbkmoney.damsel.cashreg_domain.RussianBankAccount russianBankAccount = new com.rbkmoney.damsel.cashreg_domain.RussianBankAccount();
        russianBankAccount.setAccount("Account");
        russianBankAccount.setBankName("BankName");
        russianBankAccount.setBankPostAccount("BankPostAccount");
        russianBankAccount.setBankBik("BankBik");
        russianLegalEntity.setRussianBankAccount(russianBankAccount);
        russianLegalEntity.setEmail(TestData.TEST_EMAIL);
        russianLegalEntity.setTaxMode(TaxMode.osn);

        legalEntity.setRussianLegalEntity(russianLegalEntity);

        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setLegalEntity(legalEntity);
        return accountInfo;
    }

    public static PaymentInfo createPaymentInfo() {
        PaymentInfo paymentInfo = new PaymentInfo();
        Cash cash = new Cash();
        cash.setAmount(100L);
        cash.setCurrency(new CurrencyRef().setSymbolicCode("RUR"));
        paymentInfo.setCash(cash);

        Cart cart = new Cart();
        List<ItemsLine> lines = new ArrayList<>();
        cart.setLines(lines);
        paymentInfo.setCart(cart);
        paymentInfo.setEmail(TestData.TEST_EMAIL);
        return paymentInfo;
    }


}
