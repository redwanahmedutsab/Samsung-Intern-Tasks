package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvDisplay;

    private double currentResult = 0;
    private double lastOperand = 0;
    private String lastOperator = "";
    private boolean isNewInput = true;
    private boolean lastPressedEquals = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDisplay = findViewById(R.id.tvDisplay);

        LinearLayout rootLayout = (LinearLayout) tvDisplay.getParent();
        GridLayout gridLayout = (GridLayout) rootLayout.getChildAt(1);

        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View child = gridLayout.getChildAt(i);
            if (child instanceof Button) {
                child.setOnClickListener(this::onButtonClick);
            }
        }
    }

    private void onButtonClick(View view) {
        Button button = (Button) view;
        String text = button.getText().toString();

        switch (text) {
            case "+":
            case "-":
            case "×":
            case "÷":
                operatorPressed(text);
                break;

            case "=":
                equalsPressed();
                break;

            case "C":
                clear();
                break;

            default:
                numberPressed(text);
        }
    }

    private void numberPressed(String number) {
        if (isNewInput) {
            tvDisplay.setText(number);
            isNewInput = false;
        } else {
            tvDisplay.append(number);
        }
        lastPressedEquals = false;
    }

    private void operatorPressed(String operator) {
        double input = Double.parseDouble(tvDisplay.getText().toString());

        if (!lastPressedEquals) {
            if (!lastOperator.isEmpty()) {
                currentResult = calculate(currentResult, input, lastOperator);
            } else {
                currentResult = input;
            }
        }

        lastOperator = operator;
        isNewInput = true;
        lastPressedEquals = false;
    }

    private void equalsPressed() {
        double input = Double.parseDouble(tvDisplay.getText().toString());

        if (!lastPressedEquals) {
            lastOperand = input;
            currentResult = calculate(
                    lastOperator.isEmpty() ? input : currentResult,
                    input,
                    lastOperator
            );
        } else {
            currentResult = calculate(currentResult, lastOperand, lastOperator);
        }

        tvDisplay.setText(formatResult(currentResult));
        isNewInput = true;
        lastPressedEquals = true;
    }

    private double calculate(double a, double b, String operator) {
        switch (operator) {
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "×":
                return a * b;
            case "÷":
                if (b == 0) {
                    tvDisplay.setText("Error");
                    clear();
                    return 0;
                }
                return a / b;
            default:
                return b;
        }
    }

    private String formatResult(double value) {
        if (value == (long) value) {
            return String.valueOf((long) value);
        } else {
            return String.valueOf(value);
        }
    }

    private void clear() {
        tvDisplay.setText("0");
        currentResult = 0;
        lastOperand = 0;
        lastOperator = "";
        isNewInput = true;
        lastPressedEquals = false;
    }
}