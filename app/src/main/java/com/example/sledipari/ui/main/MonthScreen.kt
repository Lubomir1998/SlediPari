package com.example.sledipari.ui.main

import android.view.View
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sledipari.R
import com.example.sledipari.data.models.Month
import com.example.sledipari.ui.MainActivity
import com.example.sledipari.ui.home
import com.example.sledipari.utility.*
import com.example.sledipari.utility.extensions.*
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun MonthScreen(
    viewModel: GetMonthViewModel,
    activity: MainActivity,
    view: View
) {

    val context = LocalContext.current

    val currentMonthId by remember {
        viewModel.monthId
    }

    val currentMonth by remember {
        viewModel.month
    }

    val allMonths by remember {
        viewModel.allMonths
    }

    val isLoading by remember {
        viewModel.isLoading
    }

    val errorMessage by remember {
        viewModel.errorMessage
    }

    val currentCategory by remember {
        viewModel.currentCategory
    }

    val totalSum by remember {
        viewModel.totalSum
    }

    val currentList by remember {
        viewModel.currentList
    }

    errorMessage?.let {
        Toast.makeText(context, it, Toast.LENGTH_LONG).show()
    }

    LaunchedEffect(key1 = true) {
        viewModel.getMonthLocal(currentMonthId)
    }

    LaunchedEffect(key1 = currentMonthId) {
        viewModel.getMonthLocal(currentMonthId)
        viewModel.getAllMonths()
    }

    LaunchedEffect(key1 = currentMonth) {
        currentMonth?.let { month ->

            when(currentCategory) {
                context.getString(R.string.food) -> {
                    viewModel.changeTotalSum(month.restaurant + month.home)
                    viewModel.changeList(foodToList(month))
                }
                context.getString(R.string.smetki) -> {
                    viewModel.changeTotalSum(month.tok + month.voda + month.toplo + month.internet + month.telefon + month.vhod)
                    viewModel.changeList(smetkiToList(month))
                }
                context.getString(R.string.transport) -> {
                    viewModel.changeTotalSum(month.publicT + month.taxi + month.car)
                    viewModel.changeList(transportToList(month))
                }
                context.getString(R.string.cosmetics) -> {
                    viewModel.changeTotalSum(month.higien + month.other)
                    viewModel.changeList(cosmeticsToList(month))
                }
                context.getString(R.string.preparati) -> {
                    viewModel.changeTotalSum(month.clean + month.wash)
                    viewModel.changeList(preparatiToList(month))
                }
                context.getString(R.string.frizior) -> {
                    viewModel.changeTotalSum(month.friziorSub + month.cosmetic + month.manikior)
                    viewModel.changeList(friziorToList(month))
                }
                context.getString(R.string.all) -> {
                    viewModel.changeList(month.toList())
                    viewModel.changeTotalSum(month.totalSum())
                }
            }
        }
    }

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    )

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            BottomSheetContent(
                bottomSheetScaffoldState = bottomSheetScaffoldState,
                viewModel = viewModel,
                activity = activity,
                view = view
            )
        },
        sheetPeekHeight = 0.dp
    ) {
        MonthContent(
            allMonths = allMonths,
            currentMonthId = currentMonthId,
            currentMonth = currentMonth,
            isLoading = isLoading,
            currentCategory = currentCategory,
            currentList = currentList,
            totalSum = totalSum,
            viewModel = viewModel,
            bottomSheetScaffoldState = bottomSheetScaffoldState
        )
    }

}

@ExperimentalMaterialApi
@Composable fun BottomSheetContent(
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    viewModel: GetMonthViewModel,
    activity: MainActivity,
    view: View,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val options = addingOptions(context)
    val quantityOptions = (1..10).toList()

    var currentSelectedOption by remember {
        mutableStateOf<Pair<String, String>?>(null)
    }

    var currentSelectedQuantity by remember {
        mutableStateOf(1)
    }

    var isCategoriesExpanded by remember {
        mutableStateOf(false)
    }

    var isQuantityExpanded by remember {
        mutableStateOf(false)
    }

    var sendNotificationsChecked by remember {
        mutableStateOf(false)
    }

    val isLoading by remember {
        viewModel.isLoading
    }

    val hasCompleted by remember {
        viewModel.hasCompleted
    }

    val isSpendingSuccessful by remember {
        viewModel.isSpendingSuccessful
    }

    val errorMessage by remember {
        viewModel.errorMessage
    }

    var sumText by remember {
        mutableStateOf("")
    }

    errorMessage?.let {
        Toast.makeText(context, it, Toast.LENGTH_LONG).show()
    }

    LaunchedEffect(key1 = hasCompleted) {

        viewModel.hasCompleted.value = false

        if (isSpendingSuccessful) {
            sumText = ""
            currentSelectedOption = null
            currentSelectedQuantity = 1
            bottomSheetScaffoldState.bottomSheetState.collapse()
            viewModel.getAllMonths()
            viewModel.getMonth(viewModel.monthId.value)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.95f)
            .background(color = colorResource(id = R.color.system_gray5))
    ) {

        Divider(
            modifier = Modifier
                .padding(12.dp)
                .width(41.dp)
                .height(5.dp)
                .clip(RoundedCornerShape(100.dp))
                .background(colorResource(id = R.color.system_gray5))
        )


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(colorResource(id = R.color.section))
        ) {

            // spending type
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    text = context.getString(R.string.spending_type),
                    color = colorResource(id = R.color.label)
                )
                DropDownMenuCategories(
                    isCategoriesExpanded = isCategoriesExpanded,
                    currentSelectedOption = currentSelectedOption,
                    options = options,
                    onCategoryArrowClick = { isCategoriesExpanded = it },
                    onSelectCategory = {
                        currentSelectedOption = it
                        isCategoriesExpanded = false
                    },
                    onDismiss = { isCategoriesExpanded = false }
                )
            }

            Divider(
                modifier = Modifier
                    .height(1.dp)
                    .padding(start = 10.dp)
                    .background(colorResource(id = R.color.divider))
            )

            // price
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    text = context.getString(R.string.price),
                    color = colorResource(id = R.color.label)
                )
                TextField(
                    value = sumText,
                    onValueChange = {
                        sumText = if (it.isEmpty()){
                            it
                        } else {
                            when (it.toDoubleOrNull()) {
                                null -> sumText //old value
                                else -> it   //new value
                            }
                        }
                    },
                    colors = TextFieldDefaults.textFieldColors(textColor = colorResource(id = R.color.label)),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .width(130.dp)
                )
            }

            Divider(
                modifier = Modifier
                    .height(1.dp)
                    .padding(start = 10.dp)
                    .background(colorResource(id = R.color.divider))
            )

            // quantity
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    text = context.getString(R.string.quantity),
                    color = colorResource(id = R.color.label)
                )
                DropDownMenuQuantity(
                    isQuantityExpanded = isQuantityExpanded,
                    currentSelectedOption = currentSelectedQuantity,
                    options = quantityOptions,
                    onQuantityArrowClick = { isQuantityExpanded = it },
                    onSelectQuantity = {
                        currentSelectedQuantity = it
                        isQuantityExpanded = false
                    },
                    onDismiss = { isQuantityExpanded = false }
                )
            }

            // send notification
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceBetween,
//                modifier = modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 10.dp)
//            ) {
//                Text(
//                    text = context.getString(R.string.send_notification),
//                    color = colorResource(id = R.color.label)
//                )
//                Switch(checked = sendNotificationsChecked, onCheckedChange = { sendNotificationsChecked = it })
//            }
        }

        Spacer(modifier = Modifier.size(50.dp))

        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = home),
            enabled = !isLoading,
            onClick = {
                activity.hideKeyboard(view)

                if (currentSelectedOption == null) {
                    Toast.makeText(context, context.getString(R.string.nothing_selected), Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (sumText.trim().isEmpty()) {
                    Toast.makeText(context, context.getString(R.string.enter_price), Toast.LENGTH_SHORT).show()
                    return@Button
                }

                viewModel.addSpending(currentSelectedOption!!, sumText.toFloat() * currentSelectedQuantity, sendNotificationsChecked)
            }
        ) {
            Text(
                text = context.getString(R.string.add),
                fontSize = 18.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.size(20.dp))

        if (isLoading) {
            CircularProgressIndicator()
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun MonthContent(
    allMonths: List<Month>,
    currentMonthId: String,
    currentMonth: Month?,
    isLoading: Boolean,
    currentCategory: String,
    currentList: List<Pair<Pair<Float, String>, Color>>,
    totalSum: Float,
    viewModel: GetMonthViewModel,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.background))
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = colorResource(id = R.color.label),
                modifier = Modifier
                    .padding(16.dp)
                    .size(36.dp)
                    .clickable {
                        coroutineScope.launch {
                            if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                bottomSheetScaffoldState.bottomSheetState.expand()
                            }
                        }
                    }
            )
        }

        AllMonthsRow(
            allMonths = allMonths,
            currentMonthId = currentMonthId
        ) { month ->
            viewModel.monthId.value = month.id
            if (currentCategory != context.getString(R.string.all) && month.getCurrentCategoryValue(context, currentCategory) == 0f) {
                viewModel.changeCategory(context.getString(R.string.all))
            }
        }

        Spacer(modifier = Modifier.size(10.dp))

        currentMonth?.let { month ->

            Text(
                text = month.id.toReadableDate(),
                fontSize = 20.sp,
                color = colorResource(id = R.color.label)
            )

            Spacer(modifier = Modifier.size(10.dp))

            CategoryTitle(title = currentCategory) {
                viewModel.changeCategory(context.getString(R.string.all))
                viewModel.changeList(month.toList())
                viewModel.changeTotalSum(month.totalSum())
            }

            Spacer(modifier = Modifier.size(10.dp))

            PieChart(
                list = currentList,
                totalSum = totalSum,
                modifier = Modifier.padding(
                    bottom = 20.dp
                )
            ) { categoryName ->
                viewModel.changeCategory(categoryName.getTitle(context))

                when(categoryName) {
                    "food" -> {
                        viewModel.changeTotalSum(month.restaurant + month.home)
                        viewModel.changeList(foodToList(month))
                    }
                    "smetki" -> {
                        viewModel.changeTotalSum(month.tok + month.voda + month.toplo + month.internet + month.telefon + month.vhod)
                        viewModel.changeList(smetkiToList(month))
                    }
                    "transport" -> {
                        viewModel.changeTotalSum(month.publicT + month.taxi + month.car)
                        viewModel.changeList(transportToList(month))
                    }
                    "cosmetics" -> {
                        viewModel.changeTotalSum(month.higien + month.other)
                        viewModel.changeList(cosmeticsToList(month))
                    }
                    "preparati" -> {
                        viewModel.changeTotalSum(month.clean + month.wash)
                        viewModel.changeList(preparatiToList(month))
                    }
                    "frizior" -> {
                        viewModel.changeTotalSum(month.friziorSub + month.cosmetic + month.manikior)
                        viewModel.changeList(friziorToList(month))
                    }
                    else -> Unit
                }
            }
        } ?: run {
            Text(
                text = stringResource(id = R.string.nothing_for_now),
                color = colorResource(id = R.color.label),
                fontSize = 22.sp
            )
        }

    }
}

@Composable
fun MonthItem(
    monthId: String,
    currentMonthId: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit) = {}
) {

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(
                color = if (monthId == currentMonthId) colorResource(id = R.color.system_gray3) else colorResource(
                    id = R.color.background
                )
            )
            .padding(7.dp)
            .clickable {
                onClick()
            }
    ) {

        Text(
            text = monthId.toReadableDate(),
            fontSize = 14.sp,
            color = colorResource(id = R.color.label),
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
fun SpendingItem(
    color: Color,
    name: String,
    sum: Float,
    total: Float,
    highlighted: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                if (highlighted) {
                    onClick()
                }
            }
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(color = color)
        )

        Text(
            text = name.toLocalizable(LocalContext.current) + if (highlighted) " *" else "",
            fontSize = 16.sp,
            color = colorResource(id = R.color.label),
            modifier = Modifier.padding(start = 7.dp)
        )

        Text(
            text = " - ${sum.formatPrice()} " + stringResource(id = R.string.leva),
            fontSize = 16.sp,
            color = colorResource(id = R.color.label)
        )

        Text(
            text = " (${String.format("%.2f", sum.toPercent(total))} %)",
            fontSize = 12.sp,
            color = colorResource(id = R.color.secondarylabel),
            modifier = Modifier.padding(start = 7.dp)
        )
    }
}

@Composable
fun CategoryTitle(
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth(0.8f)
    ) {
        if (title == stringResource(id = R.string.all)) {
            Row{}
        }
        else {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = colorResource(id = R.color.label),
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        onClick()
                    }
            )
        }

        Text(
            text = title,
            fontSize = 18.sp,
            color = colorResource(id = R.color.label)
        )

        Row{}
    }
}

@Composable
fun AllMonthsRow(
    allMonths: List<Month>,
    currentMonthId: String,
    modifier: Modifier = Modifier,
    onClick: (Month) -> Unit
) {

    val listState = rememberLazyListState()
    LaunchedEffect(allMonths) {
        if (allMonths.isNotEmpty()) {
            listState.scrollToItem(allMonths.size - 1)
        }
    }

    LazyRow(
        state = listState,
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 10.dp
        ),
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = colorResource(id = R.color.system_gray3)
            )
    ) {
        itemsIndexed(allMonths) { index, month ->
            MonthItem(
                monthId = month.id,
                currentMonthId = currentMonthId,
                modifier = Modifier.padding(start = if (index != 0) 24.dp else 0.dp)
            ) {
                onClick(month)
            }
        }
    }
}

@Composable
fun PieChart(
    list: List<Pair<Pair<Float, String>, Color>>,
    totalSum: Float,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {

        Canvas(
            modifier = Modifier
                .size(250.dp)
        ) {

            var anglesSum = 0f

            list.reversed().forEach { result ->

                drawArc(
                    color = result.second,
                    startAngle = 270f + anglesSum,
                    sweepAngle = result.first.first.toPercent(totalSum) * 3.6f,
                    useCenter = true,
                    size = Size(size.width, size.height)
                )

                anglesSum += result.first.first.toPercent(totalSum) * 3.6f

            }

        }

        Spacer(modifier = Modifier.size(10.dp))

        list.forEach {
            SpendingItem(
                color = it.second,
                name = it.first.second,
                sum = it.first.first,
                total = totalSum,
                highlighted = it.first.second == "food"
                        || it.first.second == "smetki"
                        || it.first.second == "transport"
                        || it.first.second == "cosmetics"
                        || it.first.second == "preparati"
                        || it.first.second == "frizior"
            ) {
                onClick(it.first.second)
            }
        }

        TotalSumRow(totalSum = totalSum)
    }
}

@Composable
fun TotalSumRow(
    totalSum: Float,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = modifier.fillMaxWidth(0.6f)
    ) {
        Text(
            text = stringResource(id = R.string.total) + " ${totalSum.formatPrice()} " + stringResource(id = R.string.leva),
            fontSize = 18.sp,
            color = colorResource(id = R.color.label)
        )
    }
}

@Composable
fun DropDownMenuCategories(
    isCategoriesExpanded: Boolean,
    currentSelectedOption: Pair<String, String>?,
    options:  HashMap<String, String>,
    onCategoryArrowClick: ((Boolean) -> Unit),
    onSelectCategory: ((Pair<String, String>?) -> Unit),
    onDismiss: (() -> Unit)
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            onCategoryArrowClick(!isCategoriesExpanded)
        }
    ) {
        Text(
            text = currentSelectedOption?.first ?: "--",
            fontSize = 16.sp,
            color = colorResource(id = R.color.label)
        )
        Spacer(modifier = Modifier.size(5.dp))
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = null,
            tint = colorResource(id = R.color.label),
            modifier = Modifier
                .padding(16.dp)
                .size(36.dp)
        )
        DropdownMenu(
            expanded = isCategoriesExpanded,
            modifier = Modifier.background(colorResource(id = R.color.background)),
            onDismissRequest = onDismiss
        ) {
            options.entries.forEach {
                Text(
                    text = it.key,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.label),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 8.dp,
                            horizontal = 16.dp
                        )
                        .clickable {
                            onSelectCategory(Pair(it.key, it.value))
                        }
                )
            }
        }
    }
}

@Composable
fun DropDownMenuQuantity(
    isQuantityExpanded: Boolean,
    currentSelectedOption: Int,
    options:  List<Int>,
    onQuantityArrowClick: ((Boolean) -> Unit),
    onSelectQuantity: ((Int) -> Unit),
    onDismiss: (() -> Unit)
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            onQuantityArrowClick(!isQuantityExpanded)
        }
    ) {
        Text(
            text = "x$currentSelectedOption",
            fontSize = 16.sp,
            color = colorResource(id = R.color.label)
        )
        Spacer(modifier = Modifier.size(5.dp))
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = null,
            tint = colorResource(id = R.color.label),
            modifier = Modifier
                .padding(6.dp)
                .size(36.dp)
        )
        DropdownMenu(
            expanded = isQuantityExpanded,
            modifier = Modifier.background(colorResource(id = R.color.background)),
            onDismissRequest = onDismiss
        ) {
            options.forEach {
                Text(
                    text = it.toString(),
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.label),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 8.dp,
                            horizontal = 16.dp
                        )
                        .clickable {
                            onSelectQuantity(it)
                        }
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewSpendingItem() {
    SpendingItem(color = Color.Blue, name = "clothes", sum = 40f, total = 122f, highlighted = true) {}
}