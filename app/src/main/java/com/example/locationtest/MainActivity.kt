package com.example.locationtest

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.os.Vibrator
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity() {

    var canMove = false
    var lastPoint = Point(0, 0)
    var originalWidth = 0
    var originalHeight = 0
    var textValue = 10
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        originalWidth = image.width
        originalHeight = image.height

//        originalHeight = image.height
//        设置boolena值为true，表示可以移动iamge
//        设置宽高为原来的1.1倍，表示可以拖动，并且震动
        image.setOnLongClickListener { view ->
            canMove = true
            (getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(10)
            false
        }
//        image.setOnDragListener{
//            view, dragEvent ->
//            when(dragEvent.action){
//                DragEvent.ACTION_DRAG_STARTED ->
//                    (getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(2)
//                DragEvent.ACTION_DRAG_ENTERED
//            }
//            true
//        }

        btn_dec.setOnClickListener {
            textValue--
            offsetText.text = textValue.toString() + "dp"
        }
        btn_add.setOnClickListener {
            textValue++
            offsetText.text = textValue.toString() + "dp"
        }
        // 设置X
        setX.setOnClickListener {
            image.x += textValue
            updateValue()
        }
        // 设置Left
        setLeft.setOnClickListener {
            image.left += textValue
            updateValue()
        }
        // 设置ScrollBy
        scrollBy.setOnClickListener {
            image.scrollBy(textValue, textValue)
            updateValue()
        }
        scrollTo.setOnClickListener {
            image.scrollTo(textValue,textValue)
            updateValue()
        }

        image.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastPoint.x = event.rawX.toInt()
                    lastPoint.y = event.rawY.toInt()
                    false
                }
                MotionEvent.ACTION_MOVE -> {
                    if (canMove) {
                        val xDis = (event.rawX).toInt() - (lastPoint.x)
                        val yDis = (event.rawY).toInt() - (lastPoint.y)
                        if (xDis != 0 && yDis != 0) {
                            val l = image.left + xDis
                            val t = image.top + yDis
                            val r = view.right + xDis
                            val b = view.bottom + yDis
                            // Method 1
//                            val params = image.layoutParams as LinearLayout.LayoutParams
//                            params.leftMargin = l
//                            params.topMargin = t
//                            image.requestLayout()
                            // Method 2
//                            image.layout(l, t, r, b)
                            // Method 3
//                                image.left += xDis
//                                image.top += yDis
//                                image.requestLayout()
                            // Method 4
                            image.x += xDis
                            image.y += yDis
                        }
                    }
                    lastPoint.x = event.rawX.toInt()
                    lastPoint.y = event.rawY.toInt()
                    updateValue()
                    false
                }
                MotionEvent.ACTION_UP -> {
                    canMove = false
                    (getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(2)
                    true
                }
                else -> true
            }
        }
    }

    fun updateValue(){
        getX.text = "getX:"+image.x.toString()
        scrollX.text = "scrollX:"+image.scrollX.toString()
        if (image.rawX==null) rawX.text="NULL" else rawX.text = "rawX:"+image.rawX.toString()
        translationX.text = "translationX:"+image.translationX.toString()
        getLeft.text = "getLeft:"+image.left.toString()
    }
}
