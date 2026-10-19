package com.example.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*
import com.example.util.LocationHelper
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun SetLocationScreen(
  initialLocationName: String = "Serrekunda Market",
  initialLandmark: String = "Main entrance, beside the taxi stand",
  onBackClick: () -> Unit,
  onHelpClick: () -> Unit,
  onConfirmLocation: (locationName: String, landmark: String) -> Unit
) {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()

  var isGpsAllowed by remember { mutableStateOf(true) }
  var isAutoDetecting by remember { mutableStateOf(false) }
  var locationName by remember { mutableStateOf(initialLocationName) }
  var landmark by remember { mutableStateOf(initialLandmark) }
  var pinOffset by remember { mutableStateOf(Offset.Zero) }
  var gpsAccuracyLabel by remember { mutableStateOf("GPS Auto-detected (±3m)") }

  fun triggerAutoDetect() {
    coroutineScope.launch {
      isAutoDetecting = true
      val result = LocationHelper.autoDetectCurrentLocation(context)
      locationName = result.locationName
      landmark = result.landmark
      gpsAccuracyLabel = "${result.localityDetails} · ${result.source}"
      pinOffset = Offset(0f, 0f)
      isAutoDetecting = false
    }
  }

  val locationPermissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
  ) { isGranted ->
    isGpsAllowed = isGranted
    if (isGranted) {
      triggerAutoDetect()
    }
  }

  Scaffold(
    topBar = {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(ForestGreen)
          .padding(horizontal = 12.dp, vertical = 10.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(onClick = onBackClick, modifier = Modifier.testTag("set_location_back_button")) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
          }

          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = "Set Location",
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Text(
              text = "Quick Report · Location",
              fontSize = 12.sp,
              color = Color.White.copy(alpha = 0.85f)
            )
          }

          IconButton(onClick = onHelpClick) {
            Icon(Icons.Default.HelpOutline, contentDescription = "Help", tint = Color.White)
          }
        }
      }
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(BackgroundLight)
        .verticalScroll(rememberScrollState())
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Header Text
      Column {
        Text(
          text = "Where is the issue?",
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = TextDark
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Use your location, move the pin or enter a nearby landmark.",
          fontSize = 13.sp,
          color = TextMuted,
          lineHeight = 18.sp
        )
      }

      // Automatic Location Detection Bar
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .clickable {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            triggerAutoDetect()
          }
          .testTag("auto_detect_location_bar"),
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
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Box(
              modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(ForestGreenLight),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.MyLocation,
                contentDescription = null,
                tint = ForestGreen,
                modifier = Modifier.size(18.dp)
              )
            }
            Column {
              Text(
                text = "Automatic Location Detection",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
              )
              Text(
                text = if (isAutoDetecting) "Scanning GPS satellites & Gambian landmarks..." else gpsAccuracyLabel,
                fontSize = 11.sp,
                color = if (isAutoDetecting) ForestGreen else TextMuted
              )
            }
          }

          if (isAutoDetecting) {
            CircularProgressIndicator(
              color = ForestGreen,
              modifier = Modifier.size(18.dp),
              strokeWidth = 2.dp
            )
          } else {
            Text(
              text = "Detect",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = ForestGreen
            )
          }
        }
      }

      // Map Preview with draggable Pin
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .height(200.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
      ) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
              detectDragGestures { change, dragAmount ->
                change.consume()
                pinOffset = Offset(
                  x = (pinOffset.x + dragAmount.x).coerceIn(-120f, 120f),
                  y = (pinOffset.y + dragAmount.y).coerceIn(-60f, 60f)
                )
              }
            }
        ) {
          Image(
            painter = painterResource(id = R.drawable.map_serrekunda_sat_1792407020390),
            contentDescription = "Satellite view of Serrekunda",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
          )

          // Pin
          Box(
            modifier = Modifier
              .align(Alignment.Center)
              .offset { IntOffset(pinOffset.x.roundToInt(), pinOffset.y.roundToInt()) }
              .size(40.dp)
              .clip(CircleShape)
              .background(ForestGreen),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.LocationOn,
              contentDescription = "Location Pin",
              tint = Color.White,
              modifier = Modifier.size(26.dp)
            )
          }

          // Status Badge overlay
          Surface(
            modifier = Modifier
              .align(Alignment.TopCenter)
              .padding(top = 10.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color.White.copy(alpha = 0.95f),
            shadowElevation = 2.dp
          ) {
            Text(
              text = if (isGpsAllowed) "📍 $locationName" else "Location permission pending",
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold,
              color = if (isGpsAllowed) ForestGreen else UrgencyHigh,
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
          }

          // Bottom street label
          Surface(
            modifier = Modifier
              .align(Alignment.BottomCenter)
              .padding(bottom = 10.dp),
            shape = RoundedCornerShape(8.dp),
            color = Color.White.copy(alpha = 0.9f)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Icon(Icons.Default.Storefront, contentDescription = null, tint = TextMuted, modifier = Modifier.size(13.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text(locationName.uppercase(), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextDark)
            }
          }
        }
      }

      // Allow location access card (if GPS not yet allowed)
      if (!isGpsAllowed) {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Icon(
                imageVector = Icons.Default.GpsFixed,
                contentDescription = null,
                tint = ForestGreen,
                modifier = Modifier.size(24.dp)
              )
              Text(
                text = "Allow location access?",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = "GPS helps place the issue on the map. You can decline and use an address or landmark instead.",
              fontSize = 13.sp,
              color = TextMuted,
              lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Button(
              onClick = {
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
              },
              modifier = Modifier.fillMaxWidth().height(48.dp),
              shape = RoundedCornerShape(24.dp),
              colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
            ) {
              Icon(Icons.Default.MyLocation, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text("Allow location", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
              onClick = { isGpsAllowed = true },
              modifier = Modifier.fillMaxWidth().height(48.dp),
              shape = RoundedCornerShape(24.dp)
            ) {
              Text("Enter location manually", color = TextDark, fontWeight = FontWeight.SemiBold)
            }
          }
        }
      }

      // AFTER ALLOWING LOCATION section (PDF page 5)
      Text(
        text = "AFTER ALLOWING LOCATION",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = TextMuted,
        letterSpacing = 1.sp
      )

      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(ForestGreenLight),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = ForestGreen,
                modifier = Modifier.size(20.dp)
              )
            }

            Column {
              Text(
                text = locationName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
              )
              Text(
                text = gpsAccuracyLabel,
                fontSize = 12.sp,
                color = TextMuted
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          OutlinedButton(
            onClick = {
              pinOffset = Offset(15f, 15f)
            },
            modifier = Modifier.fillMaxWidth().height(44.dp),
            shape = RoundedCornerShape(10.dp)
          ) {
            Icon(Icons.Default.OpenWith, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Edit / reposition pin", color = ForestGreen, fontWeight = FontWeight.SemiBold)
          }
        }
      }

      // Address or landmark field
      Column {
        Text(
          text = "Address or landmark",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = TextDark
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = landmark,
          onValueChange = { landmark = it },
          leadingIcon = {
            Icon(Icons.Default.Edit, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp))
          },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("landmark_input"),
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedBorderColor = OutlineBorder,
            focusedBorderColor = ForestGreen
          )
        )
      }

      // Confirm Location button
      Button(
        onClick = { onConfirmLocation(locationName, landmark) },
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp)
          .testTag("confirm_location_button"),
        shape = RoundedCornerShape(25.dp),
        colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
      ) {
        Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Confirm location",
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
      }

      Text(
        text = "Check the pin and landmark before confirming. GPS is optional.",
        fontSize = 12.sp,
        color = TextMuted,
        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
      )

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}
