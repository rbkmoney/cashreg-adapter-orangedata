package com.rbkmoney.adapter.orangedata.handler;

import com.rbkmoney.adapter.cashreg.spring.boot.starter.exception.UnsupportedMethodException;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.flow.StepResolver;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.handler.CommonHandler;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.EntryStateModel;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.ExitStateModel;
import com.rbkmoney.adapter.common.Validator;
import com.rbkmoney.adapter.orangedata.converter.entry.CtxToEntryModelConverter;
import com.rbkmoney.adapter.orangedata.converter.exit.ExitModelToProxyResultConverter;
import com.rbkmoney.adapter.orangedata.service.orangedata.model.Response;
import com.rbkmoney.adapter.orangedata.validator.CashregContextValidator;
import com.rbkmoney.damsel.cashreg.adapter.CashregAdapterSrv;
import com.rbkmoney.damsel.cashreg.adapter.CashregContext;
import com.rbkmoney.damsel.cashreg.adapter.CashregResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Primary
@Component
@RequiredArgsConstructor
public class OrangeDataServerHandler implements CashregAdapterSrv.Iface {

    private final CtxToEntryModelConverter ctxToEntryModelConverter;
    private final ExitModelToProxyResultConverter exitModelToProxyResultConverter;
    private final StepResolver stepResolver;
    private final CashregContextValidator cashregContextValidator;
    private final List<CommonHandler<ExitStateModel, ResponseEntity<Response>, EntryStateModel>> handlers;

    @Override
    public CashregResult register(CashregContext context) throws TException {
        return handle(
                cashregContextValidator,
                ctxToEntryModelConverter,
                exitModelToProxyResultConverter,
                context
        );
    }

    private <T, R> R handle(
            Validator<T> validator,
            Converter<T, EntryStateModel> entryConverter,
            Converter<ExitStateModel, R> exitConverter,
            T context
    ) throws TException {
        EntryStateModel entryStateModel = prepareEntryState(validator, entryConverter, context);
        ExitStateModel exitStateModel = handlers.stream()
                .filter(handler -> handler.isHandler(entryStateModel))
                .findFirst()
                .orElseThrow(UnsupportedMethodException::new)
                .handle(entryStateModel);
        exitStateModel.getAdapterContext().setNextStep(stepResolver.resolveExit(exitStateModel));
        return exitConverter.convert(exitStateModel);
    }

    private <T> EntryStateModel prepareEntryState(
            Validator<T> validator,
            Converter<T, EntryStateModel> entryConverter,
            T context
    ) {
        validator.validate(context);
        EntryStateModel entryStateModel = entryConverter.convert(context);
        entryStateModel.getState().getAdapterContext().setNextStep(stepResolver.resolveEntry(entryStateModel));
        return entryStateModel;
    }

}
