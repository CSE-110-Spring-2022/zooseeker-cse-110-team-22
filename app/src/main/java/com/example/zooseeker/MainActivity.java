package com.example.zooseeker;

import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // List View object
    ListView listView;

    // Define array adapter for ListView
    ArrayAdapter<String> myAdapter;

    // Define array List for List View data
    ArrayList<String> mylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialise ListView with id
        listView = findViewById(R.id.listView);

        // Add items to Array List
        mylist = new ArrayList<>();

        //database creation, not filling database with json file?
        ZooDatabase zooNodes = ZooDatabase.getSingleton(this);
        ZooExhibitsItemDao dao = zooNodes.zooExhibitsItemDao();

        //use getAllType when fixed, size currently 0
        List<ZooExhibitsItem> exhibits = dao.getAll();
        for(int i = 0; i < exhibits.size(); i++){
            mylist.add(exhibits.get(i).name);
            System.out.println(exhibits.get(i).name);
        }

        /*
        mylist.add("Camel");
        mylist.add("Toad");
        mylist.add("Tiger");
        mylist.add("Lion");
        mylist.add("Elephant");
        mylist.add("Cheetah");
        mylist.add("Kangaroo");
        mylist.add("Giraffe");
        mylist.add("Panther");

         */
        // Set adapter to ListView
        myAdapter
                = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                mylist);
        listView.setAdapter(myAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu with items using MenuInflator
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        // Initialise menu item search bar
        // with id and take its object
        MenuItem searchViewItem
                = menu.findItem(R.id.search_bar);
        SearchView searchView
                = (SearchView) MenuItemCompat
                .getActionView(searchViewItem);

        // attach setOnQueryTextListener
        // to search view defined above
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {

                    // Override onQueryTextSubmit method
                    // which is call
                    // when submitquery is searched

                    @Override
                    public boolean onQueryTextSubmit(String searchQuery) {
                        // If the list contains the search query
                        // than filter the adapter
                        // using the filter method
                        // with the query as its argument
                        if (mylist.contains(searchQuery)) {
                            myAdapter.getFilter().filter(searchQuery);
                        } else {
                            // Search query not found in List View
                            Toast
                                    .makeText(MainActivity.this,
                                            "Query not found",
                                            Toast.LENGTH_LONG)
                                    .show();
                        }
                        return false;
                    }

                    // This method is overridden to filter
                    // the adapter according to a search query
                    // when the user is typing search
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        myAdapter.getFilter().filter(newText);
                        return false;
                    }
                });

        return super.onCreateOptionsMenu(menu);
    }
}
