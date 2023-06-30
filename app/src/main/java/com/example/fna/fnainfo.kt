package com.example.fna

import android.content.res.TypedArray
import android.graphics.drawable.Drawable

class fnainfo (var keywords : Array<String>, var image: TypedArray){
    private var fnakeyword = keywords
    private var fnamemimage = image

    public fun getkeyword(i: Int): String {
        return fnakeyword[i]
    }

    public fun getimage(i: Int) : Drawable?{
        return fnamemimage.getDrawable(i)
    }

}