package com.example.mandy.savingsmanager.activities;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mandy.savingsmanager.R;
import com.example.mandy.savingsmanager.data.SavingsBean;
import com.example.mandy.savingsmanager.data.SavingsContentProvider;
import com.example.mandy.savingsmanager.data.SavingsItemEntry;
import com.example.mandy.savingsmanager.utils.Constants;
import com.example.mandy.savingsmanager.utils.Utils;

import java.util.Calendar;
import java.util.Date;

public class AddSavingsActivity extends AppCompatActivity {

    //Edit Text for the field
    private EditText bankInput;
    private EditText startDateInput;
    private EditText endDateInput;
    private EditText amountInput;
    private EditText annualizedYieldInput;
    private EditText expectedInterestInput;

    // Data
    private Calendar mCalendar = Calendar.getInstance();
    private Date mStartDate;
    private Date mEndDate;
    private float mAmount;
    private float mYield;
    private float mInterest;
    private SavingsBean mSavingsBean;
    private boolean mEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_savings);
        setupUI();
    }

    private void setupUI() {

        // ID from Layout to java
        bankInput = (EditText) findViewById(R.id.bank_Input);
        startDateInput = (EditText) findViewById(R.id.start_Date_Input);
        endDateInput = (EditText) findViewById(R.id.end_Date_Input);
        amountInput = (EditText) findViewById(R.id.amount_Input);
        annualizedYieldInput = (EditText) findViewById(R.id.annualized_Yield_Input);
        expectedInterestInput = (EditText) findViewById(R.id.expected_Interest_Input);

        //Set Text Watcher to update interest
        bankInput.addTextChangedListener(mInterestTextWatcher);
        amountInput.addTextChangedListener(mInterestTextWatcher);
        annualizedYieldInput.addTextChangedListener(mInterestTextWatcher);
        amountInput.setOnFocusChangeListener(mOnFocusChangeListener);
        annualizedYieldInput.setOnFocusChangeListener(mOnFocusChangeListener);

        //set date field listener
        startDateInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange (View v, boolean hasFocus){
                Toast.makeText(getApplicationContext(),"onFocusChange",Toast.LENGTH_SHORT).show();
                if (hasFocus){
                    Toast.makeText(getApplicationContext(),"onFocusChange",Toast.LENGTH_SHORT).show();
                    showDatePicker((EditText) v, true);
                }
            }
        } );
        startDateInput.setInputType(InputType.TYPE_NULL);
        startDateInput.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (v.hasFocus()){
                   showDatePicker((EditText)v, true);
                }
            }
        });
        endDateInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange (View v, boolean hasFocus){
                if (hasFocus){
                    showDatePicker((EditText) v, false);
                }
            }
        } );
        endDateInput.setInputType(InputType.TYPE_NULL);
        endDateInput.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (v.hasFocus()){
                    showDatePicker((EditText)v, false);
                }
            }
        });

        mSavingsBean = getIntent().getParcelableExtra(Constants.INTENT_EXTRA_SAVINGS_ITEM_PARCEL);
        if (mSavingsBean != null) {
            // set data to UI
            bankInput.setText(mSavingsBean.getBankName());
            startDateInput.setText(Utils.formatDate(mSavingsBean.getStartDate()));
            endDateInput.setText(Utils.formatDate(mSavingsBean.getEndDate()));
            amountInput.setText(Utils.formatFloat(mSavingsBean.getAmount()));
            annualizedYieldInput.setText(Utils.formatFloat(mSavingsBean.getYield()));
            expectedInterestInput.setText(Utils.formatFloat(mSavingsBean.getInterest()));

            // update the buttons
            ((Button) findViewById(R.id.save_Button)).setText(R.string.update);
            ((Button) findViewById(R.id.cancel_Button)).setText(R.string.delete);

            mEditMode = true;
            // update the data in this screen
            mStartDate = new Date(mSavingsBean.getStartDate());
            mEndDate = new Date(mSavingsBean.getEndDate());
            mAmount = mSavingsBean.getAmount();
            mYield = mSavingsBean.getYield();
            mInterest = mSavingsBean.getInterest();
            Log.d(Constants.LOG_TAG, "Edit mode, displayed existing savings item:");
            Log.d(Constants.LOG_TAG, mSavingsBean.toString());

        }
    }

    /**
     * A text watcher for fields which impacts interest
     */
    private TextWatcher mInterestTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            updateInterest();
        }
    };

    /**
     * A OnFocusChangeListener for Amount and Yield field
     */
    private View.OnFocusChangeListener mOnFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                EditText edit = (EditText) v;
                String amountStr = edit.getText().toString();
                if (!Utils.isNullOrEmpty(amountStr)) {
                    edit.setText(Utils.getFloat(amountStr));
                }
            }
        }
    };

    /**
     * Show date picker for edit field of date
     *
     * @param edit      date field
     * @param startDate whether it's start date
     */
    private void showDatePicker(final EditText edit, final boolean startDate) {
        // hide keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);

        // show date picker
        DatePickerDialog picker = new DatePickerDialog(AddSavingsActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // set the date to EditText
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, dayOfMonth);
                        Date date = new Date(calendar.getTimeInMillis());
                        edit.setText(Utils.formatDate(date, Constants.FORMAT_DATE_YEAR_MONTH_DAY));
                        if (startDate) {
                            mStartDate = new Date(date.getTime());
                        } else {
                            mEndDate = new Date(date.getTime());
                        }
                        // has to do here
                        updateInterest();
                    }
                },
                mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        picker.show();
    }

    /**
     * Update interest based on current values
     */
    public void updateInterest(){
        //Caculate when all fields are available
        String amountStr = amountInput.getText().toString();
        String yieldStr = annualizedYieldInput.getText().toString();
        String bankStr = bankInput.getText().toString();

        if (mStartDate!=null && mEndDate!=null
                && !Utils.isNullOrEmpty(amountStr)
                && !Utils.isNullOrEmpty(yieldStr)
                && !Utils.isNullOrEmpty(bankStr)){

            float days = Utils.getDiffDays(mStartDate,mEndDate);
            mAmount = Float.valueOf(amountStr);
            mYield = Float.valueOf(yieldStr);

            //Caculate the value
            mInterest = mAmount * (mYield/100)*(days/ Constants.DAYS_OF_ONE_YEAR);
            mInterest = Utils.roundFloat(mInterest);
            //Update the interest in UI
            expectedInterestInput.setText(Utils.formatFloat(mInterest));
            Log.d(Constants.LOG_TAG, "start = " + mStartDate.toString() + "\nend = " + mEndDate.toString()
                    + "\ndays = " + days + "\namount = " + mAmount + "\nyield = " + mYield + "\ninterest = " + mInterest);
        }
    }

    /**
     * Click listener for Cancel button
     *
     * @param view the cancel button
     */
    public void onCancelClicked(View view) {
        onBackPressed();
    }

    /**
     * Click listener for Save button
     *
     * @param view the save button
     */

    public void onSaveClicked(View view) {

        if (mInterest != 0.0f) {

            ContentValues values = new ContentValues();
            values.put(SavingsItemEntry.COLUMN_NAME_BANK_NAME, bankInput.getText().toString());
            values.put(SavingsItemEntry.COLUMN_NAME_AMOUNT, mAmount);
            values.put(SavingsItemEntry.COLUMN_NAME_YIELD, mYield);
            values.put(SavingsItemEntry.COLUMN_NAME_START_DATE, mStartDate.getTime());
            values.put(SavingsItemEntry.COLUMN_NAME_END_DATE, mEndDate.getTime());
            values.put(SavingsItemEntry.COLUMN_NAME_INTEREST, mInterest);

            if (mEditMode){
                //Update the data into database by ContentProvider
                getContentResolver().update(SavingsContentProvider.CONTENT_URI, values,
                        SavingsItemEntry._ID + "=" + mSavingsBean.getId(), null);
                Log.d(Constants.LOG_TAG, "Edit mode, updated existing savings item: " + mSavingsBean.getId());
            }else {
                // Save the data into database by ContentProvider
                getContentResolver().insert(
                        SavingsContentProvider.CONTENT_URI,
                        values
                );
            }
            // Go back to dashboard
            //Intent intent = new Intent(this, DashboardActivity.class);
            //startActivity(intent);
            Utils.gotoDashBoard(this);
            finish();
        } else {
            Toast.makeText(this, R.string.missing_savings_information, Toast.LENGTH_LONG).show();
        }

    }


}
