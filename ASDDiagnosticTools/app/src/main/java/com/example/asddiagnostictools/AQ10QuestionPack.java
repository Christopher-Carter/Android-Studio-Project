package com.example.asddiagnostictools;

import java.util.*;
import java.io.*;
import android.content.*;

/*
    Contains details about the AQ10 questions loaded from assets defined in the constructor

    Note that the fields for ethnicity, person completing test, and country of residence are all
    represented in integer format.  Not only does this make it easy to retain this information
    for filling the form back in, but the order should be mapped to the neural network for
    these enumerations so that it lines up when we process it through the tensorflow model.

    E.G., if Asian is the second option in the dropdown it is in position 1, which means in the
          training data used in the tensorflow model Asian needs to be replaced with a 1 so it
          will match when we run a prediction against the model.
 */
public class AQ10QuestionPack {
    private static String _csvDelimiter = ",";
    private Context _context = null;
    private List<AQ10Question> _questions = null;

    // This determines which asset to load from the assets within this application
    private String _assetName = null;

    // Personal information
    private int _age = -1;

    // 0 = male, 1 = female
    private int _gender = -1;
    private int _ethnicity = -1;
    private boolean _bornWithJaundice = false;
    private boolean _immediateFamilyMemberDiagnosedWithAsd = false;
    private int _personCompletingTest = -1;
    private int _countryOfResidence = -1;
    private boolean _hasUsedAppBefore = false;
    private int _language = -1;

    // Import the AQ10 question pack with a matching asset name
    public AQ10QuestionPack(Context context, String assetName) throws IOException {
        _assetName = assetName;
        _context = context;

        LoadAssetData();
    }

    public List<AQ10Question> getQuestions()
    {
        return _questions;
    }

    // Attempt to load the CSV file for the asset name of this object and create
    // the AQ10 survey from it
    private void LoadAssetData() throws IOException {
        String fileName = _assetName + ".csv";

        // Get the asset file
        InputStreamReader inputStreamReader = new InputStreamReader(_context.getAssets().open(fileName));

        // Turn it into something usable and discard the header row
        BufferedReader reader = new BufferedReader(inputStreamReader);
        reader.readLine();

        String line;
        _questions = new ArrayList<AQ10Question>();
        StringTokenizer st = null;

        // Get content from the CSV file
        while ((line = reader.readLine()) != null) {

            // Use the tokenizer to read in comma delimited values from a line
            st = new StringTokenizer(line, _csvDelimiter);


            // Get values from the string tokenizer
            int questionNo = Integer.parseInt(st.nextToken());
            String questionText = st.nextToken().replace("\"", "");
            int _answerWorthPoints1 = Integer.parseInt(st.nextToken());
            int _answerWorthPoints2 = Integer.parseInt(st.nextToken());

            AQ10Question question = new AQ10Question(questionNo,
                    questionText, _answerWorthPoints1, _answerWorthPoints2);
            _questions.add(question);
        }

        reader.close();
        inputStreamReader.close();
    }

    public void ResetAq10Answers()
    {
        for (AQ10Question question : _questions )
            question.setAnswerProvidedByUser(-1);
    }

    public int get_age() {
        return _age;
    }

    public void set_age(int age) {
        this._age = age;
    }

    public int get_gender() {
        return _gender;
    }

    public void set_gender(int gender) {
        this._gender = gender;
    }

    public int get_ethnicity() {
        return _ethnicity;
    }

    public void set_ethnicity(int ethnicity) {
        this._ethnicity = ethnicity;
    }

    public int get_langauge() { return _language; }

    public void set_language(int language) {this._language = language; }

    public boolean get_bornWithJaundice() {
        return _bornWithJaundice;
    }

    public void set_bornWithJaundice(boolean bornWithJaundice) {
        this._bornWithJaundice = bornWithJaundice;
    }

    public boolean get_immediateFamilyMemberDiagnosedWithAsd() {
        return _immediateFamilyMemberDiagnosedWithAsd;
    }

    public void set_immediateFamilyMemberDiagnosedWithAsd(boolean immediateFamilyMemberDiagnosedWithAsd) {
        this._immediateFamilyMemberDiagnosedWithAsd = immediateFamilyMemberDiagnosedWithAsd;
    }

    public int get_personCompletingTest() {
        return _personCompletingTest;
    }

    public void set_personCompletingTest(int personCompletingTest) {
        this._personCompletingTest = personCompletingTest;
    }

    public int get_countryOfResidence() {
        return _countryOfResidence;
    }

    public void set_countryOfResidence(int countryOfResidence) {
        this._countryOfResidence = countryOfResidence;
    }

    public boolean get_hasUsedAppBefore() {
        return _hasUsedAppBefore;
    }

    public void set_hasUsedAppBefore(boolean hasUsedAppBefore) {
        this._hasUsedAppBefore = hasUsedAppBefore;
    }

}
