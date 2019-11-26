package com.rbkmoney.adapter.orangedata.service.orangedata.signer;

public interface Signer {
    String sign(String data, String privateKey);
}
