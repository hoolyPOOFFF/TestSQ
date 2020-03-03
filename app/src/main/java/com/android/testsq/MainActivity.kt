package com.android.testsq

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.content.Intent
import android.text.TextUtils
import android.database.Cursor
import android.widget.EditText
import android.database.sqlite.SQLiteDatabase
import android.view.View
import android.widget.Button


class MainActivity : AppCompatActivity() {

    lateinit var LogInButton: Button
    lateinit var RegisterButton: Button
    lateinit var Email: EditText
    lateinit var Password: EditText
    lateinit var EmailHolder: String
    lateinit var PasswordHolder: String
    var EditTextEmptyHolder: Boolean? = null
    lateinit var sqLiteDatabaseObj: SQLiteDatabase
    lateinit var sqLiteHelper: SQLiteHelper
    lateinit var cursor: Cursor
    var TempPassword = "NOT_FOUND"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        LogInButton = findViewById(R.id.buttonLogin) as Button

        RegisterButton = findViewById(R.id.buttonRegister) as Button

        Email = findViewById(R.id.editEmail) as EditText
        Password = findViewById(R.id.editPassword) as EditText

        sqLiteHelper = SQLiteHelper(this)

        //Adding click listener to log in button.
        LogInButton.setOnClickListener(object : View.OnClickListener {
           override fun onClick(view: View) {

                // Calling EditText is empty or no method.
                CheckEditTextStatus()

                // Calling login method.
                LoginFunction()


            }
        })

        // Adding click listener to register button.
        RegisterButton.setOnClickListener(object : View.OnClickListener {
          override  fun onClick(view: View) {

                // Opening new user registration activity using intent on button click.
                val intent = Intent(this@MainActivity, RegisterActivity::class.java)
                startActivity(intent)

            }
        })

    }

    // Login function starts from here.
    fun LoginFunction() {

        if (EditTextEmptyHolder!!) {

            // Opening SQLite database write permission.
            sqLiteDatabaseObj = sqLiteHelper.getWritableDatabase()

            // Adding search email query to cursor.
            cursor = sqLiteDatabaseObj.query(
                SQLiteHelper.TABLE_NAME,
                null,
                " " + SQLiteHelper.Table_Column_2_Email + "=?",
                arrayOf(EmailHolder),
                null,
                null,
                null
            )

            while (cursor.moveToNext()) {

                if (cursor.isFirst()) {

                    cursor.moveToFirst()

                    // Storing Password associated with entered email.
                    TempPassword =
                        cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_3_Password))

                    // Closing cursor.
                    cursor.close()
                }
            }

            // Calling method to check final result ..
            CheckFinalResult()

        } else {

            //If any of login EditText empty then this block will be executed.
            Toast.makeText(
                this@MainActivity,
                "Please Enter UserName or Password.",
                Toast.LENGTH_LONG
            ).show()

        }

    }

    // Checking EditText is empty or not.
    fun CheckEditTextStatus() {

        // Getting value from All EditText and storing into String Variables.
        EmailHolder = Email.text.toString()
        PasswordHolder = Password.text.toString()

        // Checking EditText is empty or no using TextUtils.
        if (TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder)) {

            EditTextEmptyHolder = false

        } else {

            EditTextEmptyHolder = true
        }
    }

    // Checking entered password from SQLite database email associated password.
    fun CheckFinalResult() {

        if (TempPassword.equals(PasswordHolder, ignoreCase = true)) {

            Toast.makeText(this@MainActivity, "Login Successfully", Toast.LENGTH_LONG).show()

            // Going to Dashboard activity after login success message.
            val intent = Intent(this@MainActivity, AllUsersActivity::class.java)

            // Sending Email to Dashboard Activity using intent.
            intent.putExtra(UserEmail, EmailHolder)

            startActivity(intent)


        } else {

            Toast.makeText(
                this@MainActivity,
                "UserName or Password is Wrong, Please Try Again.",
                Toast.LENGTH_LONG
            ).show()

        }
        TempPassword = "NOT_FOUND"

    }

    companion object {
        val UserEmail = ""
    }

}