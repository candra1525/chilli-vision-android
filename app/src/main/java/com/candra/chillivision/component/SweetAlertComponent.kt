package com.candra.chillivision.component

import android.content.Context
import androidx.compose.runtime.Composable
import cn.pedant.SweetAlert.SweetAlertDialog

fun SweetAlertComponent(
    context: Context,
    title: String,
    contentText: String,
    type: String,
    isCancel: Boolean = false,
    confirmYes: () -> Unit = {},
) {
    when (type) {
        "error" -> {
            val sweetAlertDialog =
                SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(title)
                    .setContentText(contentText)
                    .setConfirmButton("OK") {
                        it.dismissWithAnimation()
                    }
            sweetAlertDialog.setCancelable(isCancel)
            sweetAlertDialog.show()
        }

        "success" -> {
            val sweetAlertDialog =
                SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(title)
                    .setContentText(contentText)
                    .setConfirmButton("OK") {
                        it.dismissWithAnimation()
                    }
            sweetAlertDialog.setCancelable(isCancel)
            sweetAlertDialog.show()
        }

        "warning" -> {
            val sweetAlertDialog =
                SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(title)
                    .setContentText(contentText)
                    .setConfirmButton("OK") {
                        it.dismissWithAnimation()
                    }
            sweetAlertDialog.setCancelable(isCancel)
            sweetAlertDialog.show()
        }

        "info" -> {
            val sweetAlertDialog =
                SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
                    .setTitleText(title)
                    .setContentText(contentText)
                    .setConfirmButton("OK") {
                        it.dismissWithAnimation()
                    }
            sweetAlertDialog.setCancelable(isCancel)
            sweetAlertDialog.show()
        }

        "logout" -> {
            val sweetAlertDialog =
                SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(title)
                    .setContentText(contentText)
                    .setConfirmButton("Ya") {
                        confirmYes()
                        it.dismissWithAnimation()
                    }
                    .setCancelButton("Tidak") {
                        it.dismissWithAnimation()
                    }
            sweetAlertDialog.setCancelable(isCancel)
            sweetAlertDialog.show()
        }

        "perhatian" -> {
            val sweetAlertDialog =
                SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(title)
                    .setContentText(contentText)
                    .setConfirmButton("Ya") {
                        confirmYes()
                        it.dismissWithAnimation()
                    }
                    .setCancelButton("Tidak") {
                        it.dismissWithAnimation()
                    }
            sweetAlertDialog.setCancelable(isCancel)
            sweetAlertDialog.show()
        }

    }
}