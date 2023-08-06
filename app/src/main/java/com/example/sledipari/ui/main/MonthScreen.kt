@file:OptIn(ExperimentalFoundationApi::class)

package com.example.sledipari.ui.main

import android.content.SharedPreferences
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
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sledipari.R
import com.example.sledipari.data.models.Month
import com.example.sledipari.ui.MainActivity
import com.example.sledipari.ui.getRGB
import com.example.sledipari.ui.home
import com.example.sledipari.ui.settings.currencies.CurrencyViewModel
import com.example.sledipari.ui.wash
import com.example.sledipari.utility.*
import com.example.sledipari.utility.extensions.*
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import kotlinx.serialization.*
import kotlinx.serialization.json.Json

data class SpItem(
    val price: Float,
    val name: String,
    val color: Color,
    var hidden: Boolean = false
)

@ExperimentalMaterialApi
@Composable
fun MonthScreen(
    navController: NavController,
    viewModel: GetMonthViewModel,
    currencyViewModel: CurrencyViewModel,
    activity: MainActivity,
    sharedPreferences: SharedPreferences,
    view: View
) {

    val context = LocalContext.current

    val currentMonthId by viewModel.monthId.collectAsState()
    val currentMonth by viewModel.month.collectAsState()
    val allMonths by viewModel.allMonths.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessageMonthScreen.collectAsState()
    val currentCategory by viewModel.currentCategory.collectAsState()
    val totalSum by viewModel.totalSum.collectAsState()
    val currentList by viewModel.currentList.collectAsState()

    val rates by currencyViewModel.rates.collectAsState()
    val currencyLoading by currencyViewModel.currencyLoading.collectAsState()

    errorMessage?.let {
        Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        viewModel.resetErrorState()
    }

    LaunchedEffect(key1 = true) {
        viewModel.getMonthLocal(currentMonthId)
        viewModel.getAllMonths()
        currencyViewModel.getRates()
    }

    LaunchedEffect(key1 = currentMonthId) {
        viewModel.getMonthLocal(currentMonthId)
    }

    LaunchedEffect(key1 = currentMonth) {
        currentMonth?.let { month ->

            when (currentCategory) {
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
                rates = rates,
                sharedPreferences = sharedPreferences,
                view = view
            )
        },
        sheetPeekHeight = 0.dp
    ) {

        val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)

        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                viewModel.getAllMonthsFromApi()
            },
            indicator = { state, refreshTrigger ->
                SwipeRefreshIndicator(
                    state = state,
                    refreshTriggerDistance = refreshTrigger,
                    backgroundColor = colorResource(
                        id = R.color.background
                    ),
                    contentColor = wash
                )
            }
        ) {

            MonthContent(
                navController = navController,
                allMonths = allMonths,
                currentMonthId = currentMonthId,
                currentMonth = currentMonth,
                isLoading = isLoading,
                currencyLoading = currencyLoading,
                currentCategory = currentCategory,
                currentList = currentList,
                totalSum = totalSum,
                viewModel = viewModel,
                bottomSheetScaffoldState = bottomSheetScaffoldState
            )
        }
    }

}

@ExperimentalMaterialApi
@Composable
fun BottomSheetContent(
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    viewModel: GetMonthViewModel,
    activity: MainActivity,
    rates: Map<String, Double>,
    sharedPreferences: SharedPreferences,
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

    var isCurrencyExpanded by remember {
        mutableStateOf(false)
    }

    var sendNotificationsChecked by remember {
        mutableStateOf(false)
    }

    val isLoading by viewModel.isLoading.collectAsState()

    val hasCompleted by viewModel.hasCompleted.collectAsState()

    val isSpendingSuccessful by viewModel.isSpendingSuccessful.collectAsState()

    val errorMessage by viewModel.errorMessageBottomSheet.collectAsState(null)

    var sumText by remember {
        mutableStateOf("")
    }

    var currentSelectedBaseCurrency by remember {
        mutableStateOf(sharedPreferences.getString(Constants.BASE_CURRENCY_KEY, "BGN") ?: "BGN")
    }

    var currentSelectedBaseRate by remember {
        mutableStateOf(
            try {
                rates.getValue(currentSelectedBaseCurrency)
            } catch (e: NoSuchElementException) {
                1
            }
        )
    }

    errorMessage?.let {
        Toast.makeText(context, it, Toast.LENGTH_LONG).show()
    }

    LaunchedEffect(key1 = hasCompleted) {

        viewModel.resetHasCompleted()

        if (isSpendingSuccessful) {
            sumText = ""
            currentSelectedOption = null
            currentSelectedQuantity = 1
            bottomSheetScaffoldState.bottomSheetState.collapse()
            viewModel.getMonth(System.currentTimeMillis().formatDate("yyyy-MM"))
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
                        sumText = if (it.isEmpty()) {
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

            Divider(
                modifier = Modifier
                    .height(1.dp)
                    .padding(start = 10.dp)
                    .background(colorResource(id = R.color.divider))
            )

            // send notification
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(
                    text = context.getString(R.string.send_notification),
                    color = colorResource(id = R.color.label)
                )
                Switch(
                    checked = sendNotificationsChecked,
                    onCheckedChange = { sendNotificationsChecked = it })
            }

            // currency
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    text = context.getString(R.string.currency),
                    color = colorResource(id = R.color.label)
                )
                DropDownSelectCurrency(
                    isCurrencyExpanded = isCurrencyExpanded,
                    currentSelectedOption = currentSelectedBaseCurrency,
                    options = rates,
                    onCurrencyArrowClick = { isCurrencyExpanded = it },
                    onSelectCurrency = {
                        currentSelectedBaseCurrency = it.first
                        currentSelectedBaseRate = it.second
                        isCurrencyExpanded = false
                    },
                    onDismiss = { isCurrencyExpanded = false }
                )
            }
        }

        Spacer(modifier = Modifier.size(50.dp))

        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = home),
            enabled = !isLoading,
            onClick = {
                activity.hideKeyboard(view)

                if (currentSelectedOption == null) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.nothing_selected),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                }

                if (sumText.trim().isEmpty()) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.enter_price),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                }

                viewModel.addSpending(
                    currentSelectedOption!!,
                    (sumText.toFloat() / currentSelectedBaseRate.toFloat()) * currentSelectedQuantity,
                    currentSelectedOption!!.second.getRGB(),
                    sendNotificationsChecked
                )
            }
        ) {
            Text(
                text = context.getString(R.string.add),
                fontSize = 18.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.size(30.dp))

        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
            enabled = !isLoading,
            onClick = {
                activity.hideKeyboard(view)

                if (currentSelectedOption == null) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.nothing_selected),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                }

                if (sumText.trim().isEmpty()) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.enter_price),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                }

                viewModel.addSpending(
                    currentSelectedOption!!,
                    (sumText.toFloat() / currentSelectedBaseRate.toFloat()) * currentSelectedQuantity,
                    currentSelectedOption!!.second.getRGB(),
                    sendNotificationsChecked,
                    false
                )
            }
        ) {
            Text(
                text = context.getString(R.string.undo),
                fontSize = 10.sp,
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
    navController: NavController,
    allMonths: List<Month>,
    currentMonthId: String,
    currentMonth: Month?,
    isLoading: Boolean,
    currencyLoading: Boolean,
    currentCategory: String,
    currentList: List<SpItem>,
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
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                tint = colorResource(id = R.color.icon_tint),
                modifier = Modifier
                    .padding(16.dp)
                    .size(36.dp)
                    .clickable {
                        navController.navigate("settings_screen")
                    }
            )

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = colorResource(id = R.color.icon_tint),
                modifier = Modifier
                    .padding(16.dp)
                    .size(36.dp)
                    .clickable {
                        coroutineScope.launch {
                            if (currencyLoading) return@launch
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
            viewModel.updateMonthId(month.id)
            if (currentCategory != context.getString(R.string.all) && month.getCurrentCategoryValue(
                    context,
                    currentCategory
                ) == 0f
            ) {
                viewModel.changeCategory(context.getString(R.string.all))
            }
        }

        Spacer(modifier = Modifier.size(10.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(color = colorResource(id = R.color.background))
                .verticalScroll(rememberScrollState())
        ) {
            currentMonth?.let { month ->

                Text(
                    text = month.id.toReadableDate(),
                    fontSize = 20.sp,
                    color = colorResource(id = R.color.label)
                )

                Spacer(modifier = Modifier.size(10.dp))

                if (month.totalSum() > 0.0f) {

                    CategoryTitle(title = currentCategory) {
                        viewModel.changeCategory(context.getString(R.string.all))
                        viewModel.changeList(month.toList())
                        viewModel.changeTotalSum(month.totalSum())
                    }

                    Spacer(modifier = Modifier.size(10.dp))

                    PieChart(
                        navController = navController,
                        list = currentList,
                        totalSum = totalSum,
                        allMonths = allMonths,
                        modifier = Modifier.padding(
                            bottom = 20.dp
                        ),
                        onClick = { categoryName ->

                            viewModel.changeCategory(categoryName.getTitle(context))

                            when (categoryName) {
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
                        },
                        onLongClick = { item, index ->
                            val list = currentList.toMutableList()
                            list.remove(item)
                            val changedItem = item
                            changedItem.hidden = !item.hidden
                            list.add(index, changedItem)
                            viewModel.changeList(list)
                            val changedTotalSum = if (changedItem.hidden) totalSum - item.price else totalSum + item.price
                            viewModel.changeTotalSum(changedTotalSum)
                        }
                    )
                } else {
                    Text(
                        text = stringResource(id = R.string.nothing_for_now),
                        color = colorResource(id = R.color.label),
                        fontSize = 22.sp
                    )
                }
            } ?: run {
                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text(
                        text = stringResource(id = R.string.nothing_for_now),
                        color = colorResource(id = R.color.label),
                        fontSize = 22.sp
                    )
                }
            }
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

@OptIn(ExperimentalSerializationApi::class)
@Composable
fun SpendingItem(
    navController: NavController,
    color: Color,
    name: String,
    sum: Float,
    removed: Boolean,
    total: Float,
    allMonths: List<Month>,
    highlighted: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(9f)
                .combinedClickable(
                    onClick = {
                        if (highlighted) {
                            onClick()
                        }
                    },
                    onLongClick = {
                        onLongClick()
                    }
                )
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(color = color)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = if (removed) colorResource(id = R.color.secondarylabel) else colorResource(id = R.color.label),
                            fontSize = 16.sp
                        )
                    ) {
                        append(
                            "${name.toLocalizable(LocalContext.current)} ${if (highlighted) "*" else ""} - ${sum.formatPrice()} ${
                                stringResource(
                                    id = R.string.leva
                                )
                            }"
                        )
                    }

                    if (!removed) {
                        withStyle(
                            style = SpanStyle(
                                color = colorResource(id = R.color.secondarylabel),
                                fontSize = 12.sp
                            )
                        ) {
                            append(" (${String.format("%.2f", sum.toPercent(total))} %)")
                        }
                    }
                },
                style = if (removed) TextStyle(textDecoration = TextDecoration.LineThrough) else LocalTextStyle.current
            )
        }

        if (!highlighted) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = colorResource(id = R.color.icon_tint),
                modifier = Modifier
                    .size(24.dp)
                    .weight(1f)
                    .clickable {

                        val statisticMonths = linkedMapOf<String, Float>()

                        allMonths
                            .reversed()
                            .forEach { month ->
                                getMonthValueAndColor2(month, name)?.price?.let { value ->
                                    statisticMonths[month.id] = value
                                }
                            }

                        val encodedMap = Json.encodeToString(statisticMonths)

                        navController.navigate("info_screen/$name/$encodedMap/${color.red}/${color.green}/${color.blue}")
                    }
            )
        }
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
            Row {}
        } else {
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

        Row {}
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
            .drawBehind {
                val strokeWidth = 2f
                val x = size.width - strokeWidth
                val y = size.height - strokeWidth

                //top line
                drawLine(
                    color = wash, // the color is appropriate for this case
                    start = Offset(0f, 0f), //(0,0) at top-left point of the box
                    end = Offset(x, 0f), //top-right point of the box
                    strokeWidth = strokeWidth
                )

                //bottom line
                drawLine(
                    color = wash, // the color is appropriate for this case
                    start = Offset(0f, y),// bottom-left point of the box
                    end = Offset(x, y),// bottom-right point of the box
                    strokeWidth = strokeWidth
                )
            }
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
    navController: NavController,
    list: List<SpItem>,
    totalSum: Float,
    allMonths: List<Month>,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit,
    onLongClick: (SpItem, Int) -> Unit
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

            for (result in list.reversed()) {
                if (result.hidden) { continue }
                drawArc(
                    color = result.color,
                    startAngle = 270f + anglesSum,
                    sweepAngle = result.price.toPercent(totalSum) * 3.6f,
                    useCenter = true,
                    size = Size(size.width, size.height)
                )

                anglesSum += result.price.toPercent(totalSum) * 3.6f

            }

        }

        Spacer(modifier = Modifier.size(10.dp))

        list.forEachIndexed { index, item ->

            SpendingItem(
                navController = navController,
                color = item.color,
                name = item.name,
                sum = item.price,
                removed = item.hidden,
                total = totalSum,
                allMonths = allMonths,
                highlighted = item.name == "food"
                        || item.name == "smetki"
                        || item.name == "transport"
                        || item.name == "cosmetics"
                        || item.name == "preparati"
                        || item.name == "frizior",
                onClick = {
                    onClick(item.name)
                },
                onLongClick = {
                    onLongClick(item, index)
                }
            )
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
            text = stringResource(id = R.string.total) + " ${totalSum.formatPrice()} " + stringResource(
                id = R.string.leva
            ),
            fontSize = 18.sp,
            color = colorResource(id = R.color.label)
        )
    }
}

@Composable
fun DropDownMenuCategories(
    isCategoriesExpanded: Boolean,
    currentSelectedOption: Pair<String, String>?,
    options: HashMap<String, String>,
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
    options: List<Int>,
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

@Composable
fun DropDownSelectCurrency(
    isCurrencyExpanded: Boolean,
    currentSelectedOption: String,
    options: Map<String, Double>,
    onCurrencyArrowClick: ((Boolean) -> Unit),
    onSelectCurrency: ((Pair<String, Double>) -> Unit),
    onDismiss: (() -> Unit)
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            onCurrencyArrowClick(!isCurrencyExpanded)
        }
    ) {
        Text(
            text = "$currentSelectedOption  ${currentSelectedOption.flagEmoji()}",
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
            expanded = isCurrencyExpanded,
            modifier = Modifier.background(colorResource(id = R.color.background)),
            onDismissRequest = onDismiss
        ) {
            options.forEach {
                Text(
                    text = "${it.key}  ${it.key.flagEmoji()}",
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.label),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 8.dp,
                            horizontal = 16.dp
                        )
                        .clickable {
                            onSelectCurrency(Pair(it.key, it.value))
                        }
                )
            }
        }
    }
}