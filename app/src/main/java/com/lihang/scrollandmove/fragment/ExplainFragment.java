package com.lihang.scrollandmove.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lihang.scrollandmove.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by leo
 * on 2020/3/11.
 */
public class ExplainFragment extends Fragment {
    ArrayList<Integer> starts = new ArrayList<>();
    ArrayList<Integer> ends = new ArrayList<>();
    TextView txt_test;
    int type;

    public ExplainFragment(int type) {
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explain, null);
        txt_test = view.findViewById(R.id.txt_test);
        if (type==1){
            String str = getString(R.string.message);
            SpannableString string = new SpannableString(str);
            for (int i = 0; i < str.length(); i++) {
                String subStr = str.substring(i, i + 1);
                if (subStr.equals("【")) {
                    starts.add(i);
                } else if (subStr.equals("】")) {
                    ends.add(i);
                }
            }

            if (starts.size() == ends.size()) {
                for (int i = 0; i < starts.size(); i++) {
                    StyleSpan span = new StyleSpan(Typeface.BOLD);
                    string.setSpan(span, starts.get(i), ends.get(i) + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    string.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black_shuo)), starts.get(i), ends.get(i) + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }

            txt_test.setText(string);
        }

        return view;

    }
}
