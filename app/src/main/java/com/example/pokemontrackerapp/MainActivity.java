package com.example.pokemontrackerapp;

import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
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

public class MainActivity extends AppCompatActivity {
    private TextView tvNatNumber, tvName, tvSpecies, tvGender, tvHeight, tvWeight, tvLevel, tvHP, tvAttack, tvDefense;
    private EditText nationalNumber, name, species, height, weight, hp, attack, defense;
    private Spinner level;
    private RadioGroup genderGroup;

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.table);
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
        String natNumStr = nationalNumber.getText().toString().trim();
        int natNum = Integer.parseInt(natNumStr);
        if (natNumStr.isEmpty() || natNum < 1 || natNum > 1010) {
            tvNatNumber.setTextColor(Color.RED);
            Toast.makeText(this, "National Number must be between 1 and 1010", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            tvNatNumber.setTextColor(Color.BLACK);
        }
        String nameStr = name.getText().toString().trim();
        if (nameStr.isEmpty() || nameStr.length() < 3 || nameStr.length() > 12) {
            tvName.setTextColor(Color.RED);
            Toast.makeText(this, "Name is required and must be between 3 and 12 characters", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            tvName.setTextColor(Color.BLACK);
        }
        if (genderGroup.getCheckedRadioButtonId() == -1) {
            // Nothing selected
            tvGender.setTextColor(Color.RED);
            Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            tvGender.setTextColor(Color.BLACK);
        }
        int hpNum = Integer.parseInt(hp.getText().toString().trim());
        if (hpNum < 1 || hpNum > 362) {
            tvHP.setTextColor(Color.RED);
            Toast.makeText(this, "HP must be between 1 and 362", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            tvHP.setTextColor(Color.BLACK);
        }
        int attackNum = Integer.parseInt(attack.getText().toString().trim());
        if (attackNum < 0 || attackNum > 526) {
            tvAttack.setTextColor(Color.RED);
            Toast.makeText(this, "Attack must be between 0 and 526", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            tvAttack.setTextColor(Color.BLACK);
        }
        int defenseNum = Integer.parseInt(defense.getText().toString().trim());
        if (defenseNum < 10 || defenseNum > 614) {
            tvDefense.setTextColor(Color.RED);
            Toast.makeText(this, "Defense must be between 10 and 614", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            tvDefense.setTextColor(Color.BLACK);
        }
        double heightNum = Double.parseDouble(height.getText().toString().trim());
        if (heightNum < 0.2 || heightNum > 169.99) {
            tvHeight.setTextColor(Color.RED);
            Toast.makeText(this, "Height must be between 0.2 and 169.99", Toast.LENGTH_SHORT).show();
            return;
        } else {
            tvHeight.setTextColor(Color.BLACK);
        }
        double weightNum = Double.parseDouble(weight.getText().toString().trim());
        if (weightNum < 0.1 || weightNum > 992.97) {
            tvWeight.setTextColor(Color.RED);
            Toast.makeText(this, "Weight must be between 0.1 and 992.97", Toast.LENGTH_SHORT).show();
        }
        else {
            tvWeight.setTextColor(Color.BLACK);
            Toast.makeText(this, "Pokémon successfully stored in the database.", Toast.LENGTH_SHORT).show();
        }
    }
}
