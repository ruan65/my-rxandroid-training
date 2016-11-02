package com.cool.example.rxandroidtraining;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cool.example.rxandroidtraining.databinding.ActivityEx2Binding;
import com.cool.example.rxandroidtraining.databinding.ActivityEx4Binding;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class Ex4Activity extends AppCompatActivity {

    ActivityEx4Binding b;

    Observer<String> myObserver;
    Subscription subscription;

    int counter = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b = DataBindingUtil.setContentView(this, R.layout.activity_ex4);

        setTitle("Example 4");

        createObserver();

        b.startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.startButton.setEnabled(false);
                b.stopButton.setEnabled(true);

                subscription = Observable.from(b.editText.getText().toString().split("\n"))
                        .subscribeOn(Schedulers.io())
                        .map(new Func1<String, String>() {

                            @Override
                            public String call(String s) {

                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                if (b.errorToggle.isChecked()) {
                                    counter = 2 / 0;
                                }
                                return counter++ + ". " + s;
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(myObserver);
            }
        });

        b.stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!subscription.isUnsubscribed()) {
                    subscription.unsubscribe();
                }

                b.startButton.setEnabled(true);
                b.stopButton.setEnabled(false);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    private void createObserver() {

        myObserver = new Observer<String>() {
            @Override
            public void onCompleted() {
                b.startButton.setEnabled(true);
                b.stopButton.setEnabled(false);
            }

            @Override
            public void onError(Throwable e) {

                Toast.makeText(Ex4Activity.this, "A \"" + e.getMessage() + "\" Error has been caught", Toast.LENGTH_LONG).show();

                b.startButton.setEnabled(true);
                b.stopButton.setEnabled(false);
            }

            @Override
            public void onNext(String s) {

                b.textView.append(s + "\n");
            }
        };
    }
}
