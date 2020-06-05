package com.example.rollit

import android.graphics.PorterDuff
import android.media.MediaPlayer
import android.os.*
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    //Use lateinit to extract the image view variable:
    private lateinit var diceImage: ImageView
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var vibrator: Vibrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //taking id's for button and image
        val rollButton: Button = findViewById(R.id.button)
        diceImage = findViewById(R.id.dice_image)
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        //step-1 adding messages
        val diceNumMessage = listOf(
            "Oops! You got 1. Better luck next time." + getEmojiByUnicode(0x1F61E),
            "You got 2.",
            "You got 3.",
            "You got 4. Try again for 6.",
            "You got 5. So close to get a 6!",
            "Hurray! You got 6." + getEmojiByUnicode(0x1F389)
        )
        rollButton.setOnClickListener {
            //rollButton.setEnabled(false)
            val dice = Dice(6)
            val diceRoll = dice.roll()
            //adding haptic feedback
            if(Build.VERSION.SDK_INT>=26) {
                vibrator.vibrate(VibrationEffect.createOneShot(75, 128))
            }
            //adding a toast on button click

            val toastRoll:Toast = Toast.makeText(this, "Rolling!", Toast.LENGTH_SHORT)
            toastRoll.show()
            //val makeToast = MakeToast(toast)
            //makeToast.toast()
            //step-2 selecting media file
            val mediaResource = if (diceRoll > 3) R.raw.dice_roll_win else R.raw.dice_roll_lose
            //step-3 adding background sound
            mediaPlayer = MediaPlayer.create(this, mediaResource)
            mediaPlayer.setOnPreparedListener {
                println("Ready to go")
            }
            //play sound
            mediaPlayer.start()
            //adding a delay
            Timer().schedule(2000) {
                //function call to roll a dice
                rollDice(diceRoll)
                Looper.prepare()
                //toast
                val toastDice:Toast = Toast.makeText(this@MainActivity, diceNumMessage[diceRoll - 1], Toast.LENGTH_LONG)
                toastDice.show()
                //val makeToast = MakeToast(toast)
                //makeToast.toast()
                Looper.loop()
            }
            //set-enabled the button
            //rollButton.setEnabled(true)
        }
    }

    private fun rollDice(diceRoll: Int) {
        //Choose the right drawable resource based on the value of diceRoll
        val drawableResource = when (diceRoll) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }

        //Finally, assign the drawableResource from above to diceImage
        diceImage.setImageResource(drawableResource)
        //val resultTextView: TextView = findViewById(R.id.textView)
        //resultTextView.text = diceRoll.toString()

    }

    private fun getEmojiByUnicode(unicode: Int): String {
        return String(Character.toChars(unicode))
    }
}

class Dice(private val numSides: Int) {
    fun roll(): Int {
        return (1..numSides).random()
    }
}

class MakeToast(private val toast:Toast) {
    fun toast() {
        var view:View = toast.getView()
        //background
        @Suppress("DEPRECATION")
        view.background.setColorFilter(21, PorterDuff.Mode.SRC_IN)

        //var text: TextView = view.findViewById(R.id.message)
        //text.setTextColor(22)

        toast.show()
    }
}