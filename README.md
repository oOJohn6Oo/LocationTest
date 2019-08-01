`拿完就溜、日后研究党`自取
``` Kotlin
// 效果为长按震动后响应拖拽
// 这里image为ImageView的实例
  image.setOnLongClickListener { view ->
            canMove = true
            (getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(2)
            false
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
                            // Method 1 可运行
//                            val params = image.layoutParams as LinearLayout.LayoutParams
//                            params.leftMargin = l
//                            params.topMargin = t
//                            image.requestLayout()
                            // Method 2 测试不行
//                            image.layout(l, t, r, b)
                            // Method 3 测试不行
//                                image.left += xDis
//                                image.top += yDis
//                                image.requestLayout()
                            // Method 4 可运行
                            image.x += xDis
                            image.y += yDis
                        }
                    }
                    lastPoint.x = event.rawX.toInt()
                    lastPoint.y = event.rawY.toInt()
                    false
                }
                MotionEvent.ACTION_UP -> {
                    canMove = false
                    true
                }
                else -> true
            }
        }
```

## 基本知识点：Android坐标系
  * View的getX和ActionEvent的getX不一样
 View中： X = translationX + Left
 即：控件在整个屏幕中的X坐标 = 偏移量 + 其与父控件的左边距
## 拖拽的研究思路
 `ACTION_DOWN`时记录位置存储到lastX，lastY，然后每一次`ACTION_MOVE`记录当前的x、y与其对应的上一次值的偏差，使widget移动相应距离，最后设置lastX、lastY为当下的x、y值
 Q:为什么需要最后一步的`设置lastX、lastY为当下的x、y值`?
 A:按照你的思路，假设`ACTION_DOWN`时为(30,40),`ACTION_MOVE`到(31,41), xDis = 1, yDis = 1, widget移动到(x+1, y+1), 似乎没问题
 但假如下一次的`ACTION_MOVE`到(32,42),xDis = 2, yDis = 2, widget移动到(x+2, y+2),诶，这不是也没问题？？？
 错了，这里的x已经是第一步中的x+1了，所以应该是(x+1+2,x+1+2)，看明白了吗？
 显然这种情况下，你需要一个wdiget的初始位置。
 或者就用这里使用的方法，每次widget只移动(这一次移动到的位置-上一次移动的位置)这么多的距离。
 坦率地讲记录初始位置好一点...因为懒，也懒得改了。
