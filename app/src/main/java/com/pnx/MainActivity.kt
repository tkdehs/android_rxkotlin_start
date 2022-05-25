package com.pnx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.jakewharton.rxbinding4.view.clicks
import com.pnx.databinding.ActivityMainBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.kotlin.toObservable
import java.util.concurrent.TimeUnit
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    val bag = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btn1.clicks()
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val list = arrayListOf("1","2","3","4","5","6","7","8")
                var dis = list.toObservable()
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
                bag.add(dis)
            }

        var dis:Observable<Long>? = null
        binding.btn2.clicks()
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe {
                // share() 를 사용하면 구독자가 추가 되더라도 현재 시퀀스를 공유한다.
                // 추가해주지 않을경우 새로 추가된 구독자에게는 처음부터 다시 방출된다.
                dis = Observable.interval(1000,TimeUnit.MILLISECONDS).share()
            }
        var count = 1
        binding.btn3.clicks()
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe {
                var subscribeCnt = count
                dis?.subscribe {
                    Log.d("RX","subscriber[$subscribeCnt] : $it")
                }
                count++
            }
    }
}