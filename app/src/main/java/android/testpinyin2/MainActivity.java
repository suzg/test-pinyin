package android.testpinyin2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.github.stuxuhai.jpinyin.PinyinException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<String> namelist = new LinkedList<>();
    String allNames;
    PinyinSearch search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView output = this.findViewById(R.id.textView);
        EditText input = this.findViewById(R.id.editText);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchName = editable.toString();
                if (TextUtils.isEmpty(searchName)) {
                    output.setText(allNames);
                } else {
                    StringBuilder s = new StringBuilder();
                    List<PinyinSearch.Score> scores = null;
                    try {
                        scores = search.search(searchName, 10);
                    } catch (PinyinException e) {
                        e.printStackTrace();
                        return;
                    }
                    for (PinyinSearch.Score score : scores) {
                        s.append(score.toString() + "\n");
                    }
                    output.setText(s.toString());
                }
            }
        });

        try {
            InputStream is = getAssets().open("names.txt");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            for (String line; (line = br.readLine()) != null; ) {
                namelist.add(line);
            }
            allNames = TextUtils.join("\n", namelist);
            search = new PinyinSearch(namelist);
        } catch (IOException e) {

        } catch (PinyinException e) {

        }

        output.setText(allNames);
    }
}
