package cn.zzs.xiaoai.face.bean


const val TARGET_FACE = "face"

const val TARGET_QR_CODE = "qr_code"

data class Message(val target: String, val content: String)