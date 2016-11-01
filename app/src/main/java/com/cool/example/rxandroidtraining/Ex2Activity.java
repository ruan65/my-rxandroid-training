package com.cool.example.rxandroidtraining;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cool.example.rxandroidtraining.databinding.ActivityEx2Binding;
import com.cool.example.rxandroidtraining.databinding.ActivityExample1Binding;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Ex2Activity extends AppCompatActivity {

    ActivityEx2Binding b;

    Observable<String> myObservable;
    Observer<String> myObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        b = DataBindingUtil.setContentView(this, R.layout.activity_ex2);

        setTitle("Example 2");

        createObsvls();

        b.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                b.button.setEnabled(false);

                myObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(myObserver);
            }
        });
// test changes
    }

    private void createObsvls() {

        myObservable = Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String[] strings = b.editText.getText().toString().split("\n");

                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < strings.length; i++) {

                    sb.append((i + 1) + ". " + strings[i] + "\n");
                }

                subscriber.onNext(sb.toString());
                subscriber.onCompleted();
            }
        });

        myObserver = new Observer<String>() {
            @Override
            public void onCompleted() {

                b.button.setEnabled(true);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                b.textView.setText(s);
            }
        };
    }
}
