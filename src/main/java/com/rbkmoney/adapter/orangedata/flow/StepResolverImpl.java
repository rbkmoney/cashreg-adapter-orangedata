package com.rbkmoney.adapter.orangedata.flow;

import com.rbkmoney.adapter.cashreg.spring.boot.starter.flow.StepResolver;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.EntryStateModel;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.ExitStateModel;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.Step;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StepResolverImpl implements StepResolver<EntryStateModel, ExitStateModel> {

    @Override
    public Step resolveEntry(EntryStateModel stateModel) {
        return (stateModel.getState().getAdapterContext().getNextStep() == null)
                ? Step.CREATE : Step.CHECK_STATUS;
    }

    @Override
    public Step resolveExit(ExitStateModel stateModel) {
        return stateModel.getAdapterContext().getNextStep();
    }

}
