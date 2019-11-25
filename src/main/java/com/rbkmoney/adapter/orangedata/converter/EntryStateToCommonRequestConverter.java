package com.rbkmoney.adapter.orangedata.converter;

import com.rbkmoney.adapter.cashreg.spring.boot.starter.config.properties.AdapterCashRegProperties;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.constant.OptionalField;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.EntryStateModel;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.Items;
import com.rbkmoney.adapter.orangedata.service.orangedata.constant.PaymentType;
import com.rbkmoney.adapter.orangedata.service.orangedata.constant.TaxationSystem;
import com.rbkmoney.adapter.orangedata.service.orangedata.constant.Vat;
import com.rbkmoney.adapter.orangedata.service.orangedata.model.*;
import com.rbkmoney.damsel.cashreg_domain.TaxMode;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.rbkmoney.adapter.orangedata.service.orangedata.constant.OptionalField.PAYMENT_TYPE;

@Component
@RequiredArgsConstructor
public class EntryStateToCommonRequestConverter implements Converter<EntryStateModel, RequestWrapper<Request>> {

    private final AdapterCashRegProperties adapterCashRegProperties;

    @Override
    public RequestWrapper<Request> convert(EntryStateModel entryStateModel) {
        Request request = new Request();
        request.setId(entryStateModel.getCashRegId());
        Map<String, String> options = entryStateModel.getOptions();

        if (options.get(OptionalField.GROUP.getField()) != null) {
            request.setGroup(options.get(OptionalField.GROUP.getField()));
        }

        request.setInn(entryStateModel.getCompany().getInn());
        request.setKey(options.get(OptionalField.KEY.getField()));

        Content content = new Content();
        List<Items> listItems = entryStateModel.getItems();
        List<Positions> positions = prepareCart(listItems);
        content.setPositions(positions);

        CheckClose checkClose = new CheckClose();
        checkClose.setTaxationSystem(convertTaxMode(entryStateModel.getCompany().getSno()));

        List<Payments> payments = preparePaymentForCart(listItems, options);
        checkClose.setPayments(payments);
        content.setCheckClose(checkClose);

        content.setCustomerContact(entryStateModel.getClient().getEmail());
        request.setContent(content);
        return new RequestWrapper<>(
                request,
                options.getOrDefault(OptionalField.URL.getField(), adapterCashRegProperties.getUrl()),
                options.get(OptionalField.GROUP.getField()),
                options.get(OptionalField.COMPANY_NAME.getField()),
                options.get(OptionalField.COMPANY_ADDRESS.getField()),
                options.get(OptionalField.PRIVATE_KEY.getField()),
                options.get(OptionalField.KEY.getField())
        );
    }

    private static Integer convertTaxMode(String taxMode) {
        TaxMode cashregTaxMode = TaxMode.valueOf(taxMode);
        switch (cashregTaxMode) {
            case osn:
                return TaxationSystem.GENERAL.getCode();
            case usn_income:
                return TaxationSystem.SIMPLIFIED_INCOME.getCode();
            case usn_income_outcome:
                return TaxationSystem.SIMPLIFIED_REVENUE_MINUS_CONSUMPTION.getCode();
            case envd:
                return TaxationSystem.UNIFIED_TAX_ON_IMPUTED_INCOME.getCode();
            case esn:
                return TaxationSystem.UNIFIED_AGRICULTURAL_TAX.getCode();
            case patent:
                return TaxationSystem.PATENT_SYSTEM_OF_TAXATION.getCode();
            default:
                return 0;
        }
    }

    private List<Positions> prepareCart(List<Items> itemsList) {
        List<Positions> positions = new ArrayList<>();
        itemsList.forEach(items -> {
            Positions item = new Positions();
            item.setPrice(items.getPrice());
            item.setQuantity(items.getQuantity());
            item.setTax(extractTaxId(items.getVat().getType()));
            item.setText(items.getName());
            positions.add(item);
        });
        return positions;
    }

    private Integer extractTaxId(String invoiceTaxId) {
        return (invoiceTaxId == null || invoiceTaxId.isEmpty())
                ? Vat.NO_VAT.getCode()
                : Vat.codeTextOf(invoiceTaxId).getCode();
    }

    private List<Payments> preparePaymentForCart(List<Items> itemsList, Map<String, String> options) {
        List<Payments> payments = new ArrayList<>();
        itemsList.forEach(items -> fillPayments(options, payments, items));
        return payments;
    }

    private void fillPayments(Map<String, String> options, List<Payments> payments, Items items) {
        Payments payment = new Payments();
        payment.setAmount(items.getPrice().multiply(items.getQuantity()));
        if (options.get(PAYMENT_TYPE.getField()) != null) {
            payment.setType(Integer.valueOf(options.get(PAYMENT_TYPE.getField())));
        } else {
            payment.setType(PaymentType.E_CASH.getCode());
        }
        payments.add(payment);
    }

}
