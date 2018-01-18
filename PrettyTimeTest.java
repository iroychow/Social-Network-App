package com.example.ishitaroychowdhury.socialapp;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.*;
import org.junit.*;
import org.ocpsoft.prettytime.*;
/**
 * Created by ishitaroychowdhury on 11/21/17.
 */

public class PrettyTimeTest {

    @Test
    public void testMinutesAgo() throws Exception
    {
        Date d=new Date(1000 * 60 * 12);
        PrettyTime t = new PrettyTime(d);
        assertEquals("12 minutes ago", t.format(new Date(0)));
    }

}
