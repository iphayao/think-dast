package com.phayao.thinkdast;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;

public class ListClientExampleTest {
    @Test
    public void testListClientExample() {
        ListClientExample lce = new ListClientExample();
        List list = lce.getList();
        assertThat(list, instanceOf(ArrayList.class));
    }
}
