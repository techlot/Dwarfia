package com.example.dwarfia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        login_btn.setOnClickListener {
            when {
                TextUtils.isEmpty(login_email_txt.editText?.text.toString().trim {it <= ' '}) -> {
                    Toast.makeText(
                        this@SignInActivity,
                        "Please enter the email",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(login_pwd_txt.editText?.text.toString().trim {it <= ' '}) -> {
                    Toast.makeText(
                        this@SignInActivity,
                        "Please enter the password",
                        Toast.LENGTH_SHORT
                    ).show()
                } else -> {
                val email: String = login_email_txt.editText?.text.toString().trim {it <= ' '}
                val password: String = login_pwd_txt.editText?.text.toString().trim {it <= ' '}

                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(
                        OnCompleteListener<AuthResult> { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this@SignInActivity,
                                    "Successfully registered!",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val intent = Intent(this@SignInActivity, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("user_id", FirebaseAuth.getInstance().currentUser!!.uid)
                                intent.putExtra("email_id", email)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this@SignInActivity, task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
            }

            }
        }

        register_btn_txt.setOnClickListener{
            val intent = Intent(this@SignInActivity, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}