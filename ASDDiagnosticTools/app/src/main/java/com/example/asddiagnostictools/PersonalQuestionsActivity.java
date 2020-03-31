package com.example.asddiagnostictools;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class PersonalQuestionsActivity extends AppCompatActivity {

    private AQ10QuestionPack _AQ10QuestionPack = AsdAppSettings.getAQ10QuestionPack();

    private EditText _ageEditor = null;
    private RadioGroup _genderEditor = null;
    private Spinner _ethnicityEditor = null;
    private RadioGroup _bornWithJaundiceEditor = null;
    private RadioGroup _familyDiagnosedWithAutismEditor = null;
    private Spinner _personCompletingTestEditor = null;
    private Spinner _countryOfResidenceEditor = null;
    private RadioGroup _takenTestBeforeEditor = null;
    private Spinner _languageEditor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_questions);

        _ageEditor = findViewById(R.id.editTextAge);
        _genderEditor = findViewById(R.id.radioGroupGender);
        _ethnicityEditor = findViewById(R.id.spinnerEthnicity);
        _bornWithJaundiceEditor = findViewById(R.id.radioGroupJaundice);
        _familyDiagnosedWithAutismEditor = findViewById(R.id.radioGroupAutismInImmediateFamily);
        _personCompletingTestEditor = findViewById(R.id.spinnerWhoIsCompletingTest);
        _countryOfResidenceEditor = findViewById(R.id.spinnerCountryOfResidence);
        _takenTestBeforeEditor = findViewById(R.id.radioGroupHasUsedAppBefore);
        _languageEditor = findViewById(R.id.spinnerLanguage);

        // Setup the dropdown containing ethnicities
        ArrayAdapter<CharSequence> ethnicitiesAdapter = ArrayAdapter.createFromResource(this, R.array.ethnicities, android.R.layout.simple_spinner_item);
        ethnicitiesAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        _ethnicityEditor.setAdapter(ethnicitiesAdapter);

        // Setup the dropdown containing options
        ArrayAdapter<CharSequence> peopleCompletingSurveysAdapter = ArrayAdapter.createFromResource(this, R.array.peopleCompletingSurvey, android.R.layout.simple_spinner_item);
        peopleCompletingSurveysAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        _personCompletingTestEditor.setAdapter(peopleCompletingSurveysAdapter);

        // Setup the dropdown containing ethnicities
        ArrayAdapter<CharSequence> countriesAdapter = ArrayAdapter.createFromResource(this, R.array.countries, android.R.layout.simple_spinner_item);
        countriesAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        _countryOfResidenceEditor.setAdapter(countriesAdapter);

        // Setup the dropdown containing languages
        ArrayAdapter<CharSequence> languagesAdapter = ArrayAdapter.createFromResource(this, R.array.language, android.R.layout.simple_spinner_item);
        countriesAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        _languageEditor.setAdapter(languagesAdapter);

        // This is to prefill the form with values if the user went back in the process so they
        // don't have to enter this information back into the form
        fillFormWithDataFromQuestionPack();
    }

    private void fillFormWithDataFromQuestionPack()
    {
        // Should not be possible but just in case...
        if (_AQ10QuestionPack != null)
        {
            // Age
            if (_AQ10QuestionPack.get_age() > 0)
                _ageEditor.setText(Integer.toString(_AQ10QuestionPack.get_age()));
            else
                _ageEditor.setText("");

            // Gender; 0 = male, 1 = female
            if (_AQ10QuestionPack.get_gender() == 0) {
                _genderEditor.check(R.id.radioButtonMale);
            }
            else if (_AQ10QuestionPack.get_gender() == 1) {
                _genderEditor.check(R.id.radioButtonFemale);
            }
            else {
            }

            // Ethnicity
            if (_AQ10QuestionPack.get_ethnicity() >= 0)
                _ethnicityEditor.setSelection(_AQ10QuestionPack.get_ethnicity());

            // Jaundice
            if (_AQ10QuestionPack.get_bornWithJaundice())
                _bornWithJaundiceEditor.check(R.id.radioButtonJaundiceYes);
            else
                _bornWithJaundiceEditor.check(R.id.radioButtonJaundiceNo);

            // Immediate Family
            if (_AQ10QuestionPack.get_immediateFamilyMemberDiagnosedWithAsd())
                _familyDiagnosedWithAutismEditor.check(R.id.radioButtonFamilyYes);
            else
                _familyDiagnosedWithAutismEditor.check(R.id.radioButtonFamilyNo);

            // Who completed the test
            if (_AQ10QuestionPack.get_personCompletingTest() >= 0)
                _personCompletingTestEditor.setSelection(_AQ10QuestionPack.get_personCompletingTest());

            // Country of residence
            if (_AQ10QuestionPack.get_countryOfResidence() >= 0)
                _countryOfResidenceEditor.setSelection(_AQ10QuestionPack.get_countryOfResidence());

            // Test taken before
            if (_AQ10QuestionPack.get_hasUsedAppBefore())
                _takenTestBeforeEditor.check(R.id.radioButtonUsedAppYes);
            else
                _takenTestBeforeEditor.check(R.id.radioButtonUsedAppNo);

            // Language
            if (_AQ10QuestionPack.get_langauge() >= 0)
                _languageEditor.setSelection(_AQ10QuestionPack.get_langauge());

        }
    }

    public void onBackClicked(View view)
    {
        moveToMainActivity();
    }

    public void onNextClicked(View view)
    {
        updateQuestionPack();

        if (validateInputs()) {
            moveToAq10Questions();
        }
        else {
            // TODO: Implement an alert so the user knows to go back and fill out missing information
        }
    }

    private boolean validateInputs()
    {
        boolean isValid = true;

        if (_AQ10QuestionPack.get_age() < 1)
            isValid = false;

        if (_AQ10QuestionPack.get_ethnicity() < 0)
            isValid = false;

        if (_AQ10QuestionPack.get_countryOfResidence() < 0)
            isValid = false;

        if (_AQ10QuestionPack.get_personCompletingTest() < 0)
            isValid =  false;

        if (_AQ10QuestionPack.get_langauge() < 0)
            isValid = false;

        return isValid;
    }

    // Pulls inputs from this activity into the AQ10QuestionPack
    private void updateQuestionPack()
    {
        // Age
        if (!_ageEditor.getText().toString().isEmpty())
        {
            try {
                int age = Integer.parseInt(_ageEditor.getText().toString());
                _AQ10QuestionPack.set_age(age);
            }
            catch (Exception ex) {
                // Invalid input most likely

                _AQ10QuestionPack.set_age(-1);
                // TODO: Warn the user and let them fix this
            }
        }
        else
        {
            _AQ10QuestionPack.set_age(-1);
        }

        // Gender;  0 = male, 1 = female
        switch  (_genderEditor.getCheckedRadioButtonId())
        {
            case R.id.radioButtonMale:
                _AQ10QuestionPack.set_gender(0);
                break;

            case R.id.radioButtonFemale:
                _AQ10QuestionPack.set_gender(1);
                break;

            default:
                _AQ10QuestionPack.set_gender(0);
                break;
        }

        // Ethnicity
        if (_ethnicityEditor.getSelectedItem() != null) {
            _AQ10QuestionPack.set_ethnicity(_ethnicityEditor.getSelectedItemPosition());
        }
        else {
            _AQ10QuestionPack.set_ethnicity(-1);
        }

        // Jaundice
        switch (_bornWithJaundiceEditor.getCheckedRadioButtonId())
        {
            case R.id.radioButtonJaundiceYes:
                _AQ10QuestionPack.set_bornWithJaundice(true);
                break;

            case R.id.radioButtonJaundiceNo:
                _AQ10QuestionPack.set_bornWithJaundice(false);
                break;

            default:
                // Should never occur since an option is always selected due to the default
                _AQ10QuestionPack.set_bornWithJaundice(true);
                break;
        }


        // Who completed the test
        if (_personCompletingTestEditor.getSelectedItem() != null) {
            _AQ10QuestionPack.set_personCompletingTest(_personCompletingTestEditor.getSelectedItemPosition());
        }
        else {
            _AQ10QuestionPack.set_personCompletingTest(-1);
        }

        // Country of Residence
        if (_countryOfResidenceEditor.getSelectedItem() != null) {
            _AQ10QuestionPack.set_countryOfResidence(_countryOfResidenceEditor.getSelectedItemPosition());
        }
        else {
            _AQ10QuestionPack.set_countryOfResidence(-1);
        }

        // Taken test before
        switch (_takenTestBeforeEditor.getCheckedRadioButtonId())
        {
            case R.id.radioButtonUsedAppYes:
                _AQ10QuestionPack.set_hasUsedAppBefore(true);
                break;

            case R.id.radioButtonUsedAppNo:
                _AQ10QuestionPack.set_hasUsedAppBefore(false);
                break;

            default:
                // Should never occur since an option is always selected due to the default
                _AQ10QuestionPack.set_hasUsedAppBefore(false);
                break;
        }

        // Language
        if (_languageEditor.getSelectedItem() != null) {
            _AQ10QuestionPack.set_language(_languageEditor.getSelectedItemPosition());
        }
        else {
            _AQ10QuestionPack.set_language(-1);
        }
    }

    private void moveToMainActivity()
    {
        Intent getMainActivityIntent = new Intent(this,
                MainActivity.class);

        getMainActivityIntent.putExtra("callingActivity", "PersonalQuestionsActivity");
        getMainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION );

        startActivity(getMainActivityIntent);
        this.overridePendingTransition(0, 0);
    }

    private void moveToAq10Questions()
    {
        Intent getAq10QuestionsActivityIntent = new Intent(this,
                Aq10QuestionsActivity.class);

        getAq10QuestionsActivityIntent.putExtra("callingActivity", "PersonalQuestionsActivity");
        getAq10QuestionsActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION );

        startActivity(getAq10QuestionsActivityIntent);
        this.overridePendingTransition(0, 0);
    }
}
