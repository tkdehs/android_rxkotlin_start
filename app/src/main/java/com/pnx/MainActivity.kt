package com.pnx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.textChanges
import com.pnx.databinding.ActivityMainBinding
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.kotlin.toObservable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import io.reactivex.functions.Function

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapper1 = Function<String, Observable<String>> {
            Observable.just("$it A", "$it B")
        }


        binding.btn1.clicks()
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val list = arrayListOf("1","2","3","4","5","6","7","8")
                list.toObservable()
                    .subscribeBy(
                    onNext = {
                        Log.d("RX","onNext : $it")
                    },
                    onComplete = {
                        Log.d("RX","onComplete")
                    },
                    onError = {
                        Log.d("RX","onError : $it")
                    }
                )
            }
        val subject = PublishSubject.create<String>()

        subject.onNext("")
        subject.subscribe {
            // 구독 이벤트
        }

        val mapper = Function<Int, Observable<String>> {
            Observable.just("$it A", "$it B")
        }

        Observable.fromArray(1, 2, 3)
            .flatMap(mapper)
            .subscribe {
                Log.d("RX","$it")
            }
    }
}