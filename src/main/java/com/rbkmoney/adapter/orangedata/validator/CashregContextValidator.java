package com.rbkmoney.adapter.orangedata.validator;

import com.rbkmoney.adapter.cashreg.spring.boot.starter.constant.OptionalField;
import com.rbkmoney.adapter.common.Validator;
import com.rbkmoney.damsel.cashreg.adapter.CashregContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CashregContextValidator implements Validator<CashregContext> {

    @Override
    public void validate(CashregContext context) {
        Map<String, String> options = context.getOptions();
        validateRequiredFields(options);
    }

    private void validateRequiredFields(Map<String, String> options) {
        Objects.requireNonNull(options.get(OptionalField.PRIVATE_KEY.getField()), "Option 'private_key' must be set");
        Objects.requireNonNull(options.get(OptionalField.KEY.getField()), "Option 'key' must be set");
        Objects.requireNonNull(options.get(OptionalField.PAYMENT_METHOD.getField()), "Option 'payment_method' must be set");
        Objects.requireNonNull(options.get(OptionalField.PAYMENT_OBJECT.getField()), "Option 'payment_object' must be set");
        Objects.requireNonNull(options.get(OptionalField.GROUP.getField()), "Option 'group' must be set");
    }
}
