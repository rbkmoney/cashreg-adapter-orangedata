package com.rbkmoney.adapter.orangedata.service.orangedata.signer;

public interface Signer {

    public static final String KEY_ALGORITHM = "RSA";
    public static final String HASH_ALGORITHM = "SHA256withRSA";

    String sign(String data, String privateKey);

}
