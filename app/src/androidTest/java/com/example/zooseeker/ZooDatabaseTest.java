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
public class ZooDatabaseTest {
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
        ZooExhibitsItem item1 = new ZooExhibitsItem("exhibit", "lion", null);
        ZooExhibitsItem item2 = new ZooExhibitsItem("exhibit", "monkey", null);

        long id1 = dao.insert(item1);
        long id2 = dao.insert(item2);

        assertNotEquals(id1, id2);
    }

    public void testUpdate() {
        ZooExhibitsItem item = new ZooExhibitsItem("exhibits", "giraffe", null);
        long id = dao.insert(item);

        item = dao.get(id);
        item.name = "dead_giraffe";
        int itemsUpdated = dao.update(item);
        assertEquals(1, itemsUpdated);

        item = dao.get(id);
        assertNotNull(item);
        assertEquals("dead_giraffe", item.name);
    }

    @Test
    public void testDelete() {
        ZooExhibitsItem item = new ZooExhibitsItem("exhibits", "gorilla", null);
        long id = dao.insert(item);

        item = dao.get(id);
        int itemsDeleted = dao.delete(item);
        assertEquals(1, itemsDeleted);
        assertNull(dao.get(id));
    }
}