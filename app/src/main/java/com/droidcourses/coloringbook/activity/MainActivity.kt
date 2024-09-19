package com.droidcourses.coloringbook.activity

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.droidcourses.coloringbook.PicsHolder
import com.droidcourses.coloringbook.R
import com.droidcourses.coloringbook.adapter.PicsAdapter
import com.droidcourses.coloringbook.callback.OnItemClick

class MainActivity : AppCompatActivity(), OnItemClick {
    private val picRecyclerView: RecyclerView by lazy {
        findViewById(R.id.pics_recyclerview)
    }
    private val toolbar: Toolbar by lazy {
        findViewById(R.id.toolbar)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initToolBar()
        initView()
    }

    private fun initToolBar(){
        setSupportActionBar(toolbar)
        toolbar.title = "My Pics"
        toolbar.setTitleTextColor(resources.getColor(R.color.blue))
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new_24)

    }
    private fun initView() {
        picRecyclerView.apply {
            adapter = PicsAdapter(PicsHolder.pics).also { it.setClickListener(this@MainActivity) }
            layoutManager = LinearLayoutManager(applicationContext)
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClicked(pos: Int) {
       Intent(applicationContext, PaintActivity::class.java).apply {
           putExtra("pos", pos)
           startActivity(this)
       }
    }
}