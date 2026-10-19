package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*

@Composable
fun WelcomeScreen(
  onSignIn: (name: String, phone: String) -> Unit,
  onContinueAsGuest: () -> Unit,
  onQuickReport: () -> Unit
) {
  var fullName by remember { mutableStateOf("Awa") }
  var phoneNumber by remember { mutableStateOf("+220 765 4321") }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(BackgroundLight)
      .verticalScroll(rememberScrollState()),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Top Green Branding Header
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
        .background(ForestGreen)
        .padding(top = 48.dp, bottom = 36.dp, start = 24.dp, end = 24.dp),
      contentAlignment = Alignment.Center
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Logo circle
        Box(
          modifier = Modifier
            .size(76.dp)
            .shadow(4.dp, CircleShape)
            .clip(CircleShape)
            .background(Color.White)
            .padding(8.dp),
          contentAlignment = Alignment.Center
        ) {
          Image(
            painter = painterResource(id = R.drawable.ic_citizen_logo_1792406984625),
            contentDescription = "Citizen Gambia Logo",
            modifier = Modifier.fillMaxSize().clip(CircleShape)
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
          text = "Citizen Gambia",
          fontSize = 28.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = "Your voice. Your community. Your Gambia.",
          fontSize = 14.sp,
          color = Color.White.copy(alpha = 0.9f),
          textAlign = TextAlign.Center
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Form Container Card
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp),
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = "Welcome Back",
          fontSize = 22.sp,
          fontWeight = FontWeight.Bold,
          color = TextDark
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = "Sign in to track your reports and stay informed.",
          fontSize = 13.sp,
          color = TextMuted,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Full Name Field
        OutlinedTextField(
          value = fullName,
          onValueChange = { fullName = it },
          label = { Text("Full Name") },
          leadingIcon = {
            Icon(Icons.Default.Person, contentDescription = null, tint = TextMuted)
          },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("full_name_input"),
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ForestGreen,
            unfocusedBorderColor = OutlineBorder
          )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Phone Number Field
        OutlinedTextField(
          value = phoneNumber,
          onValueChange = { phoneNumber = it },
          label = { Text("Phone Number") },
          leadingIcon = {
            Icon(Icons.Default.Phone, contentDescription = null, tint = TextMuted)
          },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("phone_number_input"),
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ForestGreen,
            unfocusedBorderColor = OutlineBorder
          )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Sign In Button
        Button(
          onClick = { onSignIn(fullName, phoneNumber) },
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("sign_in_button"),
          shape = RoundedCornerShape(25.dp),
          colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
        ) {
          Text(
            text = "SIGN IN",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Continue as Guest link
        Row(
          modifier = Modifier
            .clickable { onContinueAsGuest() }
            .padding(4.dp)
            .testTag("continue_as_guest_button"),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Just browsing? ",
            fontSize = 14.sp,
            color = TextMuted
          )
          Text(
            text = "Continue as Guest",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = ForestGreen,
            textDecoration = TextDecoration.Underline
          )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // OR Divider
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          HorizontalDivider(modifier = Modifier.weight(1f), color = DividerColor)
          Text(
            text = "OR",
            modifier = Modifier.padding(horizontal = 12.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = TextMuted
          )
          HorizontalDivider(modifier = Modifier.weight(1f), color = DividerColor)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Quick Report Box
        Surface(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          color = ForestGreenLight,
          border = androidx.compose.foundation.BorderStroke(1.dp, ForestGreen.copy(alpha = 0.3f))
        ) {
          Column(
            modifier = Modifier.padding(16.dp)
          ) {
            Row(
              verticalAlignment = Alignment.Top,
              horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(CircleShape)
                  .background(ForestGreen),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Warning,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(20.dp)
                )
              }

              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = "Quick Report",
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold,
                  color = TextDark
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = "No registration required — report any issue instantly.",
                  fontSize = 13.sp,
                  color = TextMuted,
                  lineHeight = 17.sp
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
              onClick = { onQuickReport() },
              modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .testTag("quick_report_now_button"),
              shape = RoundedCornerShape(23.dp),
              colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
            ) {
              Icon(
                imageVector = Icons.Default.NearMe,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Report an Issue Now",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
              )
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(28.dp))
  }
}
