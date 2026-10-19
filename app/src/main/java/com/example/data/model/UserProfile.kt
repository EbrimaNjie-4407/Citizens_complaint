package com.example.data.model

enum class UserRole(val displayName: String) {
  CITIZEN("Citizen"),
  INSTITUTION_OFFICER("Institutional Officer"),
  SUPER_ADMIN("Super-Admin")
}

data class UserProfile(
  val fullName: String = "Awa",
  val phoneNumber: String = "+220 765 4321",
  val isGuest: Boolean = false,
  val reportUpdates: Boolean = true,
  val smsNotifications: Boolean = true,
  val communityAlerts: Boolean = false,
  val language: String = "English",
  val role: UserRole = UserRole.CITIZEN,
  val officerInstitutionId: String = "kmc",
  val officerInstitutionName: String = "Kanifing Municipal Council (KMC)"
)

data class Infrastructure(
  val id: String,
  val name: String,
  val fullName: String,
  val categoryTag: String, // "Water", "Road", "Waste", "GSM", "Electricity", "Environment", "Health", "Education", etc.
  val description: String,
  val emergencyPhone: String,
  val logoBadgeColor: Long,
  val logoTextColor: Long
)
