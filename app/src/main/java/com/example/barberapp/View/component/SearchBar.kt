package com.example.barberapp.View.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.barberapp.View.utils.SearchBg
import com.example.barberapp.View.utils.TextSecondary

// ── Search bar ────────────────────────────────────────────────────────────────
@Composable
fun SearchBar(
    modifier: Modifier = Modifier.Companion,
    query: String,
    onQueryChange: (String)-> Unit,
    onSearch: ()-> Unit,
    ) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SearchBg)
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.Companion.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Search,
            contentDescription = "Search",
            tint = TextSecondary,
            modifier = Modifier.Companion.size(20.dp)
        )
        Spacer(modifier = Modifier.Companion.width(10.dp))
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(
                color = TextSecondary,
                fontSize = 14.sp
            ),
            cursorBrush = SolidColor(TextSecondary),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch ={
                    onSearch()
                }
            ),
            decorationBox = {innerTextField->
                if (query.isEmpty()){
                    Text(
                        text = "Search barber shop...",
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                }
                innerTextField()
            }
        )
    }
}