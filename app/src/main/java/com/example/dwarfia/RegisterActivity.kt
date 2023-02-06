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

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        reg_btn.setOnClickListener {
            when {
                TextUtils.isEmpty(reg_email_txt.editText?.text.toString().trim {it <= ' '}) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please enter the email",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(reg_password_txt.editText?.text.toString().trim {it <= ' '}) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please enter the password",
                        Toast.LENGTH_SHORT
                    ).show()
                } else -> {
                    val email: String = reg_email_txt.editText?.text.toString().trim {it <= ' '}
                    val password: String = reg_password_txt.editText?.text.toString().trim {it <= ' '}

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(
                            OnCompleteListener<AuthResult> { task ->
                                if (task.isSuccessful) {
                                    val firebaseUser: FirebaseUser = task.result!!.user!!

                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "Successfully registered!",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    intent.putExtra("user_id", firebaseUser.uid)
                                    intent.putExtra("email_id", email)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(this@RegisterActivity, task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                }
            }
        }

        login_btn_txt.setOnClickListener{
            val intent = Intent(this@RegisterActivity, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}