package com.geoffreyfrey.SQLite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.geoffreyfrey.bidfivehundredscorekeeper.R;

import java.util.ArrayList;

//Activity to allow people to browse the database

public class DatabaseExplorer extends AppCompatActivity {

    //Initializing database handler
    SQLiteDatabaseHandler dbHandler = new SQLiteDatabaseHandler(this);

    //Initializing spinner related variables
    Spinner tableSpinner;
    ArrayList<String> tableArrayList = new ArrayList<String>();
    ArrayAdapter adapter;

    Spinner columnSpinner;
    ArrayList<String> columnArrayList = new ArrayList<String>();
    ArrayAdapter adapterColumns;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Setting up the main views
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_explorer);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar_no_options);
        TextView toolbarText = (TextView) findViewById(R.id.toolbar_text_no_options);
        toolbarText.setText("DATABASE EXPLORER");
        setSupportActionBar(myToolbar);

        //Getting the database handler
//        dbHandler = new SQLiteDatabaseHandler(this);

        setUpVariables();

    }


    public void setUpVariables(){

        //Assigning View Values
        tableSpinner = (Spinner) findViewById(R.id.table_spinner);
        tableArrayList = dbHandler.getTableNames();

//        columnSpinner = (Spinner) findViewById(R.id.column_spinner);

        //Setting up the ListView
        if(tableArrayList!=null){
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tableArrayList);
            tableSpinner.setAdapter(adapter);
        }

        tableSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

//                columnArrayList = dbHandler.getColumnNames(tableArrayList.get(i));
//                ArrayAdapter adapterColumns = new ArrayAdapter(DatabaseExplorer.this, android.R.layout.simple_spinner_dropdown_item, columnArrayList);
//                columnSpinner.setAdapter(adapterColumns);
                setUpTable(tableArrayList.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){

            }
        });
    }

    public void setUpTable(String tableName){
        TableLayout table_layout = (TableLayout) findViewById(R.id.table);

        columnArrayList = dbHandler.getColumnNames(tableName);
        table_layout = dbHandler.makeTable(this, tableName, table_layout);

    }

}
