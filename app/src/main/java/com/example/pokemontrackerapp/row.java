package com.example.pokemontrackerapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class row extends AppCompatActivity {
    // special formatting for pokemon list appearance
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_row);

        ListView listView = findViewById(R.id.pokedexList);

        Cursor cursor = getContentResolver().query(
                PokemonProvider.CONTENT_URI,
                null,
                null,
                null,
                PokemonProvider.COL_NATNUM + " ASC"
        );

        String[] columns = {
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
                R.id.tvRowNationalNumber,
                R.id.tvRowName,
                R.id.tvRowSpecies,
                R.id.tvRowGender,
                R.id.tvRowStats,
                R.id.tvRowLevel,
                0,
                0
        };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.activity_row,
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
                String level = cursor1.getString(columnIndex);
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}