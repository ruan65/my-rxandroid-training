package com.cool.example.rxandroidtraining;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cool.example.rxandroidtraining.databinding.ActivityExample1Binding;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;

public class ExampleRxActivity1 extends AppCompatActivity {


    ActivityExample1Binding rx1Binding;
    Observable<String> myObservable;

    Observer<String> myObserver = new Observer<String>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(String s) {
            rx1Binding.textView.setText(s);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Example 1");

        rx1Binding = DataBindingUtil.setContentView(this, R.layout.activity_example_1);

        myObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> sub) {

                sub.onNext(rx1Binding.editText.getText().toString());
                sub.onCompleted();
            }
        });

        rx1Binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myObservable.subscribe(myObserver);
            }
        });
    }
}
