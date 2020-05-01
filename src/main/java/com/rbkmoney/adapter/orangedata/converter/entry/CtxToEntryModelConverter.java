package com.rbkmoney.adapter.orangedata.converter.entry;

import com.rbkmoney.adapter.cashreg.spring.boot.starter.constant.OptionalField;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.converter.CashregAdapterContextConverter;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.flow.TargetTypeResolver;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.*;
import com.rbkmoney.damsel.cashreg.adapter.CashregContext;
import com.rbkmoney.damsel.cashreg.domain.PaymentInfo;
import com.rbkmoney.damsel.cashreg.domain.RussianLegalEntity;
import com.rbkmoney.damsel.cashreg.domain.TaxMode;
import com.rbkmoney.damsel.cashreg.receipt.Cart;
import com.rbkmoney.damsel.cashreg.receipt.ItemsLine;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CtxToEntryModelConverter implements Converter<CashregContext, EntryStateModel> {

    private final CashregAdapterContextConverter cashregAdapterContextConveter;

    @Override
    public EntryStateModel convert(CashregContext context) {
        Map<String, String> options = context.getOptions();
        PaymentInfo paymentInfo = context.getSourceCreation().getPayment();
        RussianLegalEntity russianLegalEntity = context.getAccountInfo().getLegalEntity().getRussianLegalEntity();
        List<ItemsLine> itemsLines = paymentInfo.getCart().getLines();
        List<Payments> payments = itemsLines.stream()
                .map(this::preparePayments)
                .collect(Collectors.toList());
        List<Vat> vats = itemsLines.stream()
                .map(this::prepareVat)
                .collect(Collectors.toList());
        AdapterState adapterState = cashregAdapterContextConveter.convert(context);

        return EntryStateModel.builder()
                .options(context.getOptions())
                .cashRegId(context.getCashregId())
                .auth(Auth.builder()
                        .login(options.get(OptionalField.LOGIN.getField()))
                        .pass(options.get(OptionalField.PASS.getField()))
                        .build()
                )
                .client(Client.builder()
                        .email(paymentInfo.getEmail())
                        .build()
                )
                .company(Company.builder()
                        .email(russianLegalEntity.getEmail())
                        .inn(russianLegalEntity.getInn())
                        .paymentAddress(russianLegalEntity.getActualAddress())
                        .sno(TaxMode.findByValue(russianLegalEntity.getTaxMode().getValue()).name())
                        .build()
                )
                .items(prepareCart(paymentInfo.getCart(), options))
                .total(prepareAmount(paymentInfo.getCash().getAmount()))
                .payments(payments)
                .vats(vats)
                .state(StateModel.builder()
                        .targetType(TargetTypeResolver.resolve(context.getSession().getType()))
                        .adapterContext(adapterState)
                        .build()
                )
                .build();
    }

    private Vat prepareVat(ItemsLine itemsLine) {
        return Vat.builder()
                .sum(prepareAmount(itemsLine.getPrice().getAmount()))
                .type(itemsLine.getTax())
                .build();
    }

    private Payments preparePayments(ItemsLine itemsLine) {
        BigDecimal fullPriceAmount = prepareAmount(itemsLine.getPrice().getAmount() * itemsLine.getQuantity());
        return Payments.builder()
                .sum(fullPriceAmount)
                .type(1)
                .build();
    }

    private BigDecimal prepareAmount(Long amount) {
        return new BigDecimal(amount).movePointLeft(2);
    }

    private List<Items> prepareCart(Cart cart, Map<String, String> options) {
        List<Items> itemsList = new ArrayList<>();
        cart.getLines().forEach(itemsLine -> prepareItemList(options, itemsList, itemsLine));
        return itemsList;
    }

    private void prepareItemList(Map<String, String> options, List<Items> itemsList, ItemsLine itemsLine) {
        BigDecimal sum = prepareAmount(itemsLine.getPrice().getAmount() * itemsLine.getQuantity());
        itemsList.add(Items.builder()
                .quantity(new BigDecimal(itemsLine.getQuantity()).setScale(1))
                .price(prepareAmount(itemsLine.getPrice().getAmount()))
                .sum(sum)
                .vat(Vat.builder().type(itemsLine.getTax()).build())
                .paymentMethod(options.get(OptionalField.PAYMENT_METHOD.getField()))
                .paymentObject(options.get(OptionalField.PAYMENT_OBJECT.getField()))
                .name(itemsLine.getProduct())
                .build()
        );
    }

}
