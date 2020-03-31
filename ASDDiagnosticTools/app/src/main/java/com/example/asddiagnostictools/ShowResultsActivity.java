package com.example.asddiagnostictools;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class ShowResultsActivity extends AppCompatActivity {

    // Order of columns in data model
    // A1	A2	A3	A4	A5	A6	A7	A8	A9	A10	Age	Sex	Ethnicity	Jaundice	Family_ASD	Residence	Used_App_Before	Language	User	Class

    AQ10QuestionPack _questionPack = null;
    Interpreter tensorflowInterpreter;
    TextView textPrediction;
    Button predictionButton = null;
    Button questionButton = null;
    EditText aq1, aq2, aq3, aq4, aq5, aq6, aq7, aq8, aq9, aq10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_results);

        _questionPack = AsdAppSettings.getAQ10QuestionPack();

        textPrediction = (TextView)findViewById(R.id.textViewPrediction);

        try
        {
            tensorflowInterpreter = new Interpreter(loadModelFile());

            //tensorflowInterpreter

            doPrediction();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void onFinishClicked(View view)
    {
        moveToMainActivity();
    }

    public void onTestPredictionClicked(View view)
    {
        // Case, has ASD
        // 0, 1, 1, 1, 1, 1, 1, 0, 0, 1, 4, 0, 5, 1, 0, 43, 0, 0, 2

        float prediction = doInference(0, 1, 1, 1, 1, 1, 1, 0, 0, 1, 4, 0, 5, 1, 0, 43, 0, 0, 2);

        textPrediction.setText(String.format("%.4f", prediction));
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

    public void doPrediction()
    {
        float prediction = doInference(
                _questionPack.getQuestions().get(0).getValueOfAnswer(),
                _questionPack.getQuestions().get(1).getValueOfAnswer(),
                _questionPack.getQuestions().get(2).getValueOfAnswer(),
                _questionPack.getQuestions().get(3).getValueOfAnswer(),
                _questionPack.getQuestions().get(4).getValueOfAnswer(),
                _questionPack.getQuestions().get(5).getValueOfAnswer(),
                _questionPack.getQuestions().get(6).getValueOfAnswer(),
                _questionPack.getQuestions().get(7).getValueOfAnswer(),
                _questionPack.getQuestions().get(8).getValueOfAnswer(),
                _questionPack.getQuestions().get(9).getValueOfAnswer(),
                _questionPack.get_age(),
                _questionPack.get_gender(),
                _questionPack.get_ethnicity(),
                _questionPack.get_bornWithJaundice() ? 1 : 0,
                _questionPack.get_immediateFamilyMemberDiagnosedWithAsd() ? 1 : 0,
                _questionPack.get_countryOfResidence(),
                _questionPack.get_hasUsedAppBefore() ? 1 : 0,
                _questionPack.get_langauge(),
                _questionPack.get_personCompletingTest()
        );

        updatePredictionTextView(prediction);
    }

    private void updatePredictionTextView(float prediction)
    {
        String predictionText = "";

        if (prediction == 1)
        {
            predictionText = "Based on the answers provided you may have Autism.";
        }
        else
        {
            predictionText = "Based on the answers provided you may NOT have Autism.";
        }

        predictionText += "\nPlease consult a doctor for a formal diagnosis.";
        predictionText += "\n";
        predictionText += "\n=== Diagnostics ===";
        predictionText += "\nAQ1: " + _questionPack.getQuestions().get(0).getValueOfAnswer() + ", selected answer: " + _questionPack.getQuestions().get(0).getAnswerProvidedByUser();
        predictionText += "\nAQ2: " + _questionPack.getQuestions().get(1).getValueOfAnswer() + ", selected answer: " + _questionPack.getQuestions().get(1).getAnswerProvidedByUser();
        predictionText += "\nAQ3: " + _questionPack.getQuestions().get(2).getValueOfAnswer() + ", selected answer: " + _questionPack.getQuestions().get(2).getAnswerProvidedByUser();
        predictionText += "\nAQ4: " + _questionPack.getQuestions().get(3).getValueOfAnswer() + ", selected answer: " + _questionPack.getQuestions().get(3).getAnswerProvidedByUser();
        predictionText += "\nAQ5: " + _questionPack.getQuestions().get(4).getValueOfAnswer() + ", selected answer: " + _questionPack.getQuestions().get(4).getAnswerProvidedByUser();
        predictionText += "\nAQ6: " + _questionPack.getQuestions().get(5).getValueOfAnswer() + ", selected answer: " + _questionPack.getQuestions().get(5).getAnswerProvidedByUser();
        predictionText += "\nAQ7: " + _questionPack.getQuestions().get(6).getValueOfAnswer() + ", selected answer: " + _questionPack.getQuestions().get(6).getAnswerProvidedByUser();
        predictionText += "\nAQ8: " + _questionPack.getQuestions().get(7).getValueOfAnswer() + ", selected answer: " + _questionPack.getQuestions().get(7).getAnswerProvidedByUser();
        predictionText += "\nAQ9: " + _questionPack.getQuestions().get(8).getValueOfAnswer() + ", selected answer: " + _questionPack.getQuestions().get(8).getAnswerProvidedByUser();
        predictionText += "\nAQ10: " + _questionPack.getQuestions().get(9).getValueOfAnswer() + ", selected answer: " + _questionPack.getQuestions().get(9).getAnswerProvidedByUser();
        predictionText += "\nAge: " + _questionPack.get_age();
        predictionText += "\nGender: " + _questionPack.get_gender();
        predictionText += "\nEthnicity: " + _questionPack.get_ethnicity();
        predictionText += "\nBorn with Jaundice: " + _questionPack.get_bornWithJaundice();
        predictionText += "\nFamily Member w/ASD: " + _questionPack.get_immediateFamilyMemberDiagnosedWithAsd();
        predictionText += "\nCountry of Residence: " + _questionPack.get_countryOfResidence();
        predictionText += "\nUsed App Before: " +  _questionPack.get_hasUsedAppBefore();
        predictionText += "\nGet Language: " + _questionPack.get_langauge();
        predictionText += "\nPerson Completing Test: " + _questionPack.get_personCompletingTest();

        textPrediction.setText(predictionText);
    }

    public float doInference(int aq1, int aq2, int aq3, int aq4, int aq5,
                             int aq6, int aq7, int aq8, int aq9, int aq10,
                             int age, int sex, int ethnicity, int jaundice,
                             int family_asd, int residence, int used_app_before,
                             int language, int user)
    {
        float[] inputValues = new float[19];
        inputValues[0] = Float.valueOf(aq1);
        inputValues[1] = Float.valueOf(aq2);
        inputValues[2] = Float.valueOf(aq3);
        inputValues[3] = Float.valueOf(aq4);
        inputValues[4] = Float.valueOf(aq5);
        inputValues[5] = Float.valueOf(aq6);
        inputValues[6] = Float.valueOf(aq7);
        inputValues[7] = Float.valueOf(aq8);
        inputValues[8] = Float.valueOf(aq9);
        inputValues[9] = Float.valueOf(aq10);
        inputValues[10] = Float.valueOf(age);
        inputValues[11] = Float.valueOf(sex);
        inputValues[12] = Float.valueOf(ethnicity);
        inputValues[13] = Float.valueOf(jaundice);
        inputValues[14] = Float.valueOf(family_asd);
        inputValues[15] = Float.valueOf(residence);
        inputValues[16] = Float.valueOf(used_app_before);
        inputValues[17] = Float.valueOf(language);
        inputValues[18] = Float.valueOf(user);

        float[][] outputValues = new float[1][1];
        tensorflowInterpreter.run(inputValues, outputValues);
        float inferredValue = outputValues[0][0];

        return inferredValue;
    }


    private MappedByteBuffer loadModelFile() throws IOException
    {
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd("converted_model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

}
