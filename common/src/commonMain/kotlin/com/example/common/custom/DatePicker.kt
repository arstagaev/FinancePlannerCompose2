package com.example.common.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.common.utils.toIntSafe
import java.util.Calendar

@Composable
fun DatePickerUI(
    label: String,
    onDismissRequest: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = 10.dp,
        backgroundColor = Color.White,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(200.dp)
                .padding(vertical = 10.dp, horizontal = 5.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.h1,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))

            val chosenYear = remember { mutableStateOf(currentYear) }
            val chosenMonth = remember { mutableStateOf(currentMonth) }
            val chosenDay = remember { mutableStateOf(currentDay) }

            DateSelectionSection(
                onYearChosen = { chosenYear.value = it.toIntSafe() },
                onMonthChosen = { chosenMonth.value = monthsNames.map { it.name }.indexOf(it) }
            )

            Spacer(modifier = Modifier.height(30.dp))

           // val context = LocalContext.current
            Button(
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                onClick = {
                    //Toast.makeText(context, "${chosenDay.value}-${chosenMonth.value}-${chosenYear.value}", Toast.LENGTH_SHORT).show()
                    onDismissRequest()
                }
            ) {
                Text(
                    text = "Done",
                    style = MaterialTheme.typography.button,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun DateSelectionSection(
    onYearChosen: (String) -> Unit,
    onMonthChosen: (String) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .width(300.dp)
            .height(120.dp)
    ) {

        InfiniteItemsPicker(
            items = monthsNames.map { it.name },
            firstIndex = Int.MAX_VALUE / 2 - 4 + currentMonth,
            onItemSelected =  onMonthChosen
        )

        InfiniteItemsPicker(
            items = years,
            firstIndex = Int.MAX_VALUE / 2 + (currentYear - 1967),
            onItemSelected = onYearChosen
        )
    }
}

@Composable
fun InfiniteItemsPicker(
    modifier: Modifier = Modifier,
    items: List<String>,
    firstIndex: Int,
    onItemSelected: (String) -> Unit,
) {

    val listState = rememberLazyListState(firstIndex)
    val currentValue = remember { mutableStateOf("") }

    LaunchedEffect(key1 = !listState.isScrollInProgress) {
        onItemSelected(currentValue.value)
        listState.animateScrollToItem(index = listState.firstVisibleItemIndex)
    }

    Box(modifier = Modifier.height(106.dp)) {
        LazyColumn(
            modifier = Modifier.height(106.dp).background(Color.LightGray),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState,
            content = {
                items(count = Int.MAX_VALUE, itemContent = {
                    val index = it % items.size
                    if (it == listState.firstVisibleItemIndex + 1) {
                        currentValue.value = items[index]
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = items[index],
                        modifier = Modifier.alpha(if (it == listState.firstVisibleItemIndex + 1) 1f else 0.3f),
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                })
            }
        )
    }
}

val currentYear = Calendar.getInstance().get(Calendar.YEAR)
val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
val currentMonth = Calendar.getInstance().get(Calendar.MONTH)

val years = (1950..2050).map { it.toString() }
val monthsNumber = (1..12).map { it.toString() }
val days = (1..31).map { it.toString() }
val monthsNames = listOf(
    Month(name = "Jan",1),
    Month(name = "Feb",2),
    Month(name = "Mar",3),
    Month(name = "Apr",4),
    Month(name = "May",5),
    Month(name = "Jun",6),
    Month(name = "Jul",7),
    Month(name = "Aug",8),
    Month(name = "Sep",9),
    Month(name = "Oct",10),
    Month(name = "Nov",11),
    Month(name = "Dec",12)
)

data class Month(val name: String, val number: Int)

fun defineNumMonth(name: String): Int {
    monthsNames.forEachIndexed { index, month ->
        if (name == month.name) {
            return month.number
        }
    }
    return 1
}