package com.rbkmoney.adapter.orangedata.service.orangedata.signer;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class AsymSigner implements com.rbkmoney.adapter.orangedata.service.orangedata.signer.Signer {

    public static final String KEY_ALGORITHM = "RSA";
    public static final String HASH_ALGORITHM = "SHA256withRSA";

    private KeyFactory keyFactory;
    private Signature sig;

    public AsymSigner() {
        try {
            this.keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            this.sig = Signature.getInstance(HASH_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new UnknownCryptoException(e);
        }
    }

    @Override
    public synchronized String sign(String data, String privateKey) {
        try {
            byte[] encodedPrivateKey = java.util.Base64.getDecoder().decode(privateKey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
            PrivateKey privKey = keyFactory.generatePrivate(keySpec);
            sig.initSign(privKey);
            sig.update(data.getBytes(StandardCharsets.UTF_8));
            return java.util.Base64.getEncoder().encodeToString(sig.sign());
        } catch (InvalidKeySpecException | InvalidKeyException | SignatureException e) {
            throw new UnknownCryptoException(e);
        }
    }

}
