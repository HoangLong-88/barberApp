package com.example.barberapp.admin.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.barberapp.admin.model.BookingItem
import com.example.barberapp.admin.model.ServiceItem
import com.example.barberapp.admin.model.ShopItem
import com.example.barberapp.admin.model.UserItem

@Composable
fun SearchBarCustom(query: String, onQueryChange: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        placeholder = { Text("Tìm kiếm...", color = Color.Gray) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF1E1E1E),
            unfocusedContainerColor = Color(0xFF1E1E1E),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )
}

@Composable
fun ShopCard(shop: ShopItem, onEdit: () -> Unit, onDelete: () -> Unit) {
    Surface(color = Color(0xFF1E1E1E), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = shop.imageUrl,
                contentDescription = shop.name,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF2C2C2C)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(shop.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(shop.address, color = Color.Gray, fontSize = 12.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, null, tint = Color(0xFFEBC14F), modifier = Modifier.size(14.dp))
                    Text(" ${shop.rating} • ${shop.priceRange}", color = Color.Gray, fontSize = 12.sp)
                }
            }
            Row {
                IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, null, tint = Color.Gray, modifier = Modifier.size(20.dp)) }
                IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, null, tint = Color.Red.copy(0.7f), modifier = Modifier.size(20.dp)) }
            }
        }
    }
}

@Composable
fun ServiceCard(service: ServiceItem, onEdit: () -> Unit, onDelete: () -> Unit) {
    Surface(color = Color(0xFF1E1E1E), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(service.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("${service.duration} min", color = Color.Gray, fontSize = 14.sp)
                Text("${service.price} VND", color = Color(0xFFEBC14F), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Row {
                IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, null, tint = Color.Gray, modifier = Modifier.size(20.dp)) }
                IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, null, tint = Color.Red.copy(0.7f), modifier = Modifier.size(20.dp)) }
            }
        }
    }
}

@Composable
fun UserCard(user: UserItem, onEdit: () -> Unit, onDelete: () -> Unit) {
    Surface(color = Color(0xFF1E1E1E), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(modifier = Modifier.size(45.dp), shape = CircleShape, color = Color(0xFF2C2C2C)) {
                Box(contentAlignment = Alignment.Center) { Text(if(user.name.isNotEmpty()) user.name.first().toString() else "?", color = Color.White, fontWeight = FontWeight.Bold) }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(user.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("${user.email} • ${user.phone}", color = Color.Gray, fontSize = 12.sp)
                Surface(color = user.roleColor.copy(0.2f), shape = RoundedCornerShape(4.dp)) {
                    Text(user.role, color = user.roleColor, modifier = Modifier.padding(horizontal = 8.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
            Row {
                IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, null, tint = Color.Gray, modifier = Modifier.size(20.dp)) }
                IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, null, tint = Color.Red.copy(0.7f), modifier = Modifier.size(20.dp)) }
            }
        }
    }
}

@Composable
fun BookingCard(
    booking: BookingItem,
    onComplete: () -> Unit,
    onCancel: () -> Unit,
    onDelete: () -> Unit
) {
    val statusColor = when (booking.status) {
        "Completed" -> Color(0xFF4CAF50)
        "Cancelled" -> Color(0xFFF44336)
        else -> Color(0xFFEBC14F)
    }

    Surface(color = Color(0xFF1E1E1E), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(booking.customerName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("${booking.serviceName} • ${booking.barberName}", color = Color.Gray, fontSize = 14.sp)
                    Text(booking.dateTime, color = Color.Gray, fontSize = 14.sp)
                }
                Surface(color = statusColor.copy(0.1f), shape = RoundedCornerShape(12.dp)) {
                    Text(booking.status, color = statusColor, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                if (booking.status == "Pending") {
                    TextButton(onClick = onComplete, colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF4CAF50))) { Text("Hoàn thành", fontWeight = FontWeight.Bold) }
                    TextButton(onClick = onCancel, colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFF44336))) { Text("Hủy", fontWeight = FontWeight.Bold) }
                }
                IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, null, tint = Color.Red.copy(0.5f), modifier = Modifier.size(20.dp)) }
            }
        }
    }
}

@Composable
fun FilterChipCustom(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(color = if (isSelected) Color(0xFFEBC14F).copy(0.3f) else Color(0xFF2C2C2C), shape = RoundedCornerShape(8.dp), modifier = Modifier.clickable { onClick() }) {
        Text(label, color = if (isSelected) Color(0xFFEBC14F) else Color.Gray, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), fontSize = 12.sp)
    }
}

@Composable
fun DialogTextField(label: String, value: String, onValueChange: (String) -> Unit, placeholder: String) {
    Column {
        Text(label, color = Color.Gray, fontSize = 12.sp)
        TextField(
            value = value, onValueChange = onValueChange, modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(focusedContainerColor = Color(0xFF2C2C2C), unfocusedContainerColor = Color(0xFF2C2C2C), focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
            shape = RoundedCornerShape(12.dp), placeholder = { Text(placeholder, color = Color.Gray.copy(0.5f)) }, singleLine = true
        )
    }
}
