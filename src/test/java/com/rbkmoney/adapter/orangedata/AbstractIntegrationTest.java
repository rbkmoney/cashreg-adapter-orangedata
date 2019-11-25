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
        options.put(OptionalField.LOGIN.getField(), "login");
        options.put(OptionalField.PASS.getField(), "pass");
        options.put(OptionalField.PAYMENT_METHOD.getField(), "PAYMENT_METHOD");
        options.put(OptionalField.PAYMENT_OBJECT.getField(), "PAYMENT_OBJECT");
        options.put(OptionalField.GROUP.getField(), "Main");
        options.put(OptionalField.COMPANY_NAME.getField(), "Bull");
        options.put(OptionalField.COMPANY_ADDRESS.getField(), "street Tester");
        options.put(OptionalField.PRIVATE_KEY.getField(), "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC3ycL8S2HxRptBte7yl2uje/s2pRqdXxj6D3ZiBPvPXGqQEtEddnWC6aXc/GuqM1f0C86a7xH6poo7Id8lbQ9xEMvMKghRwc0DCkM78TmPpYBosi/uACNO3Kv2QkH2t8lqlqtWIk1m7dFJRgZO9XOc6Zcx/stM5MxHoc//kfVM/mfWDj4FsuYL0SGNR/Z40WrBkGo+3PJsFvqNocFFonRd0TeWHY54T384XQG0vCJg8MqxVPEh6Rs1/uX8NETL5htQ7FAtx54deu9tguIZZ5w/RrsKocaP1k1jWglOErcDCtJ3jIdr1afH8ZplQ21a53UFo/2DexVf6xFX3G2cj3p3AgMBAAECggEAPUfM+Aq6kZSVWAetsL3EajKAxOuwQCDhVx+ovW4j+DQ8Y+WiTEyfShNV9qVD0PBltz3omch1GjpFhQn6OaRvraeIDH9HXttb3FOjr2zzYG4yrrYbPSRWoYj63ZWiIP2O7zdl0caGQHezfNcYa2N0NTG99DGc3/q6EnhlvjWQsSbiEjmxcPx8fmV1i4DoflMQ383nsixAFapgrROUAtCgMvhWn1kSeoojKd+e4eKZxa/SNYulsBJWNFkmo1CZH4YTqlPM+IwYeDUOnOUGNxGurRZ3qQdWs2N2ZQhnrvlh+zpzurD2hwAz6gQXP7mxxMR1xHtAD8XQ+w4OiJK6VWjoIQKBgQDdZJvvZrV6tvqNwuTJkDZjbVU0iKkbP61rVE/6JpyzfGeS0WzGBNiCpbK3pJZnatK2nS7i9v8gAfIqGAk81NRKLa7Qbjgw6xHEwL8VZMXzN3KsMXgGM8EziPzicCYT8VBi/kXyV0ORqRz3rMQ+JOTkWRrcw943yYyTr84Dn0l0XQKBgQDUhFWJ3lKwOs7AlAAQqR1PjfpcRvSxVZ70BxTwnJoIQQyPQ0/OjCc1sit5s+h8xh0MeKSilCmvZerFlgNtvsCd6geSERXbpN+k9Vs3jAEkVeKHeUA/afmGqGCocanlarYu7uNRLfvpG7DduHBb4yJale/XGExNnwC0N+dkUU284wKBgBaOSojQiQrQm6RXx+F1TOVCXVz102zQRwXZWDCfQHXU5eSCa7edBMYCxbuKDDzLGF68kutSyNlk+VwqiL5m3J4WG2pm4FizimLmVFGEq9pEuu0qORVArp1mhoU3cdm0S0FasJupIlwzw5zEQFYogh11qpP1bK14XlcpoS6jSuONAoGBAJqMEljM4X1fhvPtrY5wLeyo56UrxM8h4RK+A7Bncm0GQUf+P4+JxQn7pDpBZ5U1zfI/2hqRfS8dAvrl+WBaFGHCy/ahji/JWwrvk4J1wm7WNoMm3l4/h0MyN/jHkDJSxGKlP5LNyiDgDmNvueZY66bM2zqlZPgd5bkp3pDJv6rZAoGAaP5e5F1j6s82Pm7dCpH3mRZWnfZIKqoNQIq2BO8vA9/WrdFI2C27uNhxCp2ZDMulRdBZcoeHcwJjnyDzg4I4gBZ2nSKkVdlN1REoTjLBBdlHi8XKiXzxvpItc2wjNC2AKHaJqj/dnh3bbTAQD1iUAxPmmLJYYkhfZ2i1IrTVxZE=");
        options.put(OptionalField.KEY.getField(), "key");
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
