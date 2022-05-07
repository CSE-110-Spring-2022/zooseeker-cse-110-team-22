package com.example.zooseeker;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class ZooExhibitsItemDaoTest {
    private ZooExhibitsItemDao dao;
    private ZooDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, ZooDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = db.zooExhibitsItemDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void testInsert() {
        ZooExhibitsItem item1 = new ZooExhibitsItem("lion", "exhibit", "lion", null);
        ZooExhibitsItem item2 = new ZooExhibitsItem("monkey", "exhibit", "monkey", null);

        long identification1 = dao.insert(item1);
        long identification2 = dao.insert(item2);

        assertNotEquals(identification1, identification2);
    }

    @Test
    public void testGet() {
        ZooExhibitsItem insertedItem = new ZooExhibitsItem("gator","exhibit", "gator", null);
        long identification = dao.insert(insertedItem);

        ZooExhibitsItem item = dao.get(identification);
        assertEquals(identification, item.number_assign);
        assertEquals(insertedItem.name, item.name);
        assertEquals(insertedItem.kind, item.kind);
        assertEquals(insertedItem.tags, item.tags);
    }

    @Test
    public void testUpdate() {
        ZooExhibitsItem item = new ZooExhibitsItem("giraffe","exhibit", "giraffe", null);
        long identification = dao.insert(item);

        item = dao.get(identification);
        item.name = "dead_giraffe";
        int itemsUpdated = dao.update(item);
        assertEquals(1, itemsUpdated);

        item = dao.get(identification);
        assertNotNull(item);
        assertEquals("dead_giraffe", item.name);
    }

    @Test
    public void testDelete() {
        ZooExhibitsItem item = new ZooExhibitsItem("gorilla","exhibit", "gorilla", null);
        long identification = dao.insert(item);

        item = dao.get(identification);
        int itemsDeleted = dao.delete(item);
        assertEquals(1, itemsDeleted);
        assertNull(dao.get(identification));
    }
}