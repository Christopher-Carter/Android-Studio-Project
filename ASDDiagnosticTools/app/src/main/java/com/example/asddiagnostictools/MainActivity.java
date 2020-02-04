package com.example.asddiagnostictools;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onAq10ChildSurveyStartClick(View view) {

        // Load the AQ10 child survey
        AsdAppSettings.LoadAq10QuestionPack(this, "aq10childQuestions");

        // Catch the case where the file did not load for some reason and show a dialog rather
        // than a catastrophic error
        // TODO: write safety around this case...


        // Change to the personal questions activity
        Intent getPersonalQuestionsIntent = new Intent(this,
                PersonalQuestionsActivity.class);

        getPersonalQuestionsIntent.putExtra("callingActivity", "MainActivity");
        getPersonalQuestionsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION );

        startActivity(getPersonalQuestionsIntent);
        this.overridePendingTransition(0, 0);
    }

}
