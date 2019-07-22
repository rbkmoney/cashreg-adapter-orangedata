package com.rbkmoney.adapter.orangedata.service.orangedata;


import com.rbkmoney.adapter.orangedata.configuration.properties.OrangeDataProperties;
import com.rbkmoney.adapter.orangedata.service.orangedata.constant.DocumentType;
import com.rbkmoney.adapter.orangedata.service.orangedata.constant.PaymentType;
import com.rbkmoney.adapter.orangedata.service.orangedata.model.*;
import com.rbkmoney.adapter.orangedata.utils.OptionsFields;
import com.rbkmoney.cashreg.proto.provider.CashRegContext;
import com.rbkmoney.cashreg.proto.provider.CompanyInfo;
import com.rbkmoney.cashreg.proto.provider.ItemsLine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.rbkmoney.adapter.orangedata.utils.OptionsFields.PAYMENT_TYPE;


@Slf4j
@Component
@RequiredArgsConstructor
public class OrangeDataClient implements AdapterCashReg {

    private final OrangeDataApi api;
    private final OrangeDataProperties orangeDataProperties;

    @Override
    public ResponseEntity<?> debit(CashRegContext context) {
        Request request = prepareRequest(context, DocumentType.INCOME);
        return api.create(request);
    }

    @Override
    public ResponseEntity<?> credit(CashRegContext context) {
        throw new RuntimeException("Method Credit Not Supported");
    }

    @Override
    public ResponseEntity<?> refundDebit(CashRegContext context) {
        Request request = prepareRequest(context, DocumentType.INCOME_REFUND);
        return api.create(request);
    }

    @Override
    public ResponseEntity<?> refundCredit(CashRegContext context) {
        throw new RuntimeException("Method Refund Credit Not Supported");
    }

    @Override
    public ResponseEntity<?> getStatus(CashRegContext context) {
        Map<String, String> options = context.getOptions();
        String url = options.get(OptionsFields.URL);
        api.setUrl(url);
        return api.status(context.getAccountInfo().getCompanyInfo().getInn(), context.getRequestId());
    }

    public Request prepareRequest(CashRegContext context, int documentType) {

        Map<String, String> options = context.getOptions();
        String url = options.get(OptionsFields.URL);
        String privateKey = options.get(OptionsFields.PRIVATE_KEY);
        api.setUrl(url);
        api.setPrivateKey(privateKey);

        // Items - begin
        List<Positions> positions = prepareCart(
                context.getPaymentInfo().getCart().getLines(), options
        );
        // Items - end

        if (positions.isEmpty()) {
            String message = String.format("OrangeData: List items is empty");
            log.error(message);
            throw new IllegalArgumentException(message);
        }

        Request request = new Request();

        CompanyInfo companyInfo = context.getAccountInfo().getCompanyInfo();
        request.setId(context.getRequestId());
        request.setInn(companyInfo.getInn());

        if (options.get(OptionsFields.GROUP) != null) {
            request.setGroup(options.get(OptionsFields.GROUP));
        }

        if (options.get(OptionsFields.KEY) != null) {
            request.setKey(options.get(OptionsFields.KEY));
        }

        Content content = new Content();
        content.setType(DocumentType.INCOME);
        content.setPositions(positions);

        CheckClose checkClose = new CheckClose();
        checkClose.setTaxationSystem(Integer.valueOf(companyInfo.getTaxMode().getValue()));

        List<Payments> payments = preparePaymentForCart(
                context.getPaymentInfo().getCart().getLines(), options

        );

        checkClose.setPayments(payments);
        content.setCheckClose(checkClose);

        content.setCustomerContact(context.getPaymentInfo().getPayer().getContactInfo().getEmail());
        request.setContent(content);
        return request;
    }


    public List<Positions> prepareCart(List<ItemsLine> itemsLines, Map<String, String> options) {
        List<Positions> positions = new ArrayList<>();

        if (!itemsLines.isEmpty()) {
            itemsLines.forEach(itemLine -> {
                Positions item = new Positions();
                item.setPrice(new BigDecimal(itemLine.getPrice().getAmount()).divide(new BigDecimal(100)).setScale(2));
                item.setQuantity(BigDecimal.valueOf(itemLine.getQuantity()));

                String invoiceTaxId = itemLine.getTax();
                Integer taxId = (invoiceTaxId != null && !invoiceTaxId.isEmpty())
                        ? com.rbkmoney.adapter.orangedata.service.orangedata.constant.Vat.codeTextOf(invoiceTaxId).getCode()
                        : com.rbkmoney.adapter.orangedata.service.orangedata.constant.Vat.NO_VAT.getCode();

                item.setTax(taxId);
                item.setText(itemLine.getProduct());
                positions.add(item);
            });

        }

        return positions;
    }

    public List<Payments> preparePaymentForCart(List<ItemsLine> itemsLines, Map<String, String> options) {
        List<Payments> payments = new ArrayList<>();

        if (!itemsLines.isEmpty()) {
            itemsLines.forEach(itemLine -> {
                Payments payment = new Payments();

                BigDecimal sum = new BigDecimal(itemLine.getPrice().getAmount())
                        .multiply(new BigDecimal(itemLine.getQuantity()))
                        .divide(new BigDecimal(100)).setScale(2);

                payment.setAmount(sum);

                if (options.get(PAYMENT_TYPE) != null) {
                    payment.setType(Integer.valueOf(options.get(PAYMENT_TYPE)));
                } else {
                    payment.setType(PaymentType.E_CASH.getCode());
                }

                payments.add(payment);
            });

        }

        return payments;
    }


}
