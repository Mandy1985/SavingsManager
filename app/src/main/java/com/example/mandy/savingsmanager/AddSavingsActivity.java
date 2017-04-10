package com.example.mandy.savingsmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;

public class AddSavingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_savings);
    }

    private void setupUI() {
        TextView bankLabel = (TextView) findViewById(R.id.bank_Label);
        EditText bankInput = (EditText) findViewById(R.id.bank_Input);
        /*EditText.setOnClickListener(new View.onClickListener()) {
            public void onClick (View v){
                Toast toast = new Toast(AddSavingsActivity.this);
                toast.makeText(this, "Clicked,toast.length_long).show()");
            }
        }*/
        TextView startDateLabel = (TextView) findViewById(R.id.start_Date_Label);
        EditText startDateInput = (EditText) findViewById(R.id.start_Date_Input);
        TextView endDateLabel = (TextView) findViewById(R.id.end_Date_Label);
        EditText endDateInput = (EditText) findViewById(R.id.end_Date_Input);
        TextView amountLabel = (TextView) findViewById(R.id.amount_Label);
        EditText amountInput = (EditText) findViewById(R.id.amount_Input);
        TextView annualizedYieldLabel = (TextView) findViewById(R.id.annualized_Yield_Label);
        EditText annualizedYieldInput = (EditText) findViewById(R.id.annualized_Yield_Input);
        TextView expectedInterestLabel = (TextView) findViewById(R.id.expected_Interest_Label);
        EditText expectedInterestInput = (EditText) findViewById(R.id.expected_Interest_Input);
    }
}
