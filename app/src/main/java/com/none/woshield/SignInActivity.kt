package com.none.woshield

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        signup_btn_signin.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
        signin_btn_signin.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        val emailid=emailid_signin.text.toString()
        val password=password_signin.text.toString()
        when{
            emailid.isEmpty()->{
                Toast.makeText(this,"Email is required",Toast.LENGTH_SHORT).show()
            }
            password.isEmpty()-> {
                Toast.makeText(this,"Password is required",Toast.LENGTH_SHORT).show()
            }
            else->            {
                val progressDialog= ProgressDialog(this)
                progressDialog.setTitle("Sign In")
                progressDialog.setMessage("Please wait this may take a while")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()
                val mAuth= FirebaseAuth.getInstance()
                mAuth.signInWithEmailAndPassword(emailid,password).addOnCompleteListener {task->
                    if(task.isSuccessful)
                    {
                        progressDialog.dismiss()
                        val intent=Intent(this,MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        val message = task.exception.toString()
                        Toast.makeText(this, "Error: ${message}", Toast.LENGTH_LONG).show()
                        FirebaseAuth.getInstance().signOut()
                        progressDialog.dismiss()
                    }
                }
            }

        }
    }

    override fun onStart()
    {
        super.onStart()
        if(FirebaseAuth.getInstance().currentUser!=null)
        {
            val intent=Intent(this,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}