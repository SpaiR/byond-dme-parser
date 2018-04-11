package io.github.spair.byond.dme;

import org.junit.Test;

import static org.junit.Assert.*;

public class DmeTest {

    @Test
    public void testGetItemOrCreate() {
        Dme dme = new Dme();
        assertEquals(0, dme.getItems().size());

        DmeItem item = dme.getItemOrCreate("/atom");
        assertNotNull(item);
        assertEquals(1, dme.getItems().size());
        assertEquals("/atom", item.getType());
    }

    @Test
    public void testGetGlobalObject() {
        Dme dme = new Dme();
        dme.addItem(new DmeItem(ByondTypes.GLOBAL, dme));

        assertEquals(ByondTypes.GLOBAL, dme.getGlobalObject().getType());
    }
}