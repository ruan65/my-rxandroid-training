package com.cool.example.rxandroidtraining;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cool.example.rxandroidtraining.databinding.ActivityEx3Binding;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class Ex3Activity extends AppCompatActivity {

    ActivityEx3Binding b;

    Observable<String[]> myObservable;

    Observer<String> myObserver;

    int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b = DataBindingUtil.setContentView(this, R.layout.activity_ex3);

        setTitle("Example 3");

        createObserver();

        b.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.button.setEnabled(false);

                Observable<String> from = Observable.from(b.editText.getText().toString().split("\n"));

                from
                        .subscribeOn(Schedulers.io())
                        .map(new Func1<String, String>() {
                            @Override
                            public String call(String morphedString) {

                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                return count++ + ". " + morphedString;
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(myObserver);
                        
            }
        });
    }

    private void createObserver() {

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

                b.textView.append(s + "\n");
            }
        };
    }
}
