package com.example.pokemontrackerapp;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextView tvNatNumber, tvName, tvSpecies, tvGender, tvHeight, tvWeight, tvLevel, tvHP, tvAttack, tvDefense;
    private EditText nationalNumber, name, species, height, weight, hp, attack, defense;
    private Spinner level;
    private RadioGroup genderGroup;

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.constraint);
        // Initialize UI components
        tvNatNumber = findViewById(R.id.tvNationalNumber);
        tvName = findViewById(R.id.tvName);
        tvSpecies = findViewById(R.id.tvSpecies);
        tvGender = findViewById(R.id.tvGender);
        tvHeight = findViewById(R.id.tvHeight);
        tvWeight = findViewById(R.id.tvWeight);
        tvLevel = findViewById(R.id.tvLevel);
        tvHP = findViewById(R.id.tvHP);
        tvAttack = findViewById(R.id.tvAttack);
        tvDefense = findViewById(R.id.tvDefense);
        nationalNumber = findViewById(R.id.evNationalNumber);
        name = findViewById(R.id.evName);
        species = findViewById(R.id.evSpecies);
        height = findViewById(R.id.evHeight);
        weight = findViewById(R.id.evWeight);
        hp = findViewById(R.id.evHP);
        attack = findViewById(R.id.evAttack);
        defense = findViewById(R.id.evDefense);
        genderGroup = findViewById(R.id.rgGender);

        level = findViewById(R.id.sLevel); // Spinner for level selection
        List<String> levels = new LinkedList<>();
        for (int i = 1; i <= 50; i++) {
            levels.add(i+ "");
        }
        ArrayAdapter<String> levelAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, levels);
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        level.setAdapter(levelAdapter);

        InputFilter nationalNumberFilter = (source, start, end, dest, dstart, dend) -> {
            for (int i = start; i < end; i++) {
                if (!Character.isDigit(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        };
        nationalNumber.setFilters(new InputFilter[]{nationalNumberFilter, new InputFilter.LengthFilter(4)});

        InputFilter nameFilter = (source, start, end, dest, dstart, dend) -> {
            for (int i = start; i < end; i++) {
                char c = source.charAt(i);
                if(!Character.isLetter(c) && c != ' ' && c != '.') { // only letters, spaces, and periods
                    return "";
                }
            }
            return null;
        };
        species.setFilters(new InputFilter[]{nameFilter, new InputFilter.LengthFilter(12)});

        InputFilter speciesFilter = (source, start, end, dest, dstart, dend) -> {
            for (int i = start; i < end; i++) {
                char c = source.charAt(i);
                if(!Character.isLetter(c) && c != ' ') { // only letters and space
                    return "";
                }
            }
            return null;
        };
        species.setFilters(new InputFilter[]{speciesFilter, new InputFilter.LengthFilter(20)});

        Button collectionButton = findViewById(R.id.viewCollectionButton);
        collectionButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, row.class);
            startActivity(intent);
        });


        Button resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(v -> resetFields());

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> saveChecks());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void resetFields() {
        nationalNumber.setText(R.string._896);
        name.setText(R.string.glastrier);
        species.setText(R.string.wild_horse_pok_mon);
        height.setText(R.string._2_2);
        weight.setText(R.string._800_0);
        hp.setText("0");
        attack.setText("0");
        defense.setText("0");
        level.setSelection(0); // Reset spinner
    }

    private void saveChecks() {
        // list of error messages to display at the end
        List<String> errors = new ArrayList<>();

        // ---------- National Number ----------
        String natNumStr = nationalNumber.getText().toString().trim();
        int natNum = -1;
        if (natNumStr.isEmpty()) {
            errors.add("National Number is required");
            tvNatNumber.setTextColor(Color.RED);
        } else {
            try {
                natNum = Integer.parseInt(natNumStr);
                if (natNum < 1 || natNum > 1010) {
                    errors.add("National Number must be between 1 and 1010");
                    tvNatNumber.setTextColor(Color.RED);
                } else {
                    tvNatNumber.setTextColor(Color.BLACK);
                }
            } catch (NumberFormatException e) {
                errors.add("National Number must be a valid number");
                tvNatNumber.setTextColor(Color.RED);
            }
        }

        // ---------- Name ----------
        String nameStr = name.getText().toString().trim();
        if (nameStr.isEmpty() || nameStr.length() < 3 || nameStr.length() > 12) {
            errors.add("Name must be between 3 and 12 characters");
            tvName.setTextColor(Color.RED);
        } else {
            tvName.setTextColor(Color.BLACK);
        }

        // ---------- Species ----------
        String speciesStr = species.getText().toString().trim();
        if (speciesStr.isEmpty() || speciesStr.length() < 3 || speciesStr.length() > 20) {
            errors.add("Species must be between 3 and 20 characters");
            tvSpecies.setTextColor(Color.RED);
        } else {
            tvSpecies.setTextColor(Color.BLACK);
        }

        // ---------- Gender ----------
        int genderId = genderGroup.getCheckedRadioButtonId();
        if (genderId == -1) {
            errors.add("Please select a gender");
            tvGender.setTextColor(Color.RED);
        } else {
            // optional stricter check (no Unknown):
            if (genderId == R.id.rbUnknown) {
                errors.add("Gender must be Male or Female");
                tvGender.setTextColor(Color.RED);
            } else {
                tvGender.setTextColor(Color.BLACK);
            }
        }

        // ---------- HP ----------
        String hpStr = hp.getText().toString().trim();
        if (hpStr.isEmpty()) {
            errors.add("HP is required");
            tvHP.setTextColor(Color.RED);
        } else {
            try {
                int hpNum = Integer.parseInt(hpStr);
                if (hpNum < 1 || hpNum > 362) {
                    errors.add("HP must be between 1 and 362");
                    tvHP.setTextColor(Color.RED);
                } else {
                    tvHP.setTextColor(Color.BLACK);
                }
            } catch (NumberFormatException e) {
                errors.add("HP must be a valid number");
                tvHP.setTextColor(Color.RED);
            }
        }

        // ---------- Attack ----------
        String attackStr = attack.getText().toString().trim();
        if (attackStr.isEmpty()) {
            errors.add("Attack is required");
            tvAttack.setTextColor(Color.RED);
        } else {
            try {
                int attackNum = Integer.parseInt(attackStr);
                if (attackNum < 0 || attackNum > 526) {
                    errors.add("Attack must be between 0 and 526");
                    tvAttack.setTextColor(Color.RED);
                } else {
                    tvAttack.setTextColor(Color.BLACK);
                }
            } catch (NumberFormatException e) {
                errors.add("Attack must be a valid number");
                tvAttack.setTextColor(Color.RED);
            }
        }

        // ---------- Defense ----------
        String defenseStr = defense.getText().toString().trim();
        if (defenseStr.isEmpty()) {
            errors.add("Defense is required");
            tvDefense.setTextColor(Color.RED);
        } else {
            try {
                int defenseNum = Integer.parseInt(defenseStr);
                if (defenseNum < 10 || defenseNum > 614) {
                    errors.add("Defense must be between 10 and 614");
                    tvDefense.setTextColor(Color.RED);
                } else {
                    tvDefense.setTextColor(Color.BLACK);
                }
            } catch (NumberFormatException e) {
                errors.add("Defense must be a valid number");
                tvDefense.setTextColor(Color.RED);
            }
        }

        // ---------- Height ----------
        String heightStr = height.getText().toString().trim();
        if (heightStr.isEmpty()) {
            errors.add("Height is required");
            tvHeight.setTextColor(Color.RED);
        } else {
            try {
                double heightNum = Double.parseDouble(heightStr);
                if (heightNum < 0.2 || heightNum > 169.99) {
                    errors.add("Height must be between 0.2 and 169.99");
                    tvHeight.setTextColor(Color.RED);
                } else {
                    tvHeight.setTextColor(Color.BLACK);
                }
            } catch (NumberFormatException e) {
                errors.add("Height must be a valid number");
                tvHeight.setTextColor(Color.RED);
            }
        }

        // ---------- Weight ----------
        String weightStr = weight.getText().toString().trim();
        if (weightStr.isEmpty()) {
            errors.add("Weight is required");
            tvWeight.setTextColor(Color.RED);
        } else {
            try {
                double weightNum = Double.parseDouble(weightStr);
                if (weightNum < 0.1 || weightNum > 992.7) {
                    errors.add("Weight must be between 0.1 and 992.7");
                    tvWeight.setTextColor(Color.RED);
                } else {
                    tvWeight.setTextColor(Color.BLACK);
                }
            } catch (NumberFormatException e) {
                errors.add("Weight must be a valid number");
                tvWeight.setTextColor(Color.RED);
            }
        }

        // ---------- Final decision ----------
        if (!errors.isEmpty()) {
            // join all error messages into one toast
            String message = TextUtils.join("\n", errors);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        } else {
            // update database on submission
            ContentValues values = new ContentValues();
            values.put(PokemonProvider.COL_NATNUM, natNum);
            values.put(PokemonProvider.COL_NAME, nameStr);
            values.put(PokemonProvider.COL_SPECIES, speciesStr);
            values.put(PokemonProvider.COL_GENDER, genderId == R.id.rbMale ? "Male" : "Female");
            values.put(PokemonProvider.COL_HEIGHT, heightStr);
            values.put(PokemonProvider.COL_WEIGHT, weightStr);
            values.put(PokemonProvider.COL_LEVEL, level.getSelectedItem().toString());
            values.put(PokemonProvider.COL_HP, hpStr);
            values.put(PokemonProvider.COL_ATTACK, attackStr);
            values.put(PokemonProvider.COL_DEFENSE, defenseStr);
            getContentResolver().insert(PokemonProvider.CONTENT_URI, values);

            Toast.makeText(this, "Pokémon successfully stored in the database.", Toast.LENGTH_SHORT).show();
        }
    }

}
