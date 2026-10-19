package com.example.data.model

enum class NotificationType {
  REFERRAL,
  PROGRESS,
  ASSIGNED,
  RECEIVED
}

data class NotificationItem(
  val id: String,
  val title: String,
  val message: String,
  val complaintId: String,
  val timestamp: String,
  val isRead: Boolean = false,
  val type: NotificationType = NotificationType.RECEIVED
)
