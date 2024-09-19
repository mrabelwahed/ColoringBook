package com.droidcourses.coloringbook.util

import android.graphics.Bitmap
import android.graphics.Point
import java.util.LinkedList
import java.util.Queue


//fun floodFill( bitmap: Bitmap,  point: Point,  targetColor: Int, selectedColor:Int ){
//
//}

fun floodFill(
    image: Bitmap, node: Point, targetColor: Int,
    replacementColor: Int
) {
    var node = node
    val width = image.width
    val height = image.height
    val target = targetColor
    val replacement = replacementColor
    if (target != replacement) {
        val queue: Queue<Point> = LinkedList()
        do {
            var x = node.x
            val y = node.y
            while (x > 0 && image.getPixel(x - 1, y) == target) {
                x--
            }
            var spanUp = false
            var spanDown = false
            while (x < width && image.getPixel(x, y) == target) {
                image.setPixel(x, y, replacement)
                if (!spanUp && y > 0 && image.getPixel(x, y - 1) == target) {
                    queue.add(Point(x, y - 1))
                    spanUp = true
                } else if (spanUp && y > 0 && image.getPixel(x, y - 1) != target) {
                    spanUp = false
                }
                if (!spanDown && y < height - 1 && image.getPixel(x, y + 1) == target) {
                    queue.add(Point(x, y + 1))
                    spanDown = true
                } else if (spanDown && y < height - 1 && image.getPixel(x, y + 1) != target) {
                    spanDown = false
                }
                x++
            }
        } while ((queue.poll().also { node = it }) != null)
    }
}