package com.example.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.*
import com.example.ui.components.VoiceNotePlayer
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun QuickReportScreen(
  initialCategory: IssueCategory? = IssueCategory.WASTE,
  initialCategoryId: String = "waste_management",
  initialSubcategory: String? = "Overflowing public bins",
  currentLocationName: String = "Serrekunda Market",
  currentLocationDetails: String = "Kanifing, The Gambia · GPS detected",
  savedDraft: ComplaintDraft? = null,
  onBackClick: () -> Unit,
  onOpenHotline: () -> Unit,
  onChangeLocation: () -> Unit,
  onSaveDraft: (ComplaintDraft) -> Unit = {},
  onClearDraft: () -> Unit = {},
  onReviewAndSubmit: (
    title: String,
    category: IssueCategory,
    description: String,
    locationName: String,
    locationDetails: String,
    urgency: UrgencyLevel,
    hasPhoto: Boolean,
    photoUri: String?,
    hasVoiceNote: Boolean,
    voiceDuration: String?,
    categoryId: String,
    subcategory: String?,
    region: String,
    district: String,
    community: String
  ) -> Unit
) {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()

  // Selected Category ID among 22
  var selectedCategoryId by remember { mutableStateOf(initialCategoryId) }
  val currentCategoryObj = remember(selectedCategoryId) {
    InfrastructureCategoryRegistry.allCategories.find { it.id == selectedCategoryId }
      ?: InfrastructureCategoryRegistry.allCategories.first()
  }

  // Selected Subcategory
  var selectedSubcategory by remember {
    mutableStateOf(initialSubcategory ?: currentCategoryObj.subcategories.firstOrNull() ?: "General Issue")
  }

  // When category changes, update default subcategory
  LaunchedEffect(selectedCategoryId) {
    if (!currentCategoryObj.subcategories.contains(selectedSubcategory)) {
      selectedSubcategory = currentCategoryObj.subcategories.firstOrNull() ?: "General"
    }
  }

  var titleText by remember { mutableStateOf("Overflowing Waste Bins at Taxi Stand") }
  var urgency by remember { mutableStateOf(UrgencyLevel.HIGH) }
  var landmarkText by remember { mutableStateOf("Main entrance, beside the taxi stand") }
  var descriptionText by remember {
    mutableStateOf("Bins are overflowing beside the main entrance. Waste is spreading onto the walkway near the taxi stand.")
  }

  // Regional selection
  var selectedRegion by remember { mutableStateOf("Greater Banjul Area (GBA)") }
  val currentRegionObj = remember(selectedRegion) {
    InfrastructureCategoryRegistry.gambianRegions.find { it.name.startsWith(selectedRegion.take(7)) }
      ?: InfrastructureCategoryRegistry.gambianRegions.first()
  }

  var selectedDistrict by remember { mutableStateOf("Kanifing Municipality") }
  var communityText by remember { mutableStateOf("Serrekunda Central") }

  var hasPhoto by remember { mutableStateOf(true) }
  var photoUri by remember { mutableStateOf<String?>(null) }
  var isRecordingVoice by remember { mutableStateOf(false) }
  var hasVoiceNote by remember { mutableStateOf(true) }
  var voiceDuration by remember { mutableStateOf("0:12") }

  var locName by remember(currentLocationName) { mutableStateOf(currentLocationName) }
  var locDetails by remember(currentLocationDetails) { mutableStateOf(currentLocationDetails) }
  var isDetectingLocation by remember { mutableStateOf(false) }

  var showCategorySelectorDialog by remember { mutableStateOf(false) }
  var showDraftRestoredBanner by remember { mutableStateOf(savedDraft != null && savedDraft.title.isNotBlank()) }

  // Media pickers
  val photoPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia()
  ) { uri: Uri? ->
    if (uri != null) {
      photoUri = uri.toString()
      hasPhoto = true
    }
  }

  Scaffold(
    topBar = {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(ForestGreen)
          .padding(horizontal = 12.dp, vertical = 12.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(onClick = onBackClick, modifier = Modifier.testTag("quick_report_back_button")) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
          }

          Text(
            text = "Make a Report",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )

          // Save Draft Shortcut
          TextButton(
            onClick = {
              val draft = ComplaintDraft(
                title = titleText,
                categoryId = selectedCategoryId,
                subcategory = selectedSubcategory,
                description = descriptionText,
                locationName = locName,
                locationDetails = locDetails,
                region = selectedRegion,
                district = selectedDistrict,
                community = communityText,
                urgency = urgency,
                hasPhoto = hasPhoto,
                photoUri = photoUri,
                hasVoiceNote = hasVoiceNote,
                voiceDuration = voiceDuration
              )
              onSaveDraft(draft)
              Toast.makeText(context, "Draft saved offline successfully", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.testTag("save_draft_top_button")
          ) {
            Text("Save Draft", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(BackgroundLight),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Draft Recovery Banner if available
      if (showDraftRestoredBanner && savedDraft != null) {
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A))
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = "Unsaved draft available (${savedDraft.lastSaved})",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF92400E)
                )
                Text(
                  text = savedDraft.title.ifBlank { "Untitled draft" },
                  fontSize = 11.sp,
                  color = Color(0xFFB45309),
                  maxLines = 1
                )
              }

              Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Button(
                  onClick = {
                    titleText = savedDraft.title
                    selectedCategoryId = savedDraft.categoryId
                    selectedSubcategory = savedDraft.subcategory
                    descriptionText = savedDraft.description
                    locName = savedDraft.locationName.ifBlank { locName }
                    selectedRegion = savedDraft.region
                    selectedDistrict = savedDraft.district
                    communityText = savedDraft.community
                    urgency = savedDraft.urgency
                    hasPhoto = savedDraft.hasPhoto
                    hasVoiceNote = savedDraft.hasVoiceNote
                    showDraftRestoredBanner = false
                    Toast.makeText(context, "Draft restored", Toast.LENGTH_SHORT).show()
                  },
                  colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                  contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                  shape = RoundedCornerShape(14.dp)
                ) {
                  Text("Resume", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                TextButton(
                  onClick = {
                    onClearDraft()
                    showDraftRestoredBanner = false
                  }
                ) {
                  Text("Discard", fontSize = 11.sp, color = TextMuted)
                }
              }
            }
          }
        }
      }

      // Direct Hotline Banner
      item {
        Button(
          onClick = onOpenHotline,
          modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .testTag("direct_hotline_button"),
          shape = RoundedCornerShape(23.dp),
          colors = ButtonDefaults.buttonColors(containerColor = ForestGreenDark)
        ) {
          Text("DIRECT HOT LINE", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
          Spacer(modifier = Modifier.width(8.dp))
          Icon(Icons.Default.Phone, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
        }
      }

      // SECTION 1: INFRASTRUCTURE CATEGORY & SUBCATEGORIES
      item {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "1. INFRASTRUCTURE CATEGORY (22 SECTORS)",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = TextMuted,
              letterSpacing = 1.sp
            )
            Text(
              text = "Required",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = UrgencyCritical
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Currently Selected Category Card with Browse 22 Categories button
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { showCategorySelectorDialog = true }
              .testTag("selected_category_header_card"),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, ForestGreen)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
              ) {
                Box(
                  modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color(currentCategoryObj.colorHex).copy(alpha = 0.15f)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    Icons.Default.Category,
                    contentDescription = null,
                    tint = Color(currentCategoryObj.colorHex),
                    modifier = Modifier.size(20.dp)
                  )
                }

                Column {
                  Text(
                    text = currentCategoryObj.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                  )
                  Text(
                    text = "${currentCategoryObj.groupName} · Routed to ${currentCategoryObj.defaultInstitutionName}",
                    fontSize = 11.sp,
                    color = TextMuted,
                    maxLines = 1
                  )
                }
              }

              Surface(
                shape = RoundedCornerShape(12.dp),
                color = ForestGreenLight
              ) {
                Text(
                  text = "Change (22) →",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = ForestGreen,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // CONDITIONAL SUBCATEGORIES CHIPS (Required per prompt)
          Text(
            text = "Select Specific Subcategory:",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
          )

          Spacer(modifier = Modifier.height(6.dp))

          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            items(currentCategoryObj.subcategories) { subcat ->
              val isSelected = selectedSubcategory.equals(subcat, ignoreCase = true)
              Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (isSelected) ForestGreen else Color.White,
                border = androidx.compose.foundation.BorderStroke(
                  1.dp,
                  if (isSelected) ForestGreen else OutlineBorder
                ),
                modifier = Modifier
                  .clickable { selectedSubcategory = subcat }
                  .testTag("subcat_chip_${subcat.take(6).lowercase()}")
              ) {
                Text(
                  text = subcat,
                  fontSize = 12.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  color = if (isSelected) Color.White else TextDark,
                  modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
              }
            }
          }
        }
      }

      // SECTION 2: TITLE, DESCRIPTION & URGENCY
      item {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "2. ISSUE DETAILS & URGENCY",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = TextMuted,
              letterSpacing = 1.sp
            )
            Text(
              text = "Required",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = UrgencyCritical
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Title field
          OutlinedTextField(
            value = titleText,
            onValueChange = { titleText = it },
            placeholder = { Text("Short issue summary (e.g. $selectedSubcategory at junction)") },
            label = { Text("Issue Title") },
            leadingIcon = {
              Icon(Icons.Default.Title, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(20.dp))
            },
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("report_title_input"),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              unfocusedContainerColor = Color.White,
              focusedContainerColor = Color.White,
              unfocusedBorderColor = OutlineBorder,
              focusedBorderColor = ForestGreen
            )
          )

          Spacer(modifier = Modifier.height(10.dp))

          // Description field
          OutlinedTextField(
            value = descriptionText,
            onValueChange = { descriptionText = it },
            placeholder = { Text("Describe the problem clearly for public dispatch teams...") },
            label = { Text("Detailed Description") },
            minLines = 3,
            maxLines = 5,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("complaint_description_input"),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              unfocusedContainerColor = Color.White,
              focusedContainerColor = Color.White,
              unfocusedBorderColor = OutlineBorder,
              focusedBorderColor = ForestGreen
            )
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Severity / Urgency Selector
          Text(
            text = "Public Safety Severity Level:",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
          )

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            UrgencyCard(
              urgencyLevel = UrgencyLevel.LOW,
              isSelected = urgency == UrgencyLevel.LOW,
              selectedColor = ForestGreen,
              modifier = Modifier.weight(1f),
              onClick = { urgency = UrgencyLevel.LOW }
            )

            UrgencyCard(
              urgencyLevel = UrgencyLevel.HIGH,
              isSelected = urgency == UrgencyLevel.HIGH,
              selectedColor = UrgencyHigh,
              modifier = Modifier.weight(1f),
              onClick = { urgency = UrgencyLevel.HIGH }
            )

            UrgencyCard(
              urgencyLevel = UrgencyLevel.CRITICAL,
              isSelected = urgency == UrgencyLevel.CRITICAL,
              selectedColor = UrgencyCritical,
              modifier = Modifier.weight(1f),
              onClick = { urgency = UrgencyLevel.CRITICAL }
            )
          }
        }
      }

      // SECTION 3: LOCATION, GAMBIAN REGION, DISTRICT & GPS
      item {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "3. REGION, DISTRICT & GPS LOCATION",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = TextMuted,
              letterSpacing = 1.sp
            )
            Text(
              text = "GPS Optional",
              fontSize = 11.sp,
              color = ForestGreen,
              fontWeight = FontWeight.Medium
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Automatic GPS Detection Button
          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .clickable {
                coroutineScope.launch {
                  isDetectingLocation = true
                  val result = com.example.util.LocationHelper.autoDetectCurrentLocation(context)
                  locName = result.locationName
                  locDetails = "${result.localityDetails} · ${result.source}"
                  landmarkText = result.landmark
                  communityText = result.locationName
                  isDetectingLocation = false
                }
              }
              .testTag("quick_report_auto_detect_button"),
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(1.dp, ForestGreen.copy(alpha = 0.4f))
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(ForestGreenLight),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(Icons.Default.MyLocation, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(16.dp))
                }
                Column {
                  Text("One-Tap Automatic Location Fix", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextDark)
                  Text(
                    text = if (isDetectingLocation) "Acquiring GPS fix in The Gambia..." else "Auto-detects nearest Gambian landmark & coordinates",
                    fontSize = 11.sp,
                    color = if (isDetectingLocation) ForestGreen else TextMuted
                  )
                }
              }

              if (isDetectingLocation) {
                CircularProgressIndicator(color = ForestGreen, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
              } else {
                Text("Auto-detect", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Region & District Dropdown / Selectors
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder)
          ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
              Text("Select Administrative Region in The Gambia:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted)

              LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(InfrastructureCategoryRegistry.gambianRegions) { reg ->
                  val isRegSelected = selectedRegion.startsWith(reg.name.take(7))
                  Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isRegSelected) ForestGreen else BackgroundLight,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isRegSelected) ForestGreen else OutlineBorder),
                    modifier = Modifier.clickable {
                      selectedRegion = reg.name
                      selectedDistrict = reg.districts.firstOrNull()?.name ?: "District"
                      communityText = reg.districts.firstOrNull()?.communities?.firstOrNull() ?: "Community"
                    }
                  ) {
                    Text(
                      text = reg.name.substringBefore(" ("),
                      fontSize = 11.sp,
                      fontWeight = if (isRegSelected) FontWeight.Bold else FontWeight.Medium,
                      color = if (isRegSelected) Color.White else TextDark,
                      modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                  }
                }
              }

              // District selector row
              Text("District: $selectedDistrict", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextDark)

              LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(currentRegionObj.districts) { dist ->
                  val isDistSelected = selectedDistrict == dist.name
                  Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isDistSelected) Color(0xFFEFF6FF) else BackgroundLight,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isDistSelected) Color(0xFF2563EB) else OutlineBorder),
                    modifier = Modifier.clickable {
                      selectedDistrict = dist.name
                      communityText = dist.communities.firstOrNull() ?: communityText
                    }
                  ) {
                    Text(
                      text = dist.name,
                      fontSize = 11.sp,
                      fontWeight = if (isDistSelected) FontWeight.Bold else FontWeight.Medium,
                      color = if (isDistSelected) Color(0xFF1D4ED8) else TextDark,
                      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                  }
                }
              }

              // Community & Landmark
              OutlinedTextField(
                value = landmarkText,
                onValueChange = { landmarkText = it },
                label = { Text("Community / Landmark / Street") },
                placeholder = { Text("e.g. Near taxi stand, beside mosque") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
              )
            }
          }
        }
      }

      // SECTION 4: EVIDENCE (PHOTO & VOICE NOTE)
      item {
        Column {
          Text(
            text = "4. EVIDENCE (PHOTO & VOICE NOTE)",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = TextMuted,
            letterSpacing = 1.sp
          )

          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            // Upload Photo Card
            Card(
              modifier = Modifier
                .weight(1f)
                .clickable {
                  photoPickerLauncher.launch(
                    androidx.activity.result.PickVisualMediaRequest(
                      ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                  )
                }
                .testTag("upload_photo_card"),
              shape = RoundedCornerShape(14.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder)
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(Icons.Default.FileUpload, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                  Text("Photo Evidence", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextDark)
                  Text(if (hasPhoto) "1 photo attached" else "From gallery", fontSize = 11.sp, color = if (hasPhoto) ForestGreen else TextMuted)
                }
              }
            }

            // Voice Note Card
            Card(
              modifier = Modifier
                .weight(1f)
                .clickable {
                  if (hasVoiceNote) {
                    hasVoiceNote = false
                  } else {
                    isRecordingVoice = true
                    hasVoiceNote = true
                  }
                }
                .testTag("voice_note_card"),
              shape = RoundedCornerShape(14.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder)
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(Icons.Default.Mic, contentDescription = null, tint = if (hasVoiceNote) ForestGreen else TextMuted, modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                  Text("Voice Note", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextDark)
                  Text(if (hasVoiceNote) "Recorded ($voiceDuration)" else "Tap to record", fontSize = 11.sp, color = if (hasVoiceNote) ForestGreen else TextMuted)
                }
              }
            }
          }

          if (hasVoiceNote) {
            Spacer(modifier = Modifier.height(10.dp))
            VoiceNotePlayer(durationText = voiceDuration)
          }
        }
      }

      // SUBMIT AND SAVE DRAFT ACTIONS
      item {
        Spacer(modifier = Modifier.height(8.dp))

        Button(
          onClick = {
            val title = if (titleText.isNotBlank()) {
              titleText.trim()
            } else if (descriptionText.isNotBlank()) {
              if (descriptionText.length > 40) "${descriptionText.take(37)}..." else descriptionText
            } else {
              "$selectedSubcategory at $locName"
            }

            val categoryEnum = InfrastructureCategoryRegistry.mapCategoryIdToIssueCategory(selectedCategoryId)

            onReviewAndSubmit(
              title,
              categoryEnum,
              descriptionText,
              locName,
              landmarkText.ifBlank { locDetails },
              urgency,
              hasPhoto,
              photoUri,
              hasVoiceNote,
              voiceDuration,
              selectedCategoryId,
              selectedSubcategory,
              selectedRegion,
              selectedDistrict,
              communityText.ifBlank { locName }
            )
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("review_and_submit_button"),
          shape = RoundedCornerShape(26.dp),
          colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
        ) {
          Icon(Icons.Default.NearMe, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Review & Submit Report", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
          onClick = {
            val draft = ComplaintDraft(
              title = titleText,
              categoryId = selectedCategoryId,
              subcategory = selectedSubcategory,
              description = descriptionText,
              locationName = locName,
              locationDetails = locDetails,
              region = selectedRegion,
              district = selectedDistrict,
              community = communityText,
              urgency = urgency,
              hasPhoto = hasPhoto,
              photoUri = photoUri,
              hasVoiceNote = hasVoiceNote,
              voiceDuration = voiceDuration
            )
            onSaveDraft(draft)
            Toast.makeText(context, "Draft saved locally on device", Toast.LENGTH_SHORT).show()
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .testTag("save_draft_bottom_button"),
          shape = RoundedCornerShape(22.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder)
        ) {
          Icon(Icons.Default.Save, contentDescription = null, tint = TextDark, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Save as Offline Draft", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
        }

        Spacer(modifier = Modifier.height(20.dp))
      }
    }
  }

  // 22 Categories Selection Dialog
  if (showCategorySelectorDialog) {
    var dialogSearchQuery by remember { mutableStateOf("") }
    val dialogFilteredCategories = remember(dialogSearchQuery) {
      if (dialogSearchQuery.isBlank()) InfrastructureCategoryRegistry.allCategories
      else InfrastructureCategoryRegistry.allCategories.filter {
        it.name.contains(dialogSearchQuery, ignoreCase = true) ||
          it.groupName.contains(dialogSearchQuery, ignoreCase = true) ||
          it.subcategories.any { sub -> sub.contains(dialogSearchQuery, ignoreCase = true) }
      }
    }

    AlertDialog(
      onDismissRequest = { showCategorySelectorDialog = false },
      title = {
        Column {
          Text("Select Infrastructure Sector", fontSize = 16.sp, fontWeight = FontWeight.Bold)
          Text("All 22 public categories recognized in The Gambia", fontSize = 11.sp, color = TextMuted)
        }
      },
      text = {
        Column(modifier = Modifier.fillMaxWidth().heightIn(max = 420.dp)) {
          OutlinedTextField(
            value = dialogSearchQuery,
            onValueChange = { dialogSearchQuery = it },
            placeholder = { Text("Filter sectors...", fontSize = 12.sp) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
          )

          LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            items(dialogFilteredCategories) { cat ->
              val isSelected = selectedCategoryId == cat.id
              Surface(
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable {
                    selectedCategoryId = cat.id
                    selectedSubcategory = cat.subcategories.firstOrNull() ?: "General"
                    showCategorySelectorDialog = false
                  },
                shape = RoundedCornerShape(10.dp),
                color = if (isSelected) ForestGreenLight else BackgroundLight,
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) ForestGreen else OutlineBorder)
              ) {
                Row(
                  modifier = Modifier.padding(10.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Column(modifier = Modifier.weight(1f)) {
                    Text(text = cat.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextDark)
                    Text(text = "${cat.groupName} · ${cat.defaultInstitutionName}", fontSize = 10.sp, color = TextMuted, maxLines = 1)
                  }
                  if (isSelected) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(16.dp))
                  }
                }
              }
            }
          }
        }
      },
      confirmButton = {},
      dismissButton = {
        TextButton(onClick = { showCategorySelectorDialog = false }) { Text("Close") }
      }
    )
  }
}

@Composable
private fun UrgencyCard(
  urgencyLevel: UrgencyLevel,
  isSelected: Boolean,
  selectedColor: Color,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  Card(
    modifier = modifier
      .clickable { onClick() }
      .testTag("urgency_${urgencyLevel.name.lowercase()}"),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isSelected) selectedColor else Color.White
    ),
    border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp, horizontal = 4.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = urgencyLevel.title,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = if (isSelected) Color.White else TextDark
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = urgencyLevel.subtitle,
        fontSize = 10.sp,
        fontWeight = FontWeight.Medium,
        color = if (isSelected) Color.White.copy(alpha = 0.9f) else TextMuted,
        textAlign = androidx.compose.ui.text.style.TextAlign.Center
      )
    }
  }
}
