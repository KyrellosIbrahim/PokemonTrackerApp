package com.example.pokemontrackerapp;

import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class row extends AppCompatActivity {
    SimpleCursorAdapter adapter;

    // special formatting for pokemon list appearance
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_row);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        ListView listView = findViewById(R.id.pokedexList);

        Cursor cursor = getContentResolver().query(
                PokemonProvider.CONTENT_URI,
                null,
                null,
                null,
                PokemonProvider.COL_NATNUM + " ASC"
        );

        String[] columns = {
                "_id",
                PokemonProvider.COL_NATNUM,
                PokemonProvider.COL_NAME,
                PokemonProvider.COL_SPECIES,
                PokemonProvider.COL_GENDER,
                PokemonProvider.COL_HEIGHT,
                PokemonProvider.COL_WEIGHT,
                PokemonProvider.COL_LEVEL,
                PokemonProvider.COL_HP,
                PokemonProvider.COL_ATTACK,
                PokemonProvider.COL_DEFENSE,
        };
        int[] views = {
                0,
                R.id.tvRowNationalNumber,
                R.id.tvRowName,
                R.id.tvRowSpecies,
                R.id.tvRowGender,
                R.id.tvRowStats,
                R.id.tvRowLevel,
                0,
                0,
                0
        };

        adapter = new SimpleCursorAdapter(
                this,
                R.layout.activity_pokemon_list,
                cursor,
                columns,
                views,
                0
        );

        adapter.setViewBinder((view, cursor1, columnIndex) -> {
            int viewId = view.getId();
            if (viewId == R.id.tvRowNationalNumber) {
                String natNum = cursor1.getString(columnIndex);
                ((TextView) view).setText(String.format("#%s", natNum));
                return true;
            }

            if (viewId == R.id.tvRowLevel) {
                int levelIndex = cursor1.getColumnIndex(PokemonProvider.COL_LEVEL);
                String level = cursor1.getString(levelIndex);
                ((TextView) view).setText(String.format("Lv. %s", level));
                return true;
            }

            if (viewId == R.id.tvRowStats) {
                // put HP, attack, and defense into one string for design
                int hpIndex = cursor1.getColumnIndex(PokemonProvider.COL_HP);
                int attackIndex = cursor1.getColumnIndex(PokemonProvider.COL_ATTACK);
                int defenseIndex = cursor1.getColumnIndex(PokemonProvider.COL_DEFENSE);

                String hp = cursor1.getString(hpIndex);
                String attack = cursor1.getString(attackIndex);
                String defense = cursor1.getString(defenseIndex);

                String stats = String.format("HP: %s | ATK: %s | DEF: %s", hp, attack, defense);
                ((TextView) view).setText(stats);
                return true;
            }
            return false;
        });

        listView.setAdapter(adapter);
        registerForContextMenu(listView);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.pokedexList) {
            menu.setHeaderTitle("Options");
            menu.add(0, 1, 0, "Delete");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
       if(item.getItemId() == 1) {
           AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

           Cursor cursor = (Cursor) adapter.getItem(info.position);

           int idIndex = cursor.getColumnIndex("_id");
           int id = cursor.getInt(idIndex);

           int rowsDeleted = getContentResolver().delete(
                   PokemonProvider.CONTENT_URI,
                   "_id = ?",
                   new String[]{String.valueOf(id)}
           );

           if(rowsDeleted > 0) {
               Toast.makeText(this, "Pokemon deleted", Toast.LENGTH_SHORT).show();
               // refresh the list
               Cursor newCursor = getContentResolver().query(
                       PokemonProvider.CONTENT_URI,
                       null,
                       null,
                       null,
                       PokemonProvider.COL_NATNUM + " ASC"
               );
               adapter.changeCursor(newCursor);
           }
           else {
               Toast.makeText(this, "Failed to delete Pokemon", Toast.LENGTH_SHORT).show();
           }
           return true;
       }
       return super.onContextItemSelected(item);
    }
}