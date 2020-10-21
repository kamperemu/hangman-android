package com.example.hangman


import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class game : AppCompatActivity() {


    // Main variables
    private var noGuesses = 0
    var word = ""

    // variables for guessing
    private var guessed = mutableListOf(' ')
    private var oldGuessUI=""
    private var letterList:MutableList<Any> = mutableListOf(" ",1)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getword)

        val button = findViewById<Button>(R.id.submit_word)
        val getWord = findViewById<EditText>(R.id.getword)
        // when the word is submitted it gets stored and the layout is changed and the guessUI changes according to the word guessed
        button.setOnClickListener {
            word = getWord.text.toString()
            setContentView(R.layout.activity_game)
            guess()
            // hide systemUI because when keyboard comes the system UI pops up
            hideSystemUI()
        }
    }



    // sends guess on letter being clicked
    fun sendGuess(view: View) {

        // get the letter as a button object
        val letter:Button = view as Button


        // if the letter is already used then it will not change the guessUI
        if (letter !in letterList) {
            val oldGuessNo = noGuesses
            letterList.add(letter)
            guess(letter.text as String)


            // visual and audio confirmation of whether letter is correct or wrong
            if (noGuesses == oldGuessNo){
                letter.setBackgroundColor(0xFF0000FF.toInt())
                var player = MediaPlayer.create(this,R.raw.correct)
                player.start()
            }else{
                letter.setBackgroundColor(0xFFFF0000.toInt())
                var player = MediaPlayer.create(this,R.raw.wrong)
                player.start()
            }

            // win or lose after 1 sec
            val handler = Handler()
            handler.postDelayed({
                var wordReveal = "The word was $word"
                if (noGuesses==6) {
                    setContentView(R.layout.activity_lose)
                    findViewById<TextView>(R.id.wordReveal).text = wordReveal
                }else if (oldGuessUI == word){
                    setContentView(R.layout.activity_win)
                    findViewById<TextView>(R.id.wordReveal).text = wordReveal
                }
            }, 1000)



        }
    }



    // changes the guessUI and tries textView to update the guesses
    private fun guess(){

        var guessUI = ""
        word = word.toUpperCase()

        // the following code finds the letters that have already been guessed
        for (i in word.toList()){

            // if the letter in the word is in guessed then letterActivation is true
            var letterActivation = false
            for (j in guessed){
                if (i==j){
                    letterActivation = true
                }
            }

            // if the letter is in the word then it will add letter to the guessUI or else it will add a "_ "
            if (letterActivation){
                guessUI += i
            }else{
                guessUI += "_ "
            }
        }

        // if the oldGuessUI is same as the new one it means the new letter that has been guessed is wrong so noGuesses gets incremented
        if (oldGuessUI == guessUI){
            noGuesses++
        }

        // old guessUI is updated to new guessUI for later use
        oldGuessUI = guessUI

        // the text of the text view is updated
        findViewById<TextView>(R.id.guessUI).text = guessUI
        val tries = "Tries: $noGuesses"
        findViewById<TextView>(R.id.tries).text = tries
    }


    // adds letter to guess
    private fun guess(letter:String){
        guessed.addAll(letter.toList())
        guess()
    }

    // when anything is clicked in win or lose page it goes back to home page
    fun home(view: View){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }







    // common functions - i have copied and pasted cause i don't know another way to do this right now


    // hide systemUI as long as window has focus
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    // hides status bar and navigation bar
    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
        }
    }

    // flip animation on changing
    override fun finish(){
        super.finish()
        overridePendingTransition(R.anim.grow_from_middle,R.anim.shrink_to_middle);
        var player = MediaPlayer.create(this,R.raw.transition)
        player.start()
    }
}




