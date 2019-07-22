package com.rbkmoney.adapter.orangedata.utils.wrapper.cashreg.creators;

import com.rbkmoney.cashreg.proto.provider.*;

public class CashRegProviderCreators {

    public static Intent createFinishIntentSuccess(String uuid) {
        return Intent.finish(new FinishIntent(createFinishStatusSuccess(uuid)));
    }

    public static Intent createFinishIntentSuccess(Payload payload) {
        return Intent.finish(new FinishIntent(createFinishStatusSuccess(payload)));
    }

    public static Intent createFinishIntentFailure(Failure failure) {
        return Intent.finish(new FinishIntent(createFinishStatusFailure(failure)));
    }

    public static FinishStatus createFinishStatusSuccess(String uuid) {
        Success success = new Success();
        success.setUuid(uuid);
        return FinishStatus.success(success);
    }

    public static FinishStatus createFinishStatusSuccess(Payload payload) {
        Success success = new Success();
        success.setPayload(payload);
        return FinishStatus.success(success);
    }

    public static FinishStatus createFinishStatusFailure(Failure failure) {
        return FinishStatus.failure(failure);
    }

    public static Failure createFailure(String code) {
        return createFailure(code, code);
    }

    public static Failure createFailure(String code, String message) {
        Failure failure = new Failure();
        failure.setCode(code);
        failure.setDescription(message);
        return failure;
    }

    public static CashRegResult createCashRegResult(Intent intent, String originalResponse) {
        CashRegResult cashRegResult = new CashRegResult();
        cashRegResult.setIntent(intent);
        cashRegResult.setOriginalResponse(originalResponse);
        return cashRegResult;
    }

}
