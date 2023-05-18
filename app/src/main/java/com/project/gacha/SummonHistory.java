package com.project.gacha;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;


import java.util.ArrayList;
import java.util.Collections;

public class SummonHistory extends AppCompatActivity implements View.OnClickListener {
    RecyclerView historyRecyclerView;
    TextView back;
    ImageView back_arrow;
    SummonListAdapter summonListAdapter;
    private static ArrayList<Inventory> inv;
    private ArrayList<String> frequencies;
    private GachaDAO gachaDAO;

    int startRecord, endRecord;
    EditText startEdt, endEdt;
    Button findBtn;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_summon_history);

        back_arrow = findViewById(R.id.back_arrow);
        back_arrow.setOnClickListener(this);
        back = findViewById(R.id.back_view);
        back.setOnClickListener(this);

        gachaDAO = new GachaDAO(getApplicationContext());

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        layoutManager.setAlignItems(AlignItems.STRETCH);

        historyRecyclerView = findViewById(R.id.history_list);
        historyRecyclerView.setLayoutManager(layoutManager);

        startEdt = findViewById(R.id.earliest_record);
        endEdt = findViewById(R.id.latest_record);
        findBtn = findViewById(R.id.findbtn);


    }


    public void onClick(View view) {
        if (view == findBtn) {
            if (TextUtils.isEmpty(startEdt.getText().toString().trim()) || TextUtils.isEmpty((endEdt.getText().toString().trim())))
                Toast.makeText(getApplicationContext(), "Cannot be empty", Toast.LENGTH_SHORT).show();
            else {
                startRecord = Integer.parseInt(startEdt.getText().toString().trim());
                endRecord = Integer.parseInt(endEdt.getText().toString().trim());
                if (startRecord < 0 || endRecord < 0) {
                    Toast.makeText(getApplicationContext(), "Illegal input", Toast.LENGTH_SHORT).show();
                } else if (startRecord > endRecord) {
                    Toast.makeText(getApplicationContext(), "Latest record must larger", Toast.LENGTH_SHORT).show();
                } else {
                    inv = gachaDAO.searchByRecord(startRecord, endRecord);
                    summonListAdapter = new SummonListAdapter(inv);
                    historyRecyclerView.setAdapter(summonListAdapter);
                    summonListAdapter.setList(inv);
                }
            }
        } else if (view == back_arrow || view == back) {
            Intent i = new Intent(SummonHistory.this, Gacha.class);
            startActivity(i);
        }
    }


}
