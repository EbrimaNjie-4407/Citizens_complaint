package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.IssueCategory
import com.example.data.model.SatisfactionLevel
import com.example.data.model.UrgencyLevel
import com.example.data.repository.ComplaintRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read app name from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Citizen Gambia", appName)
  }

  @Test
  fun `verify repository seeds sample complaints`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repository = ComplaintRepository(context)

    val complaints = repository.complaints.value
    assertTrue(complaints.isNotEmpty())

    val serrekunda = repository.getComplaintById("CG-2026-01482")
    assertNotNull(serrekunda)
    assertEquals("Overflowing waste bins at market", serrekunda?.title)
    assertEquals(IssueCategory.WASTE, serrekunda?.category)
  }

  @Test
  fun `create complaint and verify ID format and timeline`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repository = ComplaintRepository(context)

    val created = repository.createComplaint(
      title = "Pothole on Kairaba Ave",
      category = IssueCategory.ROADS,
      description = "Deep crater causing traffic backup",
      locationName = "Kairaba Avenue",
      locationDetails = "Near Traffic Lights",
      urgency = UrgencyLevel.HIGH
    )

    assertTrue(created.id.startsWith("CG-"))
    assertTrue(created.timeline.isNotEmpty())
    assertEquals("Received", created.timeline.first().title)

    val found = repository.getComplaintById(created.id)
    assertNotNull(found)
  }

  @Test
  fun `add feedback and review request to complaint`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repository = ComplaintRepository(context)

    val feedbackSuccess = repository.addFeedback(
      complaintId = "CG-2026-01482",
      rating = SatisfactionLevel.SOMEWHAT_SATISFIED,
      comments = "Helpful update"
    )
    assertTrue(feedbackSuccess)

    val reviewSuccess = repository.addReviewRequest(
      complaintId = "CG-2026-01482",
      reason = "I need a clearer progress update",
      explanation = "Has the cleansing supervisor inspected the bins?"
    )
    assertTrue(reviewSuccess)
  }
}
