package com.example.dishapp.view.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.dishapp.R
import com.example.dishapp.application.DishApplication
import com.example.dishapp.databinding.ActivityAddUpdateDishBinding
import com.example.dishapp.databinding.DialogCustomImageSelectionBinding
import com.example.dishapp.databinding.DialogCustomListBinding
import com.example.dishapp.model.entities.Dish
import com.example.dishapp.utils.*
import com.example.dishapp.view.adapters.CustomListItemAdapter
import com.example.dishapp.viewmodel.DishViewModel
import com.example.dishapp.viewmodel.DishViewModelFactory
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class AddUpdateDishActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mBinding: ActivityAddUpdateDishBinding
    private var mImagePath: String = ""
    private lateinit var mCustomListDialog: Dialog
    private val mDishViewModel: DishViewModel by viewModels {
        DishViewModelFactory((application as DishApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddUpdateDishBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setUpActionBar()
        mBinding.run {
            ivAddDishImage.setOnClickListener(this@AddUpdateDishActivity)
            etType.setOnClickListener(this@AddUpdateDishActivity)
            etCategory.setOnClickListener(this@AddUpdateDishActivity)
            etCookingTime.setOnClickListener(this@AddUpdateDishActivity)
            btnAddDish.setOnClickListener(this@AddUpdateDishActivity)
        }
    }

    private fun setUpActionBar() {
        setSupportActionBar(mBinding.updateAddDishToolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        mBinding.updateAddDishToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_add_dish_image -> {
                customImageSelectionDialog()
                return
            }
            R.id.et_type -> {
                customItemDialog(
                    getString(R.string.title_select_dish_type),
                    dishTypes(this),
                    DISH_TYPE
                )
                return
            }
            R.id.et_category -> {
                customItemDialog(
                    getString(R.string.title_select_dish_category),
                    dishCategories(this),
                    DISH_CATEGORY
                )
                return
            }
            R.id.et_cooking_time -> {
                customItemDialog(
                    getString(R.string.title_select_dish_cooking_time),
                    dishCookingTime(this),
                    DISH_COOKING_TIME
                )
                return
            }
            R.id.btn_add_dish -> {
                val title = trimEmptySpaces(mBinding.etTitle.text.toString())
                val type = trimEmptySpaces(mBinding.etType.text.toString())
                val category = trimEmptySpaces(mBinding.etCategory.text.toString())
                val ingredients = trimEmptySpaces(mBinding.etIngredients.text.toString())
                val cookingTime = trimEmptySpaces(mBinding.etCookingTime.text.toString())
                val directions = trimEmptySpaces(mBinding.etDirectionToCook.text.toString())

                when {
                    TextUtils.isEmpty(mImagePath) -> {
                        Toast.makeText(
                            this,
                            resources.getString(R.string.err_msg_select_dish_image),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    TextUtils.isEmpty(title) -> {
                        Toast.makeText(
                            this@AddUpdateDishActivity,
                            resources.getString(R.string.err_msg_enter_dish_title),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    TextUtils.isEmpty(type) -> {
                        Toast.makeText(
                            this@AddUpdateDishActivity,
                            resources.getString(R.string.err_msg_select_dish_type),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    TextUtils.isEmpty(category) -> {
                        Toast.makeText(
                            this@AddUpdateDishActivity,
                            resources.getString(R.string.err_msg_select_dish_category),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    TextUtils.isEmpty(ingredients) -> {
                        Toast.makeText(
                            this@AddUpdateDishActivity,
                            resources.getString(R.string.err_msg_enter_dish_ingredients),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    TextUtils.isEmpty(cookingTime) -> {
                        Toast.makeText(
                            this@AddUpdateDishActivity,
                            resources.getString(R.string.err_msg_select_dish_cooking_time),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    TextUtils.isEmpty(directions) -> {
                        Toast.makeText(
                            this@AddUpdateDishActivity,
                            resources.getString(R.string.err_msg_enter_dish_cooking_instructions),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {
                        val dishDetails: Dish = Dish(
                            image = mImagePath,
                            imageSource = DISH_IMAGE_SOURCE_LOCAL,
                            title = title,
                            type = type,
                            category = category,
                            ingredients = ingredients,
                            cookingTime = cookingTime,
                            directionsToCook = directions,
                            favoriteDish = false
                        )
                        mDishViewModel.insert(dishDetails)
                        Toast.makeText(
                            this,
                            "You successfully added your favorite dish details",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.i("Insertion", "Success ${mDishViewModel.allDishesList.value?.size}")
                        finish()
                    }
                }
            }
        }
    }

    private fun trimEmptySpaces(text: String): String {
        return text.trim { it <= ' ' }
    }

    private fun customImageSelectionDialog() {
        val dialog = Dialog(this)
        val binding: DialogCustomImageSelectionBinding =
            DialogCustomImageSelectionBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(
            AppCompatResources.getDrawable(
                this,
                R.drawable.dialog_background
            )
        )
        binding.tvCamera.setOnClickListener {
            Dexter.withContext(this)
                .withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if (it.areAllPermissionsGranted()) {
                                cameraActivity.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
                            } else {
                                showRationalDialogForPermissions()
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        showRationalDialogForPermissions()
                    }

                })
                .onSameThread()
                .check()
            dialog.dismiss()
        }
        binding.tvGallery.setOnClickListener {
            Dexter.withContext(this)
                .withPermission(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        galleryActivity.launch(
                            Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            )
                        )
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        Toast.makeText(
                            this@AddUpdateDishActivity,
                            "You denied gallery permission",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: PermissionRequest?,
                        p1: PermissionToken?
                    ) {
                        showRationalDialogForPermissions()
                    }

                })
                .onSameThread()
                .check()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(this)
            .setMessage(R.string.add_image_dialog_no_permission)
            .setPositiveButton(R.string.add_image_dialog_positive_btn) { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton(R.string.add_image_dialog_negative_btn) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private val cameraActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    result.data?.let {
                        val thumbnail: Bitmap? = it.extras?.get(DATA_KEY) as? Bitmap
                        Glide.with(this)
                            .load(thumbnail)
                            .centerCrop()
                            .into(mBinding.ivDishImage)

                        mImagePath = saveImageToInternalStorage(thumbnail)
                        Log.i("imagePath", mImagePath)
                        mBinding.ivAddDishImage.setImageDrawable(
                            AppCompatResources.getDrawable(
                                this@AddUpdateDishActivity,
                                R.drawable.ic_edit
                            )
                        )
                    }
                }
                Activity.RESULT_CANCELED -> {
                    Log.e("cancelled", "User cancelled camera capture")
                }
            }
        }

    private val galleryActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    result.data?.let {
                        val selectedPhotoUri = it.data
                        Glide.with(this)
                            .load(selectedPhotoUri)
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .listener(object : RequestListener<Drawable> {
                                override fun onLoadFailed(
                                    e: GlideException?,
                                    model: Any?,
                                    target: Target<Drawable>?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    Log.e("TAG", "Error loading image", e)
                                    return false
                                }

                                override fun onResourceReady(
                                    resource: Drawable?,
                                    model: Any?,
                                    target: Target<Drawable>?,
                                    dataSource: DataSource?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    resource?.let { drawable ->
                                        val bitmap = drawable.toBitmap()
                                        mImagePath = saveImageToInternalStorage(bitmap)
                                        Log.i("imagePath", mImagePath)
                                    }
                                    return false
                                }

                            })
                            .into(mBinding.ivDishImage)
                        mBinding.ivAddDishImage.setImageDrawable(
                            AppCompatResources.getDrawable(
                                this@AddUpdateDishActivity,
                                R.drawable.ic_edit
                            )
                        )

                    }
                }
                Activity.RESULT_CANCELED -> {
                    Log.e("cancelled", "User cancelled image selection")
                }
            }
        }

    private fun saveImageToInternalStorage(bitmap: Bitmap?): String {
        val wrapper = ContextWrapper(applicationContext)
        val path = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        val file = File(path, "${UUID.randomUUID()}.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file.absolutePath
    }

    private fun customItemDialog(title: String, itemsList: ArrayList<String>, selection: String) {
        mCustomListDialog = Dialog(this)
        val binding: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)
        mCustomListDialog.setContentView(binding.root)
        binding.tvTitle.text = title
        binding.rvList.run {
            layoutManager = LinearLayoutManager(this@AddUpdateDishActivity)
            adapter = CustomListItemAdapter(this@AddUpdateDishActivity, itemsList, selection)
        }
        mCustomListDialog.show()
    }

    fun selectedListItem(item: String, selection: String) {
        mCustomListDialog.dismiss()
        when (selection) {
            DISH_TYPE -> {
                mBinding.etType.setText(item)
            }
            DISH_CATEGORY -> {
                mBinding.etCategory.setText(item)
            }
            DISH_COOKING_TIME -> {
                mBinding.etCookingTime.setText(item)
            }
        }
    }

    companion object {
        const val IMAGE_DIRECTORY = "DishAppImages"
    }
}