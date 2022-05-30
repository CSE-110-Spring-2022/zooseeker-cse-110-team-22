package com.example.zooseeker;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class ExhibitManagerTest {

    public ExhibitManager exhibitManager;

    @Before
    public void initalizeManager(){
        Reader exhibitsReader = null;
        Context context = ApplicationProvider.getApplicationContext();
        try {
            exhibitsReader = new InputStreamReader(context.getAssets().open("exhibit_info.json"));
        } catch (IOException e) {
            throw new RuntimeException("Unable to load data");
        }

        //loading exhibits
        exhibitManager = new ExhibitManager(exhibitsReader);
    }
    @Test
    public void testClosest() {
        double mockLat = 32.72109826903826;
        double mockLng = -117.15952052282296;
        String name = exhibitManager.getClosest(mockLat, mockLng).name;
        assertEquals(name, "Koi Fish");

    }
}