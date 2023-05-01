package ca.dal.cs.csci4176.rental

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    var loggedIn = FirebaseAuth.getInstance().currentUser != null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        //add home fragment, profile fragment and post fragment
        val homeFragment = HomeFragment()
        val profileFragment = ProfileFragment()

        val postFragment = PostFragment()

        //set current page is home fragment
        setCurrentFragment(homeFragment)

        //use bottom Navigation view to change the fragment
        findViewById<BottomNavigationView>(R.id.bottomNavigationView).setOnNavigationItemSelectedListener {
            when(it.itemId){
                //if the menu id is home then change to home fragment
                R.id.home->setCurrentFragment(homeFragment)
                //if the menu id is person then change to profile page
                R.id.person->
                    //if the user login then show the page
                    if (loggedIn) {
                        setCurrentFragment(profileFragment)
                        //if the user did not login then dump to the sign in page
                    }else {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    }

            }
            true
        }

        findViewById<FloatingActionButton>(R.id.bottomAddButton).setOnClickListener{
            //if the user login then show the page
            if (loggedIn){
                setCurrentFragment(postFragment)
                //if the user did not login then dump to the sign in page
            }else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setCurrentFragment(fragment:Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
    }
}