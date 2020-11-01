package com.example.hangman


import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class game : AppCompatActivity() {


    // Main variables
    private var noGuesses = 0
    var word = ""
    private var hangman = listOf(
        R.drawable.hangman1,
        R.drawable.hangman2,
        R.drawable.hangman3,
        R.drawable.hangman4,
        R.drawable.hangman5,
        R.drawable.hangman6,
        R.drawable.hangman7
    )

    // variables for guessing
    private var validLetters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKMNOPQRSTUVWXYZ ".toList()
    private var guessed = mutableListOf(' ')
    private var oldGuessUI=""
    private var letterList:MutableList<Any> = mutableListOf(" ", 1)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getword)

        // hide systemUI
        hideSystemUI()



    }



    fun checkWord(view: View) {
        // gets word
        val getWord = findViewById<EditText>(R.id.getword)
        word = getWord.text.toString()

        // checks whether word is valid or not
        var isValid = true
        var invalidText = "invalid word"
        if (word==""){
            isValid = false
            invalidText = "need to enter word"
        }
        for (i in word.toList()){
            if (i !in validLetters){
                isValid = false
                invalidText = "your word must have English letters"
            }
        }


        // actions are taken upon whether or not the word is valid
        if (isValid){
            // change view to actual game
            setContentView(R.layout.activity_game)
            guess()
        }else{
            // invalid error text is shown to user
            findViewById<TextView>(R.id.invalid).text =  invalidText
        }
        hideSystemUI()
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

            lateinit var player: MediaPlayer
            // visual and audio confirmation of whether letter is correct or wrong
            if (noGuesses == oldGuessNo){
                letter.setBackgroundColor(0xFF0000FF.toInt())
                player = MediaPlayer.create(this, R.raw.correct)
                player.start()
            }else{
                letter.setBackgroundColor(0xFFFF0000.toInt())
                player = MediaPlayer.create(this, R.raw.wrong)
                player.start()
            }





            // visual and audio results of winning or losing
            var wordReveal = "The word was $word"

            if (noGuesses == 6) {
                setContentView(R.layout.activity_lose)
                findViewById<TextView>(R.id.wordReveal).text = wordReveal
                var player = MediaPlayer.create(this, R.raw.lose)
                player.start()
            } else if (oldGuessUI == word) {
                setContentView(R.layout.activity_win)
                findViewById<TextView>(R.id.wordReveal).text = wordReveal
                var player = MediaPlayer.create(this, R.raw.win)
                player.start()
            }
            player.setOnCompletionListener { mp ->
                mp.reset()
                mp.release()
            }
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
            findViewById<ImageView>(R.id.hangman).setImageResource(hangman[noGuesses])
        }

        // old guessUI is updated to new guessUI for later use
        oldGuessUI = guessUI

        // the text of the text view is updated
        findViewById<TextView>(R.id.guessUI).text = guessUI
        val tries = "Tries: $noGuesses"
        findViewById<TextView>(R.id.tries).text = tries
    }


    // adds letter to guess
    private fun guess(letter: String){
        guessed.addAll(letter.toList())
        guess()
    }

    // when anything is clicked in win or lose page it goes back to home page
    fun home(view: View){
        val intent = Intent(this, MainActivity::class.java)
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
        overridePendingTransition(R.anim.grow_from_middle, R.anim.shrink_to_middle);
        var player = MediaPlayer.create(this, R.raw.transition)
        player.start()
        player.setOnCompletionListener { mp ->
            mp.reset()
            mp.release()
        }
    }
}




