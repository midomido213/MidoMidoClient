package midomido213.midomidoclient;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TweetActivity extends AppCompatActivity {

    private EditText mInputText;
    private Twitter mTwitter;

    private EditText editText;
    private TextView textCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);

        mTwitter = TwitterUtils.getTwitterInstance(this);

        mInputText = (EditText) findViewById(R.id.input_text);

        findViewById(R.id.action_tweet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tweet();
            }
        });

        textCount = (TextView) findViewById(R.id.tweet_count);
        editText = (EditText) findViewById(R.id.input_text);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){
                int textColor = Color.GRAY;

                // 入力文字数の表示
                int txtLength = s.length();
                textCount.setText(Integer.toString(txtLength) + "/140");

                // 指定文字数オーバーで文字力を赤くする
                if (txtLength > 140) {
                    textColor = Color.RED;
                }
                textCount.setTextColor(textColor);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
    }

    private void tweet() {
        AsyncTask<String, Void, Boolean> task = new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                try {
                    mTwitter.updateStatus(params[0]);
                    return true;
                } catch (TwitterException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result) {
                    showToast("ツイートが完了しました！");
                    finish();
                } else {
                    showToast("ツイートに失敗しました。");
                }
            }
        };
        task.execute(mInputText.getText().toString());
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
