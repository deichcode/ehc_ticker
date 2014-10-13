package de.deichcode.ehc_neuwiedticker;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

public class NumberPickerPreference extends DialogPreference implements NumberPicker.OnValueChangeListener
{
    private static final String NAMESPACE="http://schemas.android.com/apk/res/android";

    private NumberPicker mNumberPicker;
    private Context mCtx;

    private String mDialogMessage;
    private int mDefault;
    private int mMax;
    private int mMin;
    private int mValue = 0;
    private int stepSize = 5;

    String[] mValues = new String[60];



    public NumberPickerPreference(Context ctx, AttributeSet attr) { 
        super(ctx, attr); 
        mCtx = ctx;
        //Get XML attributes
        mDialogMessage = attr.getAttributeValue(NAMESPACE,"dialogMessage");
        mDefault = attr.getAttributeIntValue(NAMESPACE,"defaultValue", 6);
        mMax = attr.getAttributeIntValue(NAMESPACE,"max", 60);
        mMin = attr.getAttributeIntValue(NAMESPACE, "min", 1);
    }

    @Override 
    protected View onCreateDialogView() {

        for (int i = 0; i < mValues.length; i++) {
            mValues[i] = Integer.toString((i+1)*stepSize);
        }

        //Create Views
        LinearLayout dialogLayout = new LinearLayout(mCtx);
        TextView mTvDialogMessage = new TextView(mCtx);
        mNumberPicker = new NumberPicker(mCtx);

        //Set View attributes
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        if (mDialogMessage!=null){
            mTvDialogMessage.setText(mDialogMessage);
        }
        dialogLayout.addView(mTvDialogMessage);
        mNumberPicker.setOnValueChangedListener(this);
        dialogLayout.addView(mNumberPicker, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        if (shouldPersist())
            mValue = getPersistedInt(mDefault);
        mNumberPicker.setDisplayedValues(mValues);
        mNumberPicker.setMinValue(mMin);
        mNumberPicker.setMaxValue(mMax);
        mNumberPicker.setMinValue(1);
        mNumberPicker.setValue(mValue);
        return dialogLayout;
    }

    @Override 
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);
        mNumberPicker.setMaxValue(mMax);
        mNumberPicker.setMinValue(1);
        mNumberPicker.setValue(mValue / stepSize);
        v.findViewById(R.id.pref_key_auto_update_onscreen_period);
        setSummary(mValue);
    }

    @Override
    protected void onSetInitialValue(boolean restore, Object defaultValue)  
    {
        super.onSetInitialValue(restore, defaultValue);
        if (restore) 
            mValue = shouldPersist() ? getPersistedInt(mDefault) : 2;
        else 
            mValue = (Integer)defaultValue;
        if (mNumberPicker!=null)
            mNumberPicker.setValue(mValue / stepSize);
        setSummary(mValue / stepSize);
    }

    public void setSummary(int value) {
        CharSequence summary = super.getSummary();
        value=getPersistedInt(-1);
        if (summary == null) {
            setSummary(Integer.toString(value));
        } else {
            setSummary(String.format(getSummary().toString(), value));
        }
        
    }

    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        setSummary(newVal * stepSize);
        if (shouldPersist())
            persistInt(newVal * stepSize);
        callChangeListener(newVal * stepSize);
    }
}
