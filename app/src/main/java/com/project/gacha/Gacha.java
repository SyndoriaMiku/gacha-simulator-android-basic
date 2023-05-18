package com.project.gacha;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class Gacha extends AppCompatActivity implements View.OnClickListener,BudgetDialog.BudgetDialogListener {
    ImageView single_summon, multi_summon, home_button, bannerImage, right_arrow, left_arrow;
    TextView summonHistoryButton, wishCount, resetButton;
    ConstraintLayout constraintLayout;
    ConstraintSet constraintSet1 = new ConstraintSet();
    Transition transition = new ChangeBounds();
    Toast wishWarning;
    Boolean state = true, homeMenu = true, isAnimationRunning = false;
    private static Boolean budgetEnabled = false;
    View backDrop;
    static ArrayList<Card> cardsPulled = new ArrayList<>();
    static ImageView[] unitSlots, raritySlots;
    static int bannerChoice = 0, wishUsed = 0;
    Banner[] banner;
    private GachaDAO gachaDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        constraintSet1.clone(this, R.layout.activity_summon);
        setContentView(R.layout.activity_summon);

        Banner adrift_in_the_harbour = new Banner(R.drawable.gi_adrift_in_the_harbor, Banner.findCardId(new ArrayList<>(Collections.singletonList(11))),
                Banner.findCardId(new ArrayList<>(Arrays.asList(19, 27, 25))), Banner.findCardId(new ArrayList<>(Arrays.asList(14, 17, 20, 8, 12))),
                Banner.findCardId(new ArrayList<>(Arrays.asList(28, 22, 9, 7, 6, 10, 18, 5, 21, 4, 105, 106, 110, 66, 80, 99, 117, 44, 79, 89, 92, 125, 49, 51, 39, 122, 56, 32))),
                Banner.findCardId(new ArrayList<>(Arrays.asList(109, 107, 102, 78, 93, 83, 61, 43, 42, 45, 33, 120, 115, 33))), false);
        banner = new Banner[]{adrift_in_the_harbour};

        bannerChoice = 0;
        gachaDAO = new GachaDAO(this);

        wishWarning = Toast.makeText(this, "Insuffficient Primogem. Please get more Primogem", Toast.LENGTH_SHORT);

        multi_summon = findViewById(R.id.multi_summon);
        multi_summon.setOnClickListener(this);

        single_summon = findViewById(R.id.single_summon);
        single_summon.setOnClickListener(this);

        backDrop = findViewById(R.id.slider_backdrop);

        bannerImage = findViewById(R.id.gi_banner_image);
        bannerImage.setImageResource(banner[bannerChoice].getImage());


        wishCount = findViewById(R.id.roll_used);
        wishCount.setText(Integer.toString(wishUsed));
        wishCount.setOnClickListener(this);

        right_arrow = findViewById(R.id.right_arrow);
        right_arrow.setOnClickListener(this);

        left_arrow = findViewById(R.id.left_arrow);
        left_arrow.setOnClickListener(this);

        resetButton = findViewById(R.id.gi_reset);
        resetButton.setOnClickListener(this);

        summonHistoryButton = findViewById(R.id.gi_history);
        summonHistoryButton.setOnClickListener(this);

        unitSlots = new ImageView[]{findViewById(R.id.gi_slot1), findViewById(R.id.gi_slot2), findViewById(R.id.gi_slot3), findViewById(R.id.gi_slot4), findViewById(R.id.gi_slot5),
                findViewById(R.id.gi_slot6), findViewById(R.id.gi_slot7), findViewById(R.id.gi_slot8), findViewById(R.id.gi_slot9), findViewById(R.id.gi_slot10)};

        raritySlots = new ImageView[]{findViewById(R.id.gi_rarity1), findViewById(R.id.gi_rarity2), findViewById(R.id.gi_rarity3), findViewById(R.id.gi_rarity4),
                findViewById(R.id.gi_rarity5), findViewById(R.id.gi_rarity6), findViewById(R.id.gi_rarity7), findViewById(R.id.gi_rarity8), findViewById(R.id.gi_rarity9),
                findViewById(R.id.gi_rarity10)};

        constraintLayout = findViewById(R.id.gi_summon_root);

        transition.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        transition.setDuration(1000);

    }

    @Override
    public void onClick(View view) {
        if (view == right_arrow) {
            bannerChoice++;
            if (bannerChoice >= banner.length)
                bannerChoice = 0;
            bannerImage.setImageResource(banner[bannerChoice].bannerImage);
        } else if (view == left_arrow) {
            bannerChoice--;
            if (bannerChoice < 0)
                bannerChoice = banner.length - 1;
            bannerImage.setImageResource(banner[bannerChoice].bannerImage);
        } else if (view == multi_summon) {
            Card[] results = banner[bannerChoice].multiRoll();
            for (int i = 0; i < 10; i++) {
                cardsPulled.add(results[i]);
                gachaDAO.addCard(results[i]);
                unitSlots[i].setImageResource(results[i].getImage());
                if (banner[bannerChoice].fiveStarBanner.contains(results[i]) || banner[bannerChoice].fiveStarPool.contains(results[i])) {
                    unitSlots[i].setForeground(getDrawable(R.drawable.red_border));
                    raritySlots[i].setImageResource(Banner.Five_star);
                } else if (banner[bannerChoice].fourStarBanner.contains(results[i]) || banner[bannerChoice].fourStarPool.contains(results[i])) {
                    unitSlots[i].setForeground(getDrawable(R.drawable.blank));
                    raritySlots[i].setImageResource(Banner.Four_star);
                } else {
                    unitSlots[i].setForeground(getDrawable(R.drawable.blank));
                    raritySlots[i].setImageResource(Banner.Three_star);
                }
            }
        } else if (view == single_summon) {
            Card[] results = banner[bannerChoice].singleRoll();
            cardsPulled.add(results[0]);
            gachaDAO.addCard(results[0]);
            unitSlots[0].setImageResource(results[0].getImage());
            if (banner[bannerChoice].fiveStarBanner.contains(results[0]) || banner[bannerChoice].fiveStarPool.contains(results[0])) {
                unitSlots[0].setForeground(getDrawable(R.drawable.red_border));
                raritySlots[0].setImageResource(Banner.Five_star);
            } else if (banner[bannerChoice].fourStarBanner.contains(results[0]) || banner[bannerChoice].fourStarPool.contains(results[0])) {
                unitSlots[0].setForeground(getDrawable(R.drawable.blank));
                raritySlots[0].setImageResource(Banner.Four_star);
            } else {
                unitSlots[0].setForeground(getDrawable(R.drawable.blank));
                raritySlots[0].setImageResource(Banner.Three_star);
            }


        } else if (view == resetButton) {
            budgetEnabled = false;
            cardsPulled.clear();
            for (ImageView views : unitSlots) {
                views.setForeground(getDrawable(R.drawable.blank));
                views.setImageResource(android.R.color.transparent);
            }
            wishUsed = 0;
            wishCount.setText(Integer.toString(wishUsed));
        } else if (view == summonHistoryButton) {
            Intent i = new Intent(Gacha.this, SummonHistory.class);
            startActivity(i);
            state = false;
        } else if (view == wishCount && !homeMenu) {
            openDialog();
        }
    }

    private void openDialog() {
        BudgetDialog budgetDialog = new BudgetDialog();
        budgetDialog.show(getSupportFragmentManager(), "Set Budget Dialog");
    }

    public void getBudget(int budget) {
        cardsPulled.clear();
        for (ImageView views : unitSlots) {
            views.setForeground(getDrawable(R.drawable.blank));
            views.setImageResource(android.R.color.transparent);
        }
        wishUsed = 0;
        wishCount.setText(Integer.toString(wishUsed));
        budgetEnabled = true;
        if (budget > 999999) {
            wishUsed = 999999;
            wishCount.setText(String.valueOf(999999));
        } else {
            wishUsed = budget;
            wishCount.setText(String.valueOf(budget));
        }
    }


}
