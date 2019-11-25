package com.rbkmoney.adapter.orangedata.converter.entry;

import com.rbkmoney.adapter.cashreg.spring.boot.starter.constant.OptionalField;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.EntryStateModel;
import com.rbkmoney.adapter.orangedata.AbstractIntegrationTest;
import com.rbkmoney.adapter.orangedata.TestData;
import com.rbkmoney.damsel.cashreg.type.Debit;
import com.rbkmoney.damsel.cashreg.type.Type;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class CtxToEntryModelConverterTest extends AbstractIntegrationTest {

    @Autowired
    private CtxToEntryModelConverter converter;

    // Проверка конвертера по маппингу входящего контекста на EntryStateModel
    @Test
    public void testCtxToEntryModelConverter() {
        EntryStateModel entryStateModel = converter.convert(makeCashRegContext(Type.debit(new Debit())));
        assertEquals(TestData.TEST_EMAIL, entryStateModel.getCompany().getEmail());
        assertEquals(TestData.TEST_EMAIL, entryStateModel.getClient().getEmail());
        assertEquals(TestData.LOGIN, entryStateModel.getAuth().getLogin());
        assertEquals(new BigDecimal(1).setScale(2), entryStateModel.getTotal());
        assertEquals(TestData.LOGIN, entryStateModel.getOptions().get(OptionalField.LOGIN.getField()));
        assertEquals(TestData.COMPANY_INN, entryStateModel.getCompany().getInn());
        assertTrue(entryStateModel.getPayments().size() == 0);
        assertTrue(entryStateModel.getVats().size() == 0);
        assertTrue(entryStateModel.getItems().size() == 0);
    }

}
