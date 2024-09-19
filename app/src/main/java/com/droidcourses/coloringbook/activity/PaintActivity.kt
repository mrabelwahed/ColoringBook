package com.droidcourses.coloringbook.activity

import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.GridLayout.Spec
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.droidcourses.coloringbook.R
import com.droidcourses.coloringbook.widget.PaintView
import com.thebluealliance.spectrum.SpectrumPalette
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import kotlin.properties.Delegates

class PaintActivity : AppCompatActivity(), SpectrumPalette.OnColorSelectedListener {
    private val toolbar: Toolbar by lazy {
        findViewById(R.id.toolbar)
    }
    private val paintView: PaintView by lazy {
        findViewById(R.id.paint_view)
    }

    private val spectrum: SpectrumPalette by lazy {
        findViewById(R.id.palette)
    }

    companion object {
        const val WRITE_EXTERNAL_STORAGE_REQUEST = 0x01
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_paint)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initToolBar()
        initView()
    }

    private fun initToolBar() {
        setSupportActionBar(toolbar)
        toolbar.title = "My Pics"
        toolbar.setTitleTextColor(resources.getColor(R.color.blue))
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new_24)

    }

    private fun initView() {
        paintView.selectedPicIndex = intent.getIntExtra("pos", 0)
        spectrum.setOnColorSelectedListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home)
            finish()
        else
            saveImage(paintView.bitmap)
        return super.onOptionsItemSelected(item)
    }

    private fun saveImage(bitmap: Bitmap) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
            saveBitmap()
        } else {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat
                    .requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        WRITE_EXTERNAL_STORAGE_REQUEST
                    )
            } else {
                showConfirmationDialog()
            }

        }
    }

    private fun showConfirmationDialog() {
         AlertDialog.Builder(this)
            .setTitle("Save Image")
            .setMessage("Do you want to save your work ?")
            .setPositiveButton("Yes", object: DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    saveBitmap()
                    dialog?.dismiss()
                }
            })
            .setNegativeButton("No", object: DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                dialog?.dismiss()
            }
        })
            .create().show()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST && grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            showConfirmationDialog()
        }
    }

    private fun saveBitmap() {
        val bitmap = paintView.bitmap
        val fileName = UUID.randomUUID().toString().plus(".png")
        val folder = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString().plus(File.separator +"Coloring Book") )
        if (!folder.exists()){
            folder.mkdirs()
        }
        runCatching {
            FileOutputStream(folder.toString().plus(File.separator + fileName)).use {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            }
        }
            .onSuccess {
                Toast.makeText(applicationContext, "Image saved correctly", Toast.LENGTH_SHORT).show()
            }
            .onFailure {
                Log.e("error","error while saving image ")
            }
    }

    override fun onColorSelected(color: Int) {
        paintView.selectedColor = color
    }
}