package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView textViewResult;

    private String currentNumber = "";
    private String operator = "";
    private double firstNumber = 0;
    private boolean isNewCalculation = true;
    private static final String DEFAULT_VALUE = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.textViewResult);
        setupButtonListeners();
        resetCalculationState();
    }

    private void setupButtonListeners() {
        int[] numberButtonIds = {R.id.buttonZero, R.id.buttonOne, R.id.buttonTwo, R.id.buttonThree,
                R.id.buttonFour, R.id.buttonFive, R.id.buttonSix, R.id.buttonSeven,
                R.id.buttonEight, R.id.buttonNine, R.id.buttonDot};

        int[] basicOperationIds = {R.id.buttonAdd, R.id.buttonSubtract, R.id.buttonMultiply, R.id.buttonDivide};

        // Skaičių mygtukai
        for (int id : numberButtonIds) {
            findViewById(id).setOnClickListener(view -> onNumberOrDotClick(((Button) view).getText().toString()));
        }

        // Pagrindinės operacijos
        for (int id : basicOperationIds) {
            findViewById(id).setOnClickListener(view -> onOperationClick(((Button) view).getText().toString()));
        }

        //Veikiančios funkcijos
        findViewById(R.id.buttonEquals).setOnClickListener(view -> onEqualsClick());
        findViewById(R.id.buttonClear).setOnClickListener(view -> onClearClick());       // C
        findViewById(R.id.buttonBackspace).setOnClickListener(view -> onDeleteClick());  // ←

        // Visi kiti mygtukai IMITACIJA
        int[] dummyButtonIds = {R.id.buttonClearEntry, R.id.buttonSignChange,
                R.id.buttonMemoryClear, R.id.buttonMemoryRecall,
                R.id.buttonMemorySave, R.id.buttonMemoryAdd,
                R.id.buttonMemorySubtract,
                R.id.buttonSquareRoot, R.id.buttonPercent,
                R.id.buttonReciprocal};
        for (int id : dummyButtonIds) {
            findViewById(id).setOnClickListener(view -> onDummyClick());
        }
    }
    private void resetCalculationState() {
        currentNumber = DEFAULT_VALUE;
        operator = "";
        firstNumber = 0;
        isNewCalculation = true;
    }
    private void onNumberOrDotClick(String value) {
        if (currentNumber.equals("Error")) {
            resetCalculationState();
        }
        if (isNewCalculation) {
            currentNumber = "";
            isNewCalculation = false;
        }
        if (value.equals(".")) {
            if (!currentNumber.contains(".")) {
                currentNumber = currentNumber.isEmpty() ? "0." : currentNumber + value;
            }
        } else {
            if (currentNumber.equals(DEFAULT_VALUE) && !currentNumber.contains(".")) {
                currentNumber = value;
            } else {
                currentNumber += value;
            }
        }
        textViewResult.setText(currentNumber);
    }
    private void onOperationClick(String newOperator) {
        if (currentNumber.equals("Error")) {
            resetCalculationState();
            return;
        }
        if (operator.isEmpty() || isNewCalculation) {
            try {
                firstNumber = Double.parseDouble(currentNumber);
            } catch (NumberFormatException e) {
                return;
            }
        } else {
            calculateResult();
        }
        operator = newOperator;
        isNewCalculation = true;
    }
    private void calculateResult() {
        double secondNumber = Double.parseDouble(currentNumber);
        double result = 0;

        switch (operator) {
            case "+":
                result = firstNumber + secondNumber;
                break;
            case "-":
                result = firstNumber - secondNumber;
                break;
            case "*":
                result = firstNumber * secondNumber;
                break;
            case "/":
                if (secondNumber != 0) {
                    result = firstNumber / secondNumber;
                } else {
                    textViewResult.setText("Error");
                    resetCalculationState();
                    return;
                }
                break;
        }
        firstNumber = result;
        currentNumber = formatResult(result);
        textViewResult.setText(currentNumber);
    }
    private void onEqualsClick() {
        if (operator.isEmpty() || isNewCalculation) return;
        calculateResult();
        operator = "";
        isNewCalculation = true;
    }
    private void onClearClick() {
        resetCalculationState();
        textViewResult.setText(DEFAULT_VALUE);
    }
    private void onDeleteClick() {
        if (isNewCalculation || currentNumber.equals("Error") || currentNumber.equals(DEFAULT_VALUE)) {
            resetCalculationState();
            textViewResult.setText(DEFAULT_VALUE);
            return;
        }
        if (currentNumber.length() > 1) {
            currentNumber = currentNumber.substring(0, currentNumber.length() - 1);
        } else {
            currentNumber = DEFAULT_VALUE;
            isNewCalculation = true;
        }
        textViewResult.setText(currentNumber);
    }
    private void onDummyClick() {
    }
    private String formatResult(double result) {
        if (result == (long) result) {
            return String.format("%d", (long) result);
        } else {
            return String.valueOf(result);
        }
    }
}