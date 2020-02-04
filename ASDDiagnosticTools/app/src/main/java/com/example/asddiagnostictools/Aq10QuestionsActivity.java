package com.example.asddiagnostictools;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

public class Aq10QuestionsActivity extends AppCompatActivity {

    private ToggleButton _definatelyAgreeButton = null;
    private ToggleButton _slightlyAgreeButton = null;
    private ToggleButton _slightlyDisagreeButton = null;
    private ToggleButton _definatelyDisagreeButton = null;
    private TextView _questionTextView = null;

    private AQ10Question _currentQuestion = null;
    private int _currentQuestionNo = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aq10_questions);

        _questionTextView = findViewById(R.id.textViewQuestionText);
        _definatelyAgreeButton = findViewById(R.id.toggleButtonDefinitelyAgree);
        _slightlyAgreeButton = findViewById(R.id.toggleButtonSlightlyAgree);
        _slightlyDisagreeButton = findViewById(R.id.toggleButtonSlightlyDisagree);
        _definatelyDisagreeButton = findViewById(R.id.toggleButtonDefinitelyDisagree);

        _currentQuestionNo = 1;
        changeToQuestion(_currentQuestionNo);

        // Update the toggle buttons in case the user went back previously so we can
        // check the previous answer selected by the user
        setOptionChecked(_currentQuestion.getAnswerProvidedByUser());
    }

    public void onBackClicked(View view)
    {
        if (_currentQuestionNo == 1)
        {
            moveToPersonalQuestions();
        }
        else
        {
            changeToQuestion(--_currentQuestionNo);

            // Update the toggle buttons in case the user is going back/forward in
            // previously selected answers
            setOptionChecked(_currentQuestion.getAnswerProvidedByUser());
        }
    }

    public void onNextClicked(View view)
    {
        if (_currentQuestionNo == 10)
        {
            moveToShowResults();
        }
        else
        {
            // Ensure that the user selected an option before proceeding
            if (validSelection()) {
                changeToQuestion(++_currentQuestionNo);

                // Update the toggle buttons in case the user is going back/forward in
                // previously selected answers
                setOptionChecked(_currentQuestion.getAnswerProvidedByUser());
            }
        }
    }

    public void onOptionClicked(View view)
    {
        int selectedValue = 0;

        switch (view.getId())
        {
            case  R.id.toggleButtonDefinitelyDisagree:
                selectedValue = 1;
                break;

            case  R.id.toggleButtonSlightlyDisagree:
                selectedValue = 2;
                break;

            case  R.id.toggleButtonSlightlyAgree:
                selectedValue = 3;
                break;

            case R.id.toggleButtonDefinitelyAgree:
                selectedValue = 4;
                break;

        }

        setOptionChecked(selectedValue);
    }

    // Set the current AQ10Question and update the UI
    private void changeToQuestion(int questionNo)
    {
        // Store the current question details and update the UI
        _currentQuestionNo = questionNo;
        _currentQuestion = AsdAppSettings.getAQ10QuestionPack().getQuestions().get(questionNo - 1);
        _questionTextView.setText(_currentQuestion.getQuestionText());

        // Reset toggle button settings for the next question
        _definatelyDisagreeButton.setChecked(false);
        _definatelyAgreeButton.setChecked(false);
        _slightlyAgreeButton.setChecked(false);
        _slightlyDisagreeButton.setChecked(false);
    }

    private void setOptionChecked(int optionChecked)
    {
        _definatelyDisagreeButton.setChecked(optionChecked == 1);
        _slightlyDisagreeButton.setChecked(optionChecked == 2);
        _slightlyAgreeButton.setChecked(optionChecked == 3);
        _definatelyAgreeButton.setChecked(optionChecked == 4);

        // Update the current question to include the user answer
        _currentQuestion.setAnswerProvidedByUser(optionChecked);
    }

    private void moveToPersonalQuestions()
    {
        Intent getPersonalQuestionsIntent = new Intent(this,
                PersonalQuestionsActivity.class);

        getPersonalQuestionsIntent.putExtra("callingActivity", "Aq10QuestionsActivity");
        getPersonalQuestionsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION );

        startActivity(getPersonalQuestionsIntent);
        this.overridePendingTransition(0, 0);
    }

    private void moveToShowResults()
    {
        Intent getShowResultsIntent = new Intent(this,
                ShowResultsActivity.class);

        getShowResultsIntent.putExtra("callingActivity", "Aq10QuestionsActivity");
        getShowResultsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION );

        startActivity(getShowResultsIntent);
        this.overridePendingTransition(0, 0);
    }

    private boolean validSelection()
    {
        boolean isValid = false;

        if (_currentQuestion.getAnswerProvidedByUser() >= 1)
            isValid = true;

        return isValid;
    }

}
