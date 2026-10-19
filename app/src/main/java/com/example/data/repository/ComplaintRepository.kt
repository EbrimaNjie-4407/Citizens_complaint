package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.R
import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class ComplaintRepository(context: Context) {

  private val prefs: SharedPreferences =
    context.getSharedPreferences("citizen_gambia_prefs_v2", Context.MODE_PRIVATE)

  private val _complaints = MutableStateFlow<List<Complaint>>(emptyList())
  val complaints: StateFlow<List<Complaint>> = _complaints.asStateFlow()

  private val _notifications = MutableStateFlow<List<NotificationItem>>(emptyList())
  val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

  private val _userProfile = MutableStateFlow(UserProfile())
  val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

  private val _routingRules = MutableStateFlow<List<InstitutionalRoutingRule>>(emptyList())
  val routingRules: StateFlow<List<InstitutionalRoutingRule>> = _routingRules.asStateFlow()

  private val _draft = MutableStateFlow<ComplaintDraft?>(null)
  val draft: StateFlow<ComplaintDraft?> = _draft.asStateFlow()

  val sectorConditions: List<SectorCondition> = listOf(
    SectorCondition(
      id = "electricity",
      name = "Electricity",
      directorate = "NAWEC Electricity Directorate",
      conditionPercent = 97.5f,
      outputLabel = "Output",
      outputValue = "Peak Load: 98 MW",
      networkLabel = "Network",
      networkValue = "Active Feeders: 18 / 19",
      responseLabel = "Response",
      responseValue = "Avg Outage Fix: 3.2 hrs",
      openReportsCount = 3,
      subStations = listOf(
        "Kotu Power Generation Plant" to "Optimal 98%",
        "Brikama Heavy Fuel Grid" to "Online 97%",
        "Mile 2 Central Incomer" to "Normal",
        "Cape Point Feeder Line" to "Stable",
        "Kairaba Avenue Transmission" to "Operating",
        "Westfield High Voltage Hub" to "Standby Active"
      ),
      uptimeHistory = listOf(96f, 98f, 97f, 99f, 98f, 97.5f)
    ),
    SectorCondition(
      id = "water",
      name = "Water",
      directorate = "NAWEC Water & Sanitation",
      conditionPercent = 89.1f,
      outputLabel = "Output",
      outputValue = "Daily Flow: 104k m³",
      networkLabel = "Network",
      networkValue = "Potability: 99.1%",
      responseLabel = "Response",
      responseValue = "Avg Leak Fix: 4.5 hrs",
      openReportsCount = 3,
      subStations = listOf(
        "Serrekunda Central Reservoir" to "Pressure 92%",
        "Bakau Booster Pumping Hub" to "Operational",
        "Banjul Marine Intake Trunk" to "Pressure Normal",
        "Brikama Deep Well Network" to "Optimal",
        "Fajara Coastal Header Tank" to "Inspection Active"
      ),
      uptimeHistory = listOf(86f, 88f, 85f, 91f, 89f, 89.1f)
    ),
    SectorCondition(
      id = "roads",
      name = "Roads",
      directorate = "National Roads Authority (NRA)",
      conditionPercent = 94.2f,
      outputLabel = "Main Arteries",
      outputValue = "96% Traffic Flow",
      networkLabel = "Drainage",
      networkValue = "88% Desilted",
      responseLabel = "Response",
      responseValue = "Avg Pothole Patch: 24 hrs",
      openReportsCount = 2,
      subStations = listOf(
        "Bertil Harding Highway Expressway" to "100% Free Flow",
        "Banjul-Serrekunda Dual Carriageway" to "Optimal",
        "Sayerr Jobe Commercial Corridor" to "Maintenance Active",
        "Kairaba Avenue Commercial Zone" to "Clear",
        "Sukuta-Brusubi Link Road" to "Pothole Patching"
      ),
      uptimeHistory = listOf(92f, 93f, 95f, 94f, 94.2f)
    ),
    SectorCondition(
      id = "waste",
      name = "Waste Management",
      directorate = "Municipal Waste Operations (KMC / BCC / BAC)",
      conditionPercent = 96.4f,
      outputLabel = "Daily Flow",
      outputValue = "Evacuation: 320 tonnes",
      networkLabel = "Fleet",
      networkValue = "Active Trucks: 26 Units",
      responseLabel = "Response",
      responseValue = "Avg Bin Turnaround: 5.8 hrs",
      openReportsCount = 4,
      subStations = listOf(
        "Serrekunda Market Depo Cordon" to "Evacuation In Progress",
        "Bakau Fish Market Refuse Yard" to "100% Cleared",
        "Banjul Docks Municipal Compactor" to "Standby Available",
        "Brikama Craft & Meat Sheds" to "Daily Shift Active",
        "Latrikunda Sabiji Dumpster Site" to "Container Cleared"
      ),
      uptimeHistory = listOf(93f, 95f, 94f, 97f, 96.4f)
    ),
    SectorCondition(
      id = "gsm",
      name = "GSM & Telecom",
      directorate = "PURA / GSM Operators (Africell, QCell, Comium)",
      conditionPercent = 98.2f,
      outputLabel = "Uptime",
      outputValue = "Tower Availability: 99.4%",
      networkLabel = "Latency",
      networkValue = "Average Delay: 42 ms",
      responseLabel = "Response",
      responseValue = "Avg Signal Fix: 2.1 hrs",
      openReportsCount = 1,
      subStations = listOf(
        "Africell 4G/5G West Coast Macro" to "99.8% Online",
        "QCell Kanifing Core Data Center" to "99.5% Operational",
        "Comium Urban Repeater Net" to "98.2% Normal",
        "Gamtel ACE Subsea Fiber Station" to "Optimal Bandwidth"
      ),
      uptimeHistory = listOf(98f, 99f, 98.5f, 98.2f)
    )
  )

  val infrastructures: List<Infrastructure> = listOf(
    Infrastructure(
      id = "nawec",
      name = "NAWEC",
      fullName = "National Water & Electricity Company",
      categoryTag = "Water",
      description = "Public utility responsible for electricity generation, transmission, and water supply across The Gambia.",
      emergencyPhone = "114",
      logoBadgeColor = 0xFF0D6838,
      logoTextColor = 0xFFFFFFFF
    ),
    Infrastructure(
      id = "nra",
      name = "NRA",
      fullName = "National Roads Authority",
      categoryTag = "Road",
      description = "Government authority overseeing trunk roads, highway maintenance, bridges, and road rehabilitation.",
      emergencyPhone = "123",
      logoBadgeColor = 0xFF1A202C,
      logoTextColor = 0xFFFFFFFF
    ),
    Infrastructure(
      id = "kmc",
      name = "Kanifing Municipal Council",
      fullName = "Kanifing Municipal Council (KMC)",
      categoryTag = "Waste",
      description = "Local municipal government managing sanitation, waste collection, markets, and municipal services in Kanifing municipality.",
      emergencyPhone = "112",
      logoBadgeColor = 0xFF2B6CB0,
      logoTextColor = 0xFFFFFFFF
    ),
    Infrastructure(
      id = "bcc",
      name = "Banjul City Council",
      fullName = "Banjul City Council (BCC)",
      categoryTag = "Waste",
      description = "Municipal governance and city services for the capital city of Banjul.",
      emergencyPhone = "112",
      logoBadgeColor = 0xFF2C7A7B,
      logoTextColor = 0xFFFFFFFF
    ),
    Infrastructure(
      id = "bac",
      name = "Brikama Area Council",
      fullName = "Brikama Area Council (BAC)",
      categoryTag = "Waste",
      description = "Local government area council for West Coast Region public facilities.",
      emergencyPhone = "112",
      logoBadgeColor = 0xFF4A5568,
      logoTextColor = 0xFFFFFFFF
    ),
    Infrastructure(
      id = "comium",
      name = "Comium",
      fullName = "Comium Gambia Ltd",
      categoryTag = "GSM",
      description = "Mobile telecommunications network operator providing cellular voice and data.",
      emergencyPhone = "111",
      logoBadgeColor = 0xFFE53E3E,
      logoTextColor = 0xFFFFFFFF
    ),
    Infrastructure(
      id = "qcell",
      name = "QCell",
      fullName = "QCell Gambia",
      categoryTag = "GSM",
      description = "Gambian-owned telecommunications providing 3G/4G/5G mobile services, fixed broadband, and QMoney.",
      emergencyPhone = "111",
      logoBadgeColor = 0xFFDD6B20,
      logoTextColor = 0xFFFFFFFF
    ),
    Infrastructure(
      id = "africell",
      name = "Africell",
      fullName = "Africell Gambia Ltd",
      categoryTag = "GSM",
      description = "Major telecommunications operator providing nationwide mobile connectivity.",
      emergencyPhone = "111",
      logoBadgeColor = 0xFFC05621,
      logoTextColor = 0xFFFFFFFF
    )
  )

  init {
    loadOrSeedData()
  }

  private fun createDefaultPipelineStages(currentStage: Int): List<ResolutionPipelineStage> {
    return listOf(
      ResolutionPipelineStage(
        stageNumber = 1,
        title = "Report Logged",
        subtext = "Geotagged & Registered",
        time = "10:14 AM",
        isCompleted = currentStage > 1,
        isActive = currentStage == 1
      ),
      ResolutionPipelineStage(
        stageNumber = 2,
        title = "Spatial Triage",
        subtext = "Verified by KMC / BCC Desk",
        time = "10:28 AM",
        isCompleted = currentStage > 2,
        isActive = currentStage == 2
      ),
      ResolutionPipelineStage(
        stageNumber = 3,
        title = "Crew Dispatched",
        subtext = "Compactor Truck #04 En Route",
        time = "10:45 AM",
        isCompleted = currentStage > 3,
        isActive = currentStage == 3
      ),
      ResolutionPipelineStage(
        stageNumber = 4,
        title = "Crews On-Site",
        subtext = "Refuse Evacuation & Containment",
        time = "11:15 AM",
        isCompleted = currentStage > 4,
        isActive = currentStage == 4
      ),
      ResolutionPipelineStage(
        stageNumber = 5,
        title = "Sanitation Restored",
        subtext = "Waste Evacuated & Area Sanitized",
        time = if (currentStage >= 5) "11:50 AM" else "Pending",
        isCompleted = currentStage > 5,
        isActive = currentStage == 5
      ),
      ResolutionPipelineStage(
        stageNumber = 6,
        title = "Certified Audit",
        subtext = "Environmental Health Sign-off",
        time = if (currentStage >= 6) "12:15 PM" else "Pending",
        isCompleted = currentStage >= 6,
        isActive = currentStage == 6
      )
    )
  }

  private fun loadOrSeedData() {
    val savedName = prefs.getString("user_name", "Awa") ?: "Awa"
    val savedPhone = prefs.getString("user_phone", "+220 765 4321") ?: "+220 765 4321"
    val isGuest = prefs.getBoolean("is_guest", false)
    val reportUpdates = prefs.getBoolean("report_updates", true)
    val smsNotifs = prefs.getBoolean("sms_notifications", true)
    val communityAlerts = prefs.getBoolean("community_alerts", false)
    val language = prefs.getString("language", "English") ?: "English"

    _userProfile.value = UserProfile(
      fullName = savedName,
      phoneNumber = savedPhone,
      isGuest = isGuest,
      reportUpdates = reportUpdates,
      smsNotifications = smsNotifs,
      communityAlerts = communityAlerts,
      language = language,
      role = when (prefs.getString("user_role", "CITIZEN")) {
        "INSTITUTION_OFFICER" -> UserRole.INSTITUTION_OFFICER
        "SUPER_ADMIN" -> UserRole.SUPER_ADMIN
        else -> UserRole.CITIZEN
      },
      officerInstitutionId = prefs.getString("officer_institution_id", "kmc") ?: "kmc",
      officerInstitutionName = prefs.getString("officer_institution_name", "Kanifing Municipal Council (KMC)") ?: "Kanifing Municipal Council (KMC)"
    )

    // Load or initialize configurable routing rules
    loadOrInitRoutingRules()

    // Load offline draft if exists
    loadSavedDraft()

    seedInitialComplaints()
    seedInitialNotifications()
  }

  private fun loadOrInitRoutingRules() {
    val initial = InfrastructureCategoryRegistry.createInitialRoutingRules()
    _routingRules.value = initial
  }

  private fun loadSavedDraft() {
    val title = prefs.getString("draft_title", null) ?: return
    _draft.value = ComplaintDraft(
      title = title,
      categoryId = prefs.getString("draft_cat_id", "roads_streets") ?: "roads_streets",
      subcategory = prefs.getString("draft_subcat", "Potholes") ?: "Potholes",
      description = prefs.getString("draft_desc", "") ?: "",
      locationName = prefs.getString("draft_loc_name", "") ?: "",
      locationDetails = prefs.getString("draft_loc_details", "") ?: "",
      region = prefs.getString("draft_region", "Greater Banjul Area") ?: "Greater Banjul Area",
      district = prefs.getString("draft_district", "Kanifing Municipality") ?: "Kanifing Municipality",
      community = prefs.getString("draft_community", "Serrekunda Central") ?: "Serrekunda Central",
      urgency = when (prefs.getString("draft_urgency", "HIGH")) {
        "CRITICAL" -> UrgencyLevel.CRITICAL
        "LOW" -> UrgencyLevel.LOW
        else -> UrgencyLevel.HIGH
      },
      hasPhoto = prefs.getBoolean("draft_has_photo", false),
      hasVoiceNote = prefs.getBoolean("draft_has_voice", false),
      voiceDuration = prefs.getString("draft_voice_dur", "0:12"),
      lastSaved = prefs.getString("draft_last_saved", "") ?: ""
    )
  }

  private fun seedInitialComplaints() {
    val initial = listOf(
      Complaint(
        id = "CG-2026-01482",
        title = "Overflowing waste bins at market",
        category = IssueCategory.WASTE,
        categoryId = "waste_management",
        subcategory = "Overflowing public bins",
        description = "Bins are overflowing beside the main entrance. Waste is spreading onto the walkway near the taxi stand.",
        locationName = "Serrekunda Market",
        locationDetails = "Kanifing, The Gambia · Main entrance",
        region = "Greater Banjul Area (GBA)",
        district = "Kanifing Municipality",
        community = "Serrekunda Central",
        latitude = 13.4398,
        longitude = -16.6789,
        urgency = UrgencyLevel.HIGH,
        status = ComplaintStatus.IN_PROGRESS,
        assignedInstitution = "Kanifing Municipal Council",
        assignedInstitutionId = "kmc",
        assignedOfficer = "Officer Modou Sanyang",
        assignedDate = "12 Jul 2026 · 14:10",
        createdAt = "12 Jul 2026 · 10:24",
        hasPhoto = true,
        photoDrawableRes = R.drawable.waste_evidence_photo_1792407002003,
        hasVoiceNote = true,
        voiceDuration = "0:12",
        currentStageNumber = 4,
        pipelineStages = createDefaultPipelineStages(4),
        authorityMeasures = AuthorityMeasures(
          rapidResponseUnit = "KMC Environmental Health Rapid Squad",
          liaisonUnit = "Kanifing Municipal Council & Fire Service Liaison",
          leadSupervisor = "Supervisor Modou Sanyang",
          supervisorCredentials = "Verified Directorate Credentials",
          diagnosticsStatus = "2026-09-14T22:35:00Z",
          dispatchedTime = "Dispatched: 10:15 PM · Live En Route GPS Beacon",
          equipmentDeployed = listOf(
            "1x Dennis Eagle 18-tonne compactor unit (Truck #04)",
            "4x High-density 1,100L municipal wheelie bins",
            "2x Industrial chlorine & lime disinfection spraying apparatus",
            "6x Certified municipal cleansing officers with PPE gear"
          ),
          actionsTaken = listOf(
            "Site containment barriers deployed to isolate pedestrian flow near taxi stand.",
            "Refuse bulk evacuation completed (approx 3.4 tonnes cleared).",
            "High-pressure water washdown and lime disinfection of sidewalk walkway.",
            "Replacement of 4 cracked municipal wheeled bins with new reinforced 1100L containers.",
            "Post-cleanup verification conducted by Kanifing Municipal Health Inspector."
          ),
          remediatedPhotoRes = R.drawable.remediated_waste_site_1792436266625
        ),
        timeline = listOf(
          TimelineEvent(
            id = "t1",
            status = ComplaintStatus.RECEIVED,
            title = "Received",
            timestamp = "12 Jul 2026 · 10:24",
            description = "Your report was received and a complaint ID was created."
          ),
          TimelineEvent(
            id = "t2",
            status = ComplaintStatus.ASSIGNED,
            title = "Assigned to KMC",
            timestamp = "12 Jul 2026 · 14:10",
            description = "Kanifing Municipal Council accepted responsibility for this waste complaint.",
            institutionName = "Kanifing Municipal Council"
          ),
          TimelineEvent(
            id = "t3",
            status = ComplaintStatus.IN_PROGRESS,
            title = "In progress",
            timestamp = "13 Jul 2026 · 09:15",
            description = "The cleansing team is checking the reported location. Collection is not yet confirmed.",
            institutionName = "KMC Cleansing Services"
          )
        ),
        institutionalUpdate = InstitutionalUpdate(
          quote = "We have notified the cleansing supervisor. The team will assess access to the bins at the market entrance.",
          author = "KMC Cleansing Services · 13 Jul 2026, 09:15"
        )
      ),
      Complaint(
        id = "CG-2026-01490",
        title = "Deep crater pothole obstructing traffic",
        category = IssueCategory.ROADS,
        categoryId = "roads_streets",
        subcategory = "Potholes",
        description = "Large deep pothole on the dual carriageway causing swerving and rim damage during rush hour.",
        locationName = "Kairaba Avenue, Fajara",
        locationDetails = "Near Traffic Lights & Standard Chartered",
        region = "Greater Banjul Area (GBA)",
        district = "Kanifing Municipality",
        community = "Fajara",
        latitude = 13.4560,
        longitude = -16.6800,
        urgency = UrgencyLevel.CRITICAL,
        status = ComplaintStatus.IN_PROGRESS,
        assignedInstitution = "National Roads Authority (NRA)",
        assignedInstitutionId = "nra",
        assignedOfficer = "Eng. Ebrima Cham",
        assignedDate = "10 Jul 2026 · 08:30",
        createdAt = "09 Jul 2026 · 14:20",
        isOverdue = true, // Overdue > 48h case!
        priorityEscalation = "Escalated to CRITICAL: Arterial route blockage",
        currentStageNumber = 3,
        pipelineStages = createDefaultPipelineStages(3),
        authorityMeasures = AuthorityMeasures(
          rapidResponseUnit = "NRA Emergency Highway Maintenance Unit",
          liaisonUnit = "Gambia Police Traffic Division",
          leadSupervisor = "Principal Highway Engineer Ebrima Cham",
          supervisorCredentials = "NRA Civil Engineering Board #410",
          diagnosticsStatus = "Sub-base asphalt core inspection logged",
          dispatchedTime = "Dispatched: 09:30 AM · Traffic diversion active",
          equipmentDeployed = listOf(
            "1x Dynapac vibratory asphalt roller",
            "1x Bitumen emulsion spray tanker",
            "8x Reflective traffic safety cones"
          ),
          actionsTaken = listOf(
            "Milling of fractured edges to sound pavement boundary.",
            "Compacted crushed granite base course applied.",
            "Hot-mix asphalt wearing course scheduled for evening delivery."
          )
        ),
        timeline = listOf(
          TimelineEvent(
            id = "p1",
            status = ComplaintStatus.RECEIVED,
            title = "Received",
            timestamp = "09 Jul 2026 · 14:20",
            description = "Pothole hazard logged on Kairaba Avenue."
          ),
          TimelineEvent(
            id = "p2",
            status = ComplaintStatus.ASSIGNED,
            title = "Assigned to NRA",
            timestamp = "10 Jul 2026 · 08:30",
            description = "Dispatched to National Roads Authority Highway Unit.",
            institutionName = "National Roads Authority (NRA)"
          )
        )
      ),
      Complaint(
        id = "CG-2026-01520",
        title = "Duplicate report: Overflowing bins at Serrekunda",
        category = IssueCategory.WASTE,
        categoryId = "waste_management",
        subcategory = "Overflowing public bins",
        description = "Refuse spilling onto road beside market entrance.",
        locationName = "Serrekunda Market",
        locationDetails = "Main taxi entrance",
        region = "Greater Banjul Area (GBA)",
        district = "Kanifing Municipality",
        community = "Serrekunda Central",
        latitude = 13.4398,
        longitude = -16.6789,
        urgency = UrgencyLevel.HIGH,
        status = ComplaintStatus.RECEIVED,
        assignedInstitution = "Kanifing Municipal Council",
        assignedInstitutionId = "kmc",
        createdAt = "12 Jul 2026 · 11:45",
        isDuplicate = true, // Duplicate report detection!
        duplicateOfId = "CG-2026-01482",
        currentStageNumber = 1,
        pipelineStages = createDefaultPipelineStages(1)
      ),
      Complaint(
        id = "CG-2026-01501",
        title = "Water leak beside the road",
        category = IssueCategory.WATER,
        categoryId = "water_sewerage",
        subcategory = "Burst water main pipes",
        description = "Water leak beside the road causing clean water loss and roadside pooling near residential compound.",
        locationName = "Latrikunda Sabiji, Kanifing",
        locationDetails = "Sayerr Jobe Ave junction",
        region = "Greater Banjul Area (GBA)",
        district = "Kanifing Municipality",
        community = "Latrikunda Sabiji",
        latitude = 13.4320,
        longitude = -16.6850,
        urgency = UrgencyLevel.HIGH,
        status = ComplaintStatus.RECEIVED,
        assignedInstitution = "NAWEC",
        assignedInstitutionId = "nawec",
        assignedDate = "13 Jul 2026 · 11:05",
        createdAt = "13 Jul 2026 · 10:45",
        hasPhoto = false,
        hasVoiceNote = false,
        currentStageNumber = 2,
        pipelineStages = createDefaultPipelineStages(2),
        authorityMeasures = AuthorityMeasures(
          rapidResponseUnit = "NAWEC Rapid Hydraulics Team",
          liaisonUnit = "Ministry of Petroleum & Energy Utility Oversight",
          leadSupervisor = "Engineer Lamin Ceesay",
          supervisorCredentials = "NAWEC Water Networks Level 4",
          diagnosticsStatus = "Pipe burst acoustic scan completed",
          dispatchedTime = "Dispatched: 11:05 AM · Crew En Route",
          equipmentDeployed = listOf(
            "1x Mini-excavator & hydraulic breaker",
            "1x 150mm cast ductile replacement sleeve",
            "2x Submersible de-watering pumps"
          ),
          actionsTaken = listOf(
            "Acoustic listening rod located 150mm underground crack.",
            "Water isolation valve shut off for sector to prevent wastage.",
            "Excavation team preparing replacement coupling."
          )
        ),
        timeline = listOf(
          TimelineEvent(
            id = "w1",
            status = ComplaintStatus.RECEIVED,
            title = "Received",
            timestamp = "13 Jul 2026 · 10:45",
            description = "Your report was received and a complaint ID was created."
          ),
          TimelineEvent(
            id = "w2",
            status = ComplaintStatus.ASSIGNED,
            title = "Referred to NAWEC",
            timestamp = "13 Jul 2026 · 11:05",
            description = "Your water leak report was referred to NAWEC for assessment.",
            institutionName = "NAWEC"
          )
        )
      ),
      Complaint(
        id = "CG-2026-01533",
        title = "Damaged school sanitation facilities",
        category = IssueCategory.HEALTH,
        categoryId = "education",
        subcategory = "Broken school toilets",
        description = "Sanitary block pipe fractured with damaged latrine doors impacting 450 pupils.",
        locationName = "Brikama Box Bar Primary School",
        locationDetails = "Opposite Mini Stadium",
        region = "West Coast Region (WCR)",
        district = "Kombo Central",
        community = "Brikama Box Bar",
        latitude = 13.2710,
        longitude = -16.6490,
        urgency = UrgencyLevel.HIGH,
        status = ComplaintStatus.ASSIGNED,
        assignedInstitution = "Ministry of Basic & Secondary Education (MoBSE)",
        assignedInstitutionId = "mobse",
        assignedOfficer = "Regional Education Officer Musa Bojang",
        assignedDate = "11 Jul 2026 · 14:00",
        createdAt = "11 Jul 2026 · 09:30",
        currentStageNumber = 2,
        pipelineStages = createDefaultPipelineStages(2)
      ),
      Complaint(
        id = "CG-2026-01320",
        title = "Streetlight not working",
        category = IssueCategory.ELECTRICITY,
        categoryId = "public_transport_safety",
        subcategory = "Defective streetlights",
        description = "Pole light fixture flickering and off for several nights, reducing visibility near taxi drop-off point.",
        locationName = "Kairaba Avenue, Fajara",
        locationDetails = "Kairaba Avenue opposite commercial bank",
        region = "Greater Banjul Area (GBA)",
        district = "Kanifing Municipality",
        community = "Fajara",
        latitude = 13.4560,
        longitude = -16.6800,
        urgency = UrgencyLevel.LOW,
        status = ComplaintStatus.RESOLVED,
        assignedInstitution = "NAWEC",
        assignedInstitutionId = "nawec",
        assignedDate = "08 Jul 2026 · 11:30",
        createdAt = "08 Jul 2026 · 09:10",
        hasPhoto = false,
        hasVoiceNote = false,
        currentStageNumber = 6,
        pipelineStages = createDefaultPipelineStages(6),
        authorityMeasures = AuthorityMeasures(
          rapidResponseUnit = "NAWEC Public Lighting Unit",
          liaisonUnit = "Kanifing Municipal Traffic Lighting Desk",
          leadSupervisor = "Senior Linesman Bakary Camara",
          supervisorCredentials = "High-Voltage Certification #8812",
          diagnosticsStatus = "Replaced ballast & LED fixture",
          dispatchedTime = "Dispatched: 08 Jul 2026 · Completed",
          equipmentDeployed = listOf(
            "1x Aerial bucket boom truck",
            "1x 150W Phillips High-Output LED luminaire",
            "1x Digital lux meter tester"
          ),
          actionsTaken = listOf(
            "Disconnected pole breaker and tested terminal feeder voltage.",
            "Removed burnt ballast module and installed IP66 sealed LED luminaire.",
            "Verified 65 lux illumination level across pedestrian crossing.",
            "Passed municipal electrical safety audit."
          )
        ),
        timeline = listOf(
          TimelineEvent(
            id = "e1",
            status = ComplaintStatus.RECEIVED,
            title = "Received",
            timestamp = "08 Jul 2026 · 09:10",
            description = "Your report was received and a complaint ID was created."
          ),
          TimelineEvent(
            id = "e2",
            status = ComplaintStatus.ASSIGNED,
            title = "Assigned to NAWEC",
            timestamp = "08 Jul 2026 · 11:30",
            description = "NAWEC maintenance crew dispatched for inspection."
          ),
          TimelineEvent(
            id = "e3",
            status = ComplaintStatus.IN_PROGRESS,
            title = "In progress",
            timestamp = "09 Jul 2026 · 15:00",
            description = "Bulb and control gear replacement scheduled."
          ),
          TimelineEvent(
            id = "e4",
            status = ComplaintStatus.RESOLVED,
            title = "Resolved",
            timestamp = "10 Jul 2026 · 17:00",
            description = "Streetlight fully repaired and illuminating properly."
          )
        ),
        institutionalUpdate = InstitutionalUpdate(
          quote = "The circuit fixture was repaired by NAWEC street lighting maintenance unit.",
          author = "NAWEC Public Lighting · 10 Jul 2026, 17:00"
        )
      )
    )
    _complaints.value = initial
  }

  private fun seedInitialNotifications() {
    val initial = listOf(
      NotificationItem(
        id = "n1",
        title = "Report referred to NAWEC",
        message = "Your water leak report was referred to NAWEC for assessment.",
        complaintId = "CG-2026-01501",
        timestamp = "13 Jul 2026 · 11:05",
        isRead = false,
        type = NotificationType.REFERRAL
      ),
      NotificationItem(
        id = "n2",
        title = "Your report is in progress",
        message = "KMC’s cleansing team is checking the location at Serrekunda Market.",
        complaintId = "CG-2026-01482",
        timestamp = "13 Jul 2026 · 09:15",
        isRead = false,
        type = NotificationType.PROGRESS
      ),
      NotificationItem(
        id = "n3",
        title = "Assigned to Kanifing Council",
        message = "Kanifing Municipal Council accepted your waste complaint.",
        complaintId = "CG-2026-01482",
        timestamp = "12 Jul 2026 · 14:10",
        isRead = true,
        type = NotificationType.ASSIGNED
      ),
      NotificationItem(
        id = "n4",
        title = "We received your report",
        message = "Your complaint is recorded. Keep your ID to follow updates.",
        complaintId = "CG-2026-01482",
        timestamp = "12 Jul 2026 · 10:24",
        isRead = true,
        type = NotificationType.RECEIVED
      )
    )
    _notifications.value = initial
  }

  fun getComplaintById(id: String): Complaint? {
    val clean = id.trim().uppercase()
    return _complaints.value.find { it.id.equals(clean, ignoreCase = true) }
  }

  fun advancePipelineStage(complaintId: String): Boolean {
    val current = _complaints.value
    val targetIndex = current.indexOfFirst { it.id.equals(complaintId, ignoreCase = true) }
    if (targetIndex == -1) return false

    val target = current[targetIndex]
    val nextStage = if (target.currentStageNumber < 6) target.currentStageNumber + 1 else 6
    val updatedStatus = if (nextStage >= 5) {
      if (nextStage == 6) ComplaintStatus.RESOLVED else ComplaintStatus.IN_PROGRESS
    } else target.status

    val updatedStages = createDefaultPipelineStages(nextStage)
    val now = SimpleDateFormat("dd MMM yyyy · HH:mm", Locale.getDefault()).format(Date())

    val updatedTimeline = target.timeline + TimelineEvent(
      id = UUID.randomUUID().toString(),
      status = updatedStatus,
      title = updatedStages[nextStage - 1].title,
      timestamp = now,
      description = "Field update: ${updatedStages[nextStage - 1].subtext}",
      institutionName = target.assignedInstitution
    )

    val updatedComplaint = target.copy(
      currentStageNumber = nextStage,
      status = updatedStatus,
      pipelineStages = updatedStages,
      timeline = updatedTimeline
    )

    val newList = current.toMutableList()
    newList[targetIndex] = updatedComplaint
    _complaints.value = newList

    // Post notification for user
    val notif = NotificationItem(
      id = UUID.randomUUID().toString(),
      title = "Field Step: ${updatedStages[nextStage - 1].title}",
      message = "Progress updated on ${target.id} — ${updatedStages[nextStage - 1].subtext}",
      complaintId = target.id,
      timestamp = now,
      isRead = false,
      type = NotificationType.PROGRESS
    )
    _notifications.value = listOf(notif) + _notifications.value
    return true
  }

  fun createComplaint(
    title: String,
    category: IssueCategory,
    description: String,
    locationName: String,
    locationDetails: String,
    urgency: UrgencyLevel,
    photoUri: String? = null,
    hasPhoto: Boolean = false,
    hasVoiceNote: Boolean = false,
    voiceDuration: String? = null,
    categoryId: String = "waste_management",
    subcategory: String? = null,
    region: String = "Greater Banjul Area",
    district: String = "Kanifing Municipality",
    community: String = "Serrekunda Central"
  ): Complaint {
    val year = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date())
    val randomNum = (1000..9999).random()
    val id = "CG-$year-0$randomNum"

    val nowFormatted = SimpleDateFormat("dd MMM yyyy · HH:mm", Locale.getDefault()).format(Date())

    // Consult dynamic configurable routing rules
    val matchingRule = _routingRules.value.firstOrNull {
      it.categoryId.equals(categoryId, ignoreCase = true) &&
        (it.subcategoryFilter == null || it.subcategoryFilter.equals(subcategory, ignoreCase = true))
    }

    val instName = matchingRule?.targetInstitutionName ?: when (category) {
      IssueCategory.WASTE -> "Kanifing Municipal Council"
      IssueCategory.WATER -> "NAWEC"
      IssueCategory.ELECTRICITY -> "NAWEC"
      IssueCategory.ROADS -> "National Roads Authority"
      IssueCategory.GSM -> "Telecom Regulator / Operator"
      IssueCategory.HEALTH -> "Ministry of Health / Council"
      IssueCategory.SECURITY -> "Community Police / Watch"
      IssueCategory.OTHER -> "Local Council Administration"
    }

    val instId = matchingRule?.targetInstitutionId ?: when (category) {
      IssueCategory.WASTE -> "kmc"
      IssueCategory.WATER, IssueCategory.ELECTRICITY -> "nawec"
      IssueCategory.ROADS -> "nra"
      IssueCategory.GSM -> "pura"
      IssueCategory.HEALTH -> "moh"
      else -> "mrg"
    }

    val initialTimeline = listOf(
      TimelineEvent(
        id = UUID.randomUUID().toString(),
        status = ComplaintStatus.RECEIVED,
        title = "Received",
        timestamp = nowFormatted,
        description = "Your report was received and routed to $instName under complaint reference $id."
      )
    )

    // Check if duplicate report in same community
    val existingInCommunity = _complaints.value.firstOrNull {
      it.community.equals(community, ignoreCase = true) &&
        (it.subcategory?.equals(subcategory, ignoreCase = true) == true || it.categoryId == categoryId) &&
        it.status != ComplaintStatus.RESOLVED
    }

    val newComplaint = Complaint(
      id = id,
      title = if (title.isNotBlank()) title else "${category.displayName} issue at $locationName",
      category = category,
      categoryId = categoryId,
      subcategory = subcategory ?: category.shortName,
      description = description,
      locationName = locationName,
      locationDetails = locationDetails,
      region = region,
      district = district,
      community = community,
      latitude = 13.4398,
      longitude = -16.6789,
      urgency = urgency,
      status = ComplaintStatus.RECEIVED,
      assignedInstitution = instName,
      assignedInstitutionId = instId,
      assignedDate = null,
      createdAt = nowFormatted,
      hasPhoto = hasPhoto,
      photoUri = photoUri,
      hasVoiceNote = hasVoiceNote,
      voiceDuration = voiceDuration,
      isDuplicate = existingInCommunity != null,
      duplicateOfId = existingInCommunity?.id,
      currentStageNumber = 1,
      pipelineStages = createDefaultPipelineStages(1),
      authorityMeasures = AuthorityMeasures(
        rapidResponseUnit = "$instName Incident Command",
        liaisonUnit = "Citizen Grievance & Dispatch Desk",
        leadSupervisor = "Field Duty Officer",
        supervisorCredentials = "Civic Response Division #GMB",
        diagnosticsStatus = "Initial complaint intake logged and verified",
        dispatchedTime = "Pending team allocation",
        equipmentDeployed = listOf("Standard inspection toolkit", "Digital telemetry terminal"),
        actionsTaken = listOf("Triage assessment logged in municipal register", "Forwarded to district field coordinator")
      ),
      timeline = initialTimeline
    )

    val updatedList = listOf(newComplaint) + _complaints.value
    _complaints.value = updatedList

    val newNotification = NotificationItem(
      id = UUID.randomUUID().toString(),
      title = "We received your report",
      message = "Your complaint $id for ${subcategory ?: category.displayName} was submitted to $instName. Keep your ID to track progress.",
      complaintId = id,
      timestamp = nowFormatted,
      isRead = false,
      type = NotificationType.RECEIVED
    )
    _notifications.value = listOf(newNotification) + _notifications.value

    // Clear saved draft once submitted
    clearDraft()

    return newComplaint
  }

  fun addFeedback(complaintId: String, rating: SatisfactionLevel, comments: String): Boolean {
    val current = _complaints.value
    val targetIndex = current.indexOfFirst { it.id.equals(complaintId, ignoreCase = true) }
    if (targetIndex == -1) return false

    val now = SimpleDateFormat("dd MMM yyyy · HH:mm", Locale.getDefault()).format(Date())
    val newFeedback = ComplaintFeedback(
      rating = rating,
      comments = comments,
      submittedAt = now
    )

    val target = current[targetIndex]
    val updatedFeedbacks = target.feedbacks + newFeedback
    val updatedComplaint = target.copy(feedbacks = updatedFeedbacks)

    val newList = current.toMutableList()
    newList[targetIndex] = updatedComplaint
    _complaints.value = newList
    return true
  }

  fun addReviewRequest(
    complaintId: String,
    reason: String,
    explanation: String,
    extraEvidence: String? = null
  ): Boolean {
    val current = _complaints.value
    val targetIndex = current.indexOfFirst { it.id.equals(complaintId, ignoreCase = true) }
    if (targetIndex == -1) return false

    val now = SimpleDateFormat("dd MMM yyyy · HH:mm", Locale.getDefault()).format(Date())
    val newReview = ReviewRequest(
      reason = reason,
      explanation = explanation,
      extraEvidence = extraEvidence,
      submittedAt = now
    )

    val target = current[targetIndex]
    val updatedReviews = target.reviewRequests + newReview
    val updatedComplaint = target.copy(reviewRequests = updatedReviews)

    val newList = current.toMutableList()
    newList[targetIndex] = updatedComplaint
    _complaints.value = newList

    val notif = NotificationItem(
      id = UUID.randomUUID().toString(),
      title = "Review requested for $complaintId",
      message = "Your request for review regarding \"$reason\" has been forwarded to the civic review team.",
      complaintId = complaintId,
      timestamp = now,
      isRead = false,
      type = NotificationType.PROGRESS
    )
    _notifications.value = listOf(notif) + _notifications.value

    return true
  }

  fun markNotificationRead(id: String) {
    val current = _notifications.value
    _notifications.value = current.map {
      if (it.id == id) it.copy(isRead = true) else it
    }
  }

  fun markAllNotificationsRead() {
    val current = _notifications.value
    _notifications.value = current.map { it.copy(isRead = true) }
  }

  fun updateUserProfile(
    fullName: String,
    phoneNumber: String,
    isGuest: Boolean,
    reportUpdates: Boolean,
    smsNotifications: Boolean,
    communityAlerts: Boolean,
    language: String
  ) {
    val profile = UserProfile(
      fullName = fullName,
      phoneNumber = phoneNumber,
      isGuest = isGuest,
      reportUpdates = reportUpdates,
      smsNotifications = smsNotifications,
      communityAlerts = communityAlerts,
      language = language,
      role = _userProfile.value.role,
      officerInstitutionId = _userProfile.value.officerInstitutionId,
      officerInstitutionName = _userProfile.value.officerInstitutionName
    )
    _userProfile.value = profile
    prefs.edit()
      .putString("user_name", fullName)
      .putString("user_phone", phoneNumber)
      .putBoolean("is_guest", isGuest)
      .putBoolean("report_updates", reportUpdates)
      .putBoolean("sms_notifications", smsNotifications)
      .putBoolean("community_alerts", communityAlerts)
      .putString("language", language)
      .apply()
  }

  fun updateUserRole(role: UserRole, officerInstitutionId: String? = null, officerInstitutionName: String? = null) {
    val current = _userProfile.value
    val updated = current.copy(
      role = role,
      officerInstitutionId = officerInstitutionId ?: current.officerInstitutionId,
      officerInstitutionName = officerInstitutionName ?: current.officerInstitutionName
    )
    _userProfile.value = updated
    prefs.edit()
      .putString("user_role", role.name)
      .putString("officer_institution_id", updated.officerInstitutionId)
      .putString("officer_institution_name", updated.officerInstitutionName)
      .apply()
  }

  // --- Administrative and Institutional Workflows ---

  data class AdminKPIStats(
    val totalReports: Int,
    val pendingReview: Int,
    val inProgress: Int,
    val resolved: Int,
    val overdueCount: Int,
    val duplicateCount: Int,
    val resolutionRatePercent: Int
  )

  fun getAdminKPIs(): AdminKPIStats {
    val list = _complaints.value
    val total = list.size
    val pending = list.count { it.status == ComplaintStatus.RECEIVED }
    val inProg = list.count { it.status == ComplaintStatus.ASSIGNED || it.status == ComplaintStatus.IN_PROGRESS }
    val resolved = list.count { it.status == ComplaintStatus.RESOLVED }
    val overdue = list.count { it.isOverdue }
    val duplicates = list.count { it.isDuplicate }
    val rate = if (total > 0) (resolved * 100) / total else 0
    return AdminKPIStats(
      totalReports = total,
      pendingReview = pending,
      inProgress = inProg,
      resolved = resolved,
      overdueCount = overdue,
      duplicateCount = duplicates,
      resolutionRatePercent = rate
    )
  }

  fun assignInstitution(complaintId: String, institutionId: String, institutionName: String, officerName: String?): Boolean {
    val current = _complaints.value
    val idx = current.indexOfFirst { it.id.equals(complaintId, ignoreCase = true) }
    if (idx == -1) return false
    val now = SimpleDateFormat("dd MMM yyyy · HH:mm", Locale.getDefault()).format(Date())
    val target = current[idx]
    val updatedTimeline = target.timeline + TimelineEvent(
      id = UUID.randomUUID().toString(),
      status = ComplaintStatus.ASSIGNED,
      title = "Assigned to $institutionName",
      timestamp = now,
      description = "Administrative routing updated. Designated officer: ${officerName ?: "Field Operations Unit"}.",
      institutionName = institutionName,
      officerName = officerName
    )
    val updated = target.copy(
      assignedInstitution = institutionName,
      assignedInstitutionId = institutionId,
      assignedOfficer = officerName,
      assignedDate = now,
      status = if (target.status == ComplaintStatus.RECEIVED) ComplaintStatus.ASSIGNED else target.status,
      timeline = updatedTimeline
    )
    val list = current.toMutableList()
    list[idx] = updated
    _complaints.value = list

    val notif = NotificationItem(
      id = UUID.randomUUID().toString(),
      title = "Assigned to $institutionName",
      message = "Your report ${target.id} was routed to $institutionName for field action.",
      complaintId = target.id,
      timestamp = now,
      isRead = false,
      type = NotificationType.ASSIGNED
    )
    _notifications.value = listOf(notif) + _notifications.value
    return true
  }

  fun escalateSeverity(complaintId: String, newUrgency: UrgencyLevel, reason: String): Boolean {
    val current = _complaints.value
    val idx = current.indexOfFirst { it.id.equals(complaintId, ignoreCase = true) }
    if (idx == -1) return false
    val now = SimpleDateFormat("dd MMM yyyy · HH:mm", Locale.getDefault()).format(Date())
    val target = current[idx]
    val updatedTimeline = target.timeline + TimelineEvent(
      id = UUID.randomUUID().toString(),
      status = target.status,
      title = "Severity Escalated to ${newUrgency.name}",
      timestamp = now,
      description = "Urgency escalated by administrative command: $reason"
    )
    val updated = target.copy(
      urgency = newUrgency,
      priorityEscalation = "Escalated to ${newUrgency.name}: $reason",
      timeline = updatedTimeline
    )
    val list = current.toMutableList()
    list[idx] = updated
    _complaints.value = list
    return true
  }

  fun flagDuplicate(complaintId: String, duplicateOfId: String): Boolean {
    val current = _complaints.value
    val idx = current.indexOfFirst { it.id.equals(complaintId, ignoreCase = true) }
    if (idx == -1) return false
    val target = current[idx]
    val updated = target.copy(
      isDuplicate = true,
      duplicateOfId = duplicateOfId
    )
    val list = current.toMutableList()
    list[idx] = updated
    _complaints.value = list
    return true
  }

  fun postInstitutionalUpdate(complaintId: String, quote: String, author: String): Boolean {
    val current = _complaints.value
    val idx = current.indexOfFirst { it.id.equals(complaintId, ignoreCase = true) }
    if (idx == -1) return false
    val target = current[idx]
    val updated = target.copy(
      institutionalUpdate = InstitutionalUpdate(quote = quote, author = author)
    )
    val list = current.toMutableList()
    list[idx] = updated
    _complaints.value = list
    return true
  }

  fun resolveWithEvidence(
    complaintId: String,
    resolutionNotes: String,
    completedActions: List<String>,
    deployedEquipment: List<String>
  ): Boolean {
    val current = _complaints.value
    val idx = current.indexOfFirst { it.id.equals(complaintId, ignoreCase = true) }
    if (idx == -1) return false
    val target = current[idx]
    val now = SimpleDateFormat("dd MMM yyyy · HH:mm", Locale.getDefault()).format(Date())
    val stages = createDefaultPipelineStages(6)
    val existingMeasures = target.authorityMeasures ?: AuthorityMeasures()
    val updatedMeasures = existingMeasures.copy(
      actionsTaken = if (completedActions.isNotEmpty()) completedActions else existingMeasures.actionsTaken,
      equipmentDeployed = if (deployedEquipment.isNotEmpty()) deployedEquipment else existingMeasures.equipmentDeployed,
      verifiedResolutionNotes = resolutionNotes
    )
    val updatedTimeline = target.timeline + TimelineEvent(
      id = UUID.randomUUID().toString(),
      status = ComplaintStatus.RESOLVED,
      title = "Resolved & Verified",
      timestamp = now,
      description = "Resolution verified by municipal supervisor. $resolutionNotes",
      institutionName = target.assignedInstitution
    )
    val updated = target.copy(
      status = ComplaintStatus.RESOLVED,
      currentStageNumber = 6,
      pipelineStages = stages,
      authorityMeasures = updatedMeasures,
      resolutionEvidenceNotes = resolutionNotes,
      timeline = updatedTimeline,
      isOverdue = false
    )
    val list = current.toMutableList()
    list[idx] = updated
    _complaints.value = list

    val notif = NotificationItem(
      id = UUID.randomUUID().toString(),
      title = "Complaint Resolved: ${target.id}",
      message = "Your reported issue at ${target.locationName} has been officially resolved and verified.",
      complaintId = target.id,
      timestamp = now,
      isRead = false,
      type = NotificationType.PROGRESS
    )
    _notifications.value = listOf(notif) + _notifications.value
    return true
  }

  fun getRecurringInfrastructureHistory(community: String, categoryId: String): List<Complaint> {
    return _complaints.value.filter {
      (it.community.equals(community, ignoreCase = true) || it.locationName.contains(community, ignoreCase = true)) &&
        (it.categoryId.equals(categoryId, ignoreCase = true) || categoryId.isBlank())
    }
  }

  // --- Offline Draft Storage ---

  fun saveDraft(draft: ComplaintDraft) {
    _draft.value = draft
    val now = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()).format(Date())
    val updatedDraft = draft.copy(lastSaved = now)
    _draft.value = updatedDraft
    prefs.edit()
      .putString("draft_title", updatedDraft.title)
      .putString("draft_cat_id", updatedDraft.categoryId)
      .putString("draft_subcat", updatedDraft.subcategory)
      .putString("draft_desc", updatedDraft.description)
      .putString("draft_loc_name", updatedDraft.locationName)
      .putString("draft_loc_details", updatedDraft.locationDetails)
      .putString("draft_region", updatedDraft.region)
      .putString("draft_district", updatedDraft.district)
      .putString("draft_community", updatedDraft.community)
      .putString("draft_urgency", updatedDraft.urgency.name)
      .putBoolean("draft_has_photo", updatedDraft.hasPhoto)
      .putBoolean("draft_has_voice", updatedDraft.hasVoiceNote)
      .putString("draft_voice_dur", updatedDraft.voiceDuration)
      .putString("draft_last_saved", now)
      .apply()
  }

  fun clearDraft() {
    _draft.value = null
    prefs.edit()
      .remove("draft_title")
      .remove("draft_cat_id")
      .remove("draft_subcat")
      .remove("draft_desc")
      .remove("draft_loc_name")
      .remove("draft_loc_details")
      .remove("draft_region")
      .remove("draft_district")
      .remove("draft_community")
      .remove("draft_urgency")
      .remove("draft_has_photo")
      .remove("draft_has_voice")
      .remove("draft_voice_dur")
      .remove("draft_last_saved")
      .apply()
  }

  // --- Configurable Institutional Routing Rules ---

  fun updateRoutingRule(rule: InstitutionalRoutingRule) {
    val current = _routingRules.value
    val idx = current.indexOfFirst { it.id == rule.id || it.categoryId == rule.categoryId }
    val updated = if (idx != -1) {
      val mutable = current.toMutableList()
      mutable[idx] = rule
      mutable
    } else {
      current + rule
    }
    _routingRules.value = updated
  }

  fun resetRoutingRules() {
    _routingRules.value = InfrastructureCategoryRegistry.createInitialRoutingRules()
  }
}
