package com.example.barberapp.View.screenUI.customer.bookings

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.barberapp.Model.entities.Booking
import com.example.barberapp.Model.entities.BookingService
import com.example.barberapp.Model.entities.Employee
import com.example.barberapp.View.screenUI.customer.home.CardDark
import com.example.barberapp.View.utils.BackgroundDark
import com.example.barberapp.View.utils.GoldAccent
import com.example.barberapp.ViewModel.ShopVM
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// ─── Time slots available ────────────────────────────────────────────────────
private val TIME_SLOTS = listOf(
    "09:00", "10:00", "11:00", "12:00",
    "13:00", "14:00", "15:00", "16:00", "17:00", "18:00"
)

// ─── Helper: generate next 7 days ────────────────────────────────────────────
private data class DateOption(
    val dayOfWeek: String,   // "Thu"
    val dayNumber: String,   // "11"
    val fullDate: String     // "15/06/2026"
)

private fun generateNextDays(count: Int = 7): List<DateOption> {
    val cal = Calendar.getInstance()
    val dowFormat = SimpleDateFormat("EEE", Locale.ENGLISH)
    val dayFormat = SimpleDateFormat("d", Locale.ENGLISH)
    val fullFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
    return (0 until count).map {
        val c = cal.clone() as Calendar
        c.add(Calendar.DAY_OF_YEAR, it)
        DateOption(
            dayOfWeek = dowFormat.format(c.time),
            dayNumber = dayFormat.format(c.time),
            fullDate  = fullFormat.format(c.time)
        )
    }
}

// ─── Section label ───────────────────────────────────────────────────────────
@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        color = Color.White,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
    )
}

// ─── Gold chip (replaces InputChip to fix tím/purple bug) ────────────────────
@Composable
private fun GoldDismissibleChip(label: String, onRemove: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(GoldAccent)           // ← màu vàng cố định, không bị Material tím
            .padding(start = 12.dp, end = 6.dp, top = 6.dp, bottom = 6.dp)
    ) {
        Text(label, color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Medium)
        Spacer(Modifier.width(4.dp))
        Box(
            modifier = Modifier
                .size(18.dp)
                .clip(CircleShape)
                .background(Color(0x33000000))
                .clickable { onRemove() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Xóa",
                tint = Color.Black,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}

// ─── Barber avatar button ─────────────────────────────────────────────────────
@Composable
private fun BarberAvatarButton(
    barber: Employee,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) GoldAccent else Color.Transparent,
        animationSpec = tween(200), label = "border"
    )
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFF3A2A00) else Color(0xFF2E2E2E),
        animationSpec = tween(200), label = "bg"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(72.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(bgColor)
                .border(2.dp, borderColor, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = barber.name.first().toString().uppercase(),
                color = if (isSelected) GoldAccent else Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = barber.name.split(" ").last(), // tên ngắn
            color = if (isSelected) GoldAccent else Color.LightGray,
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

// ─── Date selector pill ───────────────────────────────────────────────────────
@Composable
private fun DatePill(date: DateOption, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) GoldAccent else Color(0xFF252525),
        animationSpec = tween(200), label = "dateBg"
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp)
    ) {
        Text(
            text = date.dayOfWeek,
            color = if (isSelected) Color.Black else Color.Gray,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = date.dayNumber,
            color = if (isSelected) Color.Black else Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// ─── Time slot button ─────────────────────────────────────────────────────────
@Composable
private fun TimeSlotButton(time: String, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) GoldAccent else Color(0xFF252525),
        animationSpec = tween(200), label = "timeBg"
    )
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = time,
            color = if (isSelected) Color.Black else Color.White,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

// ─── Summary row ─────────────────────────────────────────────────────────────
@Composable
private fun SummaryRow(label: String, value: String, valueColor: Color = Color.White) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray, fontSize = 14.sp)
        Text(value, color = valueColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

// ─── Main Screen ──────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingCheckoutScreen(
    navController: NavController,
    shopId: String,
    initialServiceIds: List<String>,
    shopVM: ShopVM = viewModel()
) {
    val context = LocalContext.current
    val db      = FirebaseFirestore.getInstance()

    val shopState by shopVM.shop.collectAsState()

    // ── Selection state ───────────────────────────────────────────────────────
    var selectedServices by remember { mutableStateOf(listOf<BookingService>()) }
    var selectedBarber   by remember { mutableStateOf<Employee?>(null) }
    var selectedDate     by remember { mutableStateOf<DateOption?>(null) }
    var selectedTime     by remember { mutableStateOf<String?>(null) }
    var isLoading        by remember { mutableStateOf(true) }

    val dateOptions = remember { generateNextDays(7) }

    // ── Load shop + filter services ───────────────────────────────────────────
    LaunchedEffect(shopId) {
        shopVM.loadShopDetails(shopId)
    }

    LaunchedEffect(shopState) {
        shopState?.let { shop ->
            val filtered = shop.services
                .filter { initialServiceIds.contains(it.id) }
                .map {
                    BookingService(
                        serviceId = it.id,
                        name      = it.name,
                        price     = it.price.toLong(),
                        duration  = it.duration
                    )
                }
            selectedServices = filtered
            isLoading        = false
        }
    }

    val totalPrice = remember(selectedServices) { selectedServices.sumOf { it.price } }

    // ── UI ────────────────────────────────────────────────────────────────────
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Xác nhận đặt lịch", color = Color.White, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundDark)
            )
        },
        containerColor = BackgroundDark
    ) { paddingValues ->
        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = GoldAccent)
            }
        } else {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // ── 1. Shop info ──────────────────────────────────────
                    item {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = shopState?.name ?: "Barber Shop",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = shopState?.address ?: "",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    }

                    // ── 2. Selected services chips ────────────────────────
                    item {
                        SectionLabel("Dịch vụ đã chọn")
                        if (selectedServices.isEmpty()) {
                            Text(
                                "Chưa chọn dịch vụ nào. Vui lòng quay lại.",
                                color = Color.Red,
                                fontSize = 13.sp
                            )
                        } else {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                selectedServices.forEach { service ->
                                    GoldDismissibleChip(
                                        label    = service.name,
                                        onRemove = {
                                            selectedServices = selectedServices.filter {
                                                it.serviceId != service.serviceId
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // ── 3. Select Barber ──────────────────────────────────
                    item {
                        SectionLabel("Select Barber")
                        val barbers = shopState?.barbers ?: emptyList()
                        if (barbers.isEmpty()) {
                            Text("Không có thợ nào.", color = Color.Gray, fontSize = 13.sp)
                        } else {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                barbers.forEach { barber ->
                                    BarberAvatarButton(
                                        barber     = barber,
                                        isSelected = selectedBarber?.id == barber.id,
                                        onClick    = { selectedBarber = barber }
                                    )
                                }
                            }
                        }
                    }

                    // ── 4. Select Date ────────────────────────────────────
                    item {
                        SectionLabel("Select Date")
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            dateOptions.forEach { date ->
                                DatePill(
                                    date       = date,
                                    isSelected = selectedDate?.fullDate == date.fullDate,
                                    onClick    = { selectedDate = date }
                                )
                            }
                        }
                    }

                    // ── 5. Select Time ────────────────────────────────────
                    item {
                        SectionLabel("Select Time")
                        // Grid: 4 cột
                        val rows = TIME_SLOTS.chunked(4)
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            rows.forEach { rowSlots ->
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    rowSlots.forEach { time ->
                                        TimeSlotButton(
                                            time       = time,
                                            isSelected = selectedTime == time,
                                            onClick    = { selectedTime = time }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // ── 6. Booking Summary card ───────────────────────────
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = CardDark),
                            shape  = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    "Booking Summary",
                                    color      = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize   = 16.sp
                                )
                                Spacer(Modifier.height(12.dp))

                                SummaryRow("Shop", shopState?.name ?: "-")
                                SummaryRow(
                                    label = "Service",
                                    value = if (selectedServices.isEmpty()) "-"
                                    else selectedServices.joinToString(", ") { it.name }
                                )
                                SummaryRow(
                                    label = "Barber",
                                    value = selectedBarber?.name ?: "Chưa chọn"
                                )
                                SummaryRow(
                                    label = "Date",
                                    value = selectedDate?.let {
                                        "${it.dayOfWeek} ${it.dayNumber} — ${it.fullDate}"
                                    } ?: "Chưa chọn"
                                )
                                SummaryRow(
                                    label = "Time",
                                    value = selectedTime ?: "Chưa chọn"
                                )

                                Divider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFF3A3A3A))

                                // Line-items
                                selectedServices.forEach { svc ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(svc.name, color = Color.LightGray, fontSize = 13.sp)
                                        Text("%,d VND".format(svc.price), color = Color.White, fontSize = 13.sp)
                                    }
                                }

                                Divider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFF3A3A3A))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "Total",
                                        color      = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize   = 16.sp
                                    )
                                    Text(
                                        text       = "%,d VND".format(totalPrice),
                                        color      = GoldAccent,
                                        fontWeight = FontWeight.Bold,
                                        fontSize   = 18.sp
                                    )
                                }
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(100.dp)) }
                }

                // ── 7. Confirm button ─────────────────────────────────────
                Button(
                    onClick = {
                        when {
                            selectedServices.isEmpty() ->
                                Toast.makeText(context, "Vui lòng chọn ít nhất 1 dịch vụ!", Toast.LENGTH_SHORT).show()
                            selectedBarber == null ->
                                Toast.makeText(context, "Vui lòng chọn thợ!", Toast.LENGTH_SHORT).show()
                            selectedDate == null ->
                                Toast.makeText(context, "Vui lòng chọn ngày!", Toast.LENGTH_SHORT).show()
                            selectedTime == null ->
                                Toast.makeText(context, "Vui lòng chọn giờ!", Toast.LENGTH_SHORT).show()
                            else -> {
                                val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                                val newBooking = Booking(
                                    userId      = currentUserId,
                                    shopId      = shopId,
                                    shopName    = shopState?.name ?: "",
                                    barberId    = selectedBarber!!.id,
                                    barberName  = selectedBarber!!.name,
                                    services    = selectedServices,
                                    totalPrice  = totalPrice,
                                    bookingDate = selectedDate!!.fullDate,
                                    bookingTime = selectedTime!!,
                                    status      = BookingStatus.Pending
                                )
                                db.collection("bookings").add(newBooking)
                                    .addOnSuccessListener { docRef ->
                                        navController.navigate("booking_success/${docRef.id}") {
                                            popUpTo("booking_checkout/{shopId}/{serviceIds}") { inclusive = true }
                                        }
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(context, "Lỗi: ${it.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                    shape  = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Confirm Booking",
                        color      = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 16.sp
                    )
                }
            }
        }
    }
}