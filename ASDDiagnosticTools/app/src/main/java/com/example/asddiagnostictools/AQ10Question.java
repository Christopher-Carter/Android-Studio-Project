package com.example.asddiagnostictools;

public class AQ10Question
{
    private int _questionNo = -1;
    private String _questionText = null;

    // These two variables determine which answers are granted one point as dictated by the rules of
    // each AQ10 survey.  The answers that grant points are different between child and adult surveys
    // so this is necessary.
    private int _answerWorthPoints1 = -1;
    private int _answerWorthPoints2 = -1;
    private int _answerProvidedByUser = -1;

    public AQ10Question(int questionNo, String questionText, int answerWorthPoints1, int answerWorthPoints2)
    {
        _questionNo = questionNo;
        _questionText = questionText;
        _answerWorthPoints1 = answerWorthPoints1;
        _answerWorthPoints2 = answerWorthPoints2;

    }

    public int getQuestionNo()
    {
        return _questionNo;
    }

    public String getQuestionText()
    {
        return _questionText;
    }

    public int getAnswerWorthPoints1()
    {
        return _answerWorthPoints1;
    }

    public int getAnswerWorthPoints2()
    {
        return _answerWorthPoints2;
    }

    public int getAnswerProvidedByUser() { return _answerProvidedByUser; }

    public void setAnswerProvidedByUser(int answer) { _answerProvidedByUser = answer; }

    public int getValueOfAnswer() {

        // Only provide
        if (_answerProvidedByUser == _answerWorthPoints1 || _answerProvidedByUser == _answerWorthPoints2)
            return 1;
        else
            return 0;
    }


}
