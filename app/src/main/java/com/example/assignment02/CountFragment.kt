package com.example.assignment02

import android.app.Notification
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class CountFragment : Fragment() {

    private lateinit var numberText: TextView
    private lateinit var notification: Notification
    private var currentNumber: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_count, container, false)

        numberText = view.findViewById(R.id.number_text)
        val countButton = view.findViewById<Button>(R.id.count_btn)
        val toastButton = view.findViewById<Button>(R.id.toast_btn)
        val randomButton = view.findViewById<Button>(R.id.random_btn)

        currentNumber = arguments?.getInt("count") ?: 0
        numberText.text = currentNumber.toString()

        countButton.setOnClickListener {
            currentNumber++
            numberText.text = currentNumber.toString()
        }
        toastButton.setOnClickListener {
            showAlertDialog()
        }
        randomButton.setOnClickListener {
            // 숫자 전달을 위한 Intent 생성
            val intent = Intent(context, MainActivity::class.java).apply {
                putExtra("number", currentNumber) // 데이터를 Intent에 추가
                putExtra("Notification", true) // Notification 여부 플래그 추가
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            notification = context?.let { it1 ->
                NotificationCompat.Builder(it1, "assignment-7")
                    .setSmallIcon(android.R.drawable.ic_notification_overlay)
                    .setContentTitle("랜덤 숫자 (0 ~ " + currentNumber + ")")
                    .setContentText("새로운 랜덤 숫자가 생성되었습니다.")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent) // 클릭 시 실행
                    .setAutoCancel(true) // 클릭 후 알림 자동 제거
                    .build()
            }!!

            showNotification()
        }

        return view
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Assignment Notification"
            val descriptionText = "Channel for Assignment notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("assignment-7", name, importance).apply {
                description = descriptionText
            }
            // NotificationManager에 채널 등록
            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // 권한이 허용된 경우 알림 표시
            showNotification()
        } else {
            // 권한이 거부된 경우 처리
            println("Notification permission denied")
        }
    }

    // 권한등을 채크하고 Notification을 보낸다.
    private fun showNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13 이상에서는 POST_NOTIFICATIONS 권한 확인
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                return
            }
        }

        // 권한이 있거나 Android 12 이하인 경우 알림 표시
        NotificationManagerCompat.from(requireContext()).notify(1, notification)
    }

    private fun showAlertDialog() {
        // Fragment에서는 requireContext()를 사용하여 컨텍스트를 가져옴
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("ToastButton")
            .setMessage("초기화/Toast/취소")

            // Positive 버튼 설정
            .setPositiveButton("positive") { dialog, which ->
                currentNumber = 0
                numberText.text = currentNumber.toString()
            }

            // Neutral 버튼 설정
            .setNeutralButton("neutral") { dialog, which ->
                Toast.makeText(requireContext(), "Number: $currentNumber", Toast.LENGTH_SHORT).show()
            }

            // Negative 버튼 설정
            .setNegativeButton("negative") { dialog, which ->
            }

            // 다이얼로그 생성 및 표시
            .create()
            .show()
    }

    companion object {
        fun newInstance(count: Int): CountFragment {
            val fragment = CountFragment()
            val args = Bundle()
            args.putInt("count", count)
            fragment.arguments = args
            return fragment
        }
    }
}