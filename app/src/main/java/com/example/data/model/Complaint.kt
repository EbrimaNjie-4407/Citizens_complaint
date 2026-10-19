package com.example.data.model

enum class IssueCategory(val displayName: String, val shortName: String) {
  WASTE("Waste & Sanitation", "Waste"),
  ROADS("Roads & Paths", "Roads"),
  WATER("Water Supply", "Water"),
  ELECTRICITY("Electricity", "Electricity"),
  HEALTH("Health & Safety", "Health"),
  GSM("GSM / Telecom", "GSM"),
  SECURITY("Security", "Security"),
  OTHER("Other", "Other")
}

data class InfrastructureCategory(
  val id: String,
  val name: String,
  val groupName: String,
  val subcategories: List<String>,
  val defaultInstitutionId: String,
  val defaultInstitutionName: String,
  val iconKey: String,
  val colorHex: Long
)

data class InstitutionalRoutingRule(
  val id: String,
  val categoryId: String,
  val categoryName: String,
  val subcategoryFilter: String? = null, // null means all subcategories in this category
  val targetInstitutionId: String,
  val targetInstitutionName: String,
  val prioritySlaHours: Int = 48,
  val autoEscalateUrgent: Boolean = true,
  val notificationEmail: String = "reports@gambia.gov.gm"
)

data class GambianRegion(
  val id: String,
  val name: String,
  val districts: List<GambianDistrict>
)

data class GambianDistrict(
  val id: String,
  val name: String,
  val communities: List<String>
)

enum class UrgencyLevel(val title: String, val subtitle: String) {
  LOW("LOW", "Cosmic/Minor"),
  HIGH("HIGH", "Active hazard"),
  CRITICAL("CRITICAL", "URGENT")
}

enum class ComplaintStatus(val label: String) {
  RECEIVED("Received"),
  ASSIGNED("Assigned"),
  IN_PROGRESS("In progress"),
  RESOLVED("Resolved")
}

enum class SatisfactionLevel(val label: String) {
  NOT_SATISFIED("Not satisfied"),
  SOMEWHAT_SATISFIED("Somewhat satisfied"),
  SATISFIED("Satisfied")
}

data class TimelineEvent(
  val id: String,
  val status: ComplaintStatus,
  val title: String,
  val timestamp: String,
  val description: String,
  val institutionName: String? = null,
  val officerName: String? = null
)

data class InstitutionalUpdate(
  val quote: String,
  val author: String
)

data class ComplaintFeedback(
  val rating: SatisfactionLevel,
  val comments: String,
  val submittedAt: String
)

data class ReviewRequest(
  val reason: String,
  val explanation: String,
  val extraEvidence: String? = null,
  val submittedAt: String
)

data class ResolutionPipelineStage(
  val stageNumber: Int,
  val title: String,
  val subtext: String,
  val time: String,
  val isCompleted: Boolean,
  val isActive: Boolean
)

data class AuthorityMeasures(
  val rapidResponseUnit: String = "KMC Environmental Health Rapid Squad",
  val liaisonUnit: String = "Kanifing Municipal Council & Fire Service Liaison",
  val leadSupervisor: String = "Supervisor Modou Sanyang",
  val supervisorCredentials: String = "Verified Directorate Credentials",
  val diagnosticsStatus: String = "2026-09-14T22:35:00Z",
  val dispatchedTime: String = "Dispatched: 10:15 PM · Live Beacon Active",
  val equipmentDeployed: List<String> = listOf(
    "1x Dennis Eagle 18-tonne heavy compactor unit (Truck #04)",
    "4x High-density 1,100L municipal wheelie bins",
    "2x Industrial chlorine & lime disinfection spraying apparatus",
    "6x Certified municipal cleansing officers with PPE gear"
  ),
  val actionsTaken: List<String> = listOf(
    "Safety cordon erected around taxi stand entrance to redirect pedestrians.",
    "Evacuated 3.4 tonnes of compacted organic and market refuse.",
    "High-pressure washdown and alkaline decontamination applied to walkway.",
    "Replaced 4 fractured waste containers with reinforced galvanized units.",
    "Instituted bi-daily scheduled collection frequency for Serrekunda Market."
  ),
  val remediatedPhotoRes: Int? = null,
  val verifiedResolutionNotes: String? = null
)

data class ComplaintDraft(
  val title: String = "",
  val categoryId: String = "roads_streets",
  val subcategory: String = "Potholes",
  val description: String = "",
  val locationName: String = "",
  val locationDetails: String = "",
  val region: String = "Greater Banjul Area",
  val district: String = "Kanifing Municipality",
  val community: String = "Serrekunda Central",
  val urgency: UrgencyLevel = UrgencyLevel.HIGH,
  val hasPhoto: Boolean = false,
  val photoUri: String? = null,
  val hasVoiceNote: Boolean = false,
  val voiceDuration: String? = null,
  val lastSaved: String = ""
)

data class Complaint(
  val id: String,
  val title: String,
  val category: IssueCategory,
  val categoryId: String = "waste_management",
  val subcategory: String? = null,
  val description: String,
  val locationName: String,
  val locationDetails: String,
  val region: String = "Greater Banjul Area",
  val district: String = "Kanifing Municipality",
  val community: String = "Serrekunda Central",
  val latitude: Double? = 13.4398,
  val longitude: Double? = -16.6789,
  val urgency: UrgencyLevel,
  val status: ComplaintStatus,
  val assignedInstitution: String? = null,
  val assignedInstitutionId: String? = null,
  val assignedOfficer: String? = null,
  val assignedDate: String? = null,
  val createdAt: String,
  val hasPhoto: Boolean = false,
  val photoDrawableRes: Int? = null,
  val photoUri: String? = null,
  val hasVoiceNote: Boolean = false,
  val voiceDuration: String? = null,
  val isOverdue: Boolean = false,
  val isDuplicate: Boolean = false,
  val duplicateOfId: String? = null,
  val priorityEscalation: String? = null,
  val timeline: List<TimelineEvent> = emptyList(),
  val institutionalUpdate: InstitutionalUpdate? = null,
  val feedbacks: List<ComplaintFeedback> = emptyList(),
  val reviewRequests: List<ReviewRequest> = emptyList(),
  val currentStageNumber: Int = 4,
  val pipelineStages: List<ResolutionPipelineStage> = emptyList(),
  val authorityMeasures: AuthorityMeasures? = null,
  val resolutionEvidenceNotes: String? = null
)

data class SectorCondition(
  val id: String,
  val name: String,
  val directorate: String,
  val conditionPercent: Float,
  val outputLabel: String,
  val outputValue: String,
  val networkLabel: String,
  val networkValue: String,
  val responseLabel: String,
  val responseValue: String,
  val openReportsCount: Int,
  val subStations: List<Pair<String, String>>,
  val uptimeHistory: List<Float>
)
