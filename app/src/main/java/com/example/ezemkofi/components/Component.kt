package com.example.ezemkofi.components

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.ezemkofi.R
import com.example.ezemkofi.dataclass.CoffeeDetails
import com.example.ezemkofi.ui.theme.CartItem
import com.example.ezemkofi.ui.theme.CartViewModel
import com.example.ezemkofi.ui.theme.Coffee
import com.example.ezemkofi.ui.theme.CoffeeCategory
import com.example.ezemkofi.ui.theme.CoffeeListViewModel
import com.example.ezemkofi.ui.theme.CoffeeViewModel
import com.example.ezemkofi.ui.theme.LoginViewModel
import com.example.ezemkofi.ui.theme.TokenManager
import java.util.Locale
import androidx.compose.ui.unit.sp as sp

val poppinsFontFamily = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_bold, FontWeight.Bold),
    Font(R.font.poppins_italic, FontWeight.Normal, FontStyle.Italic)
)

@Composable
fun HeaderText(text: String, value: String){
    Box(
        modifier = Modifier.fillMaxWidth()
    ){
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .absolutePadding(20.dp, 0.dp, 20.dp, 0.dp),
                text = text,
                style = TextStyle(
                    fontSize = 30.sp,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold
                ),
                color = Color.Black
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .absolutePadding(21.dp, 0.dp, 20.dp, 10.dp),
                text = value,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Medium
                ),
                color = Color.Black
            )
        }
    }
}

@Composable
fun ImageLogo(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ){
        Row(modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_green_with_icon),
                contentDescription = stringResource(id = R.string.logo),
                modifier = Modifier
                    .fillMaxWidth()
                    .size(0.dp, 35.dp)
            )
        }
    }
}

@Composable
fun FieldText(text: String){
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .absolutePadding(20.dp, 0.dp, 20.dp, 0.dp),
        style = TextStyle(
            fontSize = 14.sp,
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Medium,
        ),
        color = colorResource(id = R.color.EzemGray)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderBar(
    text: String,
    name: String,
    category: String,
    navController: NavController,
    viewModel: LoginViewModel,
    coffeeViewModel: CoffeeViewModel = viewModel()
){
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxWidth()){
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text,
                    modifier = Modifier
                        .absolutePadding(15.dp, 20.dp, 0.dp, 0.dp),
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Normal,
                    ),
                    color = Color.Black
                )

                Spacer(modifier = Modifier.weight(5f))
                Image(
                    painter = painterResource(id = R.drawable.outline_shopping_bag_24),
                    contentDescription = "Shop Bag",
                    modifier = Modifier
                        .size(45.dp)
                        .absolutePadding(0.dp, 20.dp, 15.dp, 0.dp)
                        .clickable {
                            navController.navigate("cart") {
                                popUpTo("main") { inclusive = true }
                            }
                        }
                )
            }

            Text(
                text = name,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.logout()
                        Toast
                            .makeText(context, "Logged out successfully", Toast.LENGTH_SHORT)
                            .show()
                        navController.navigate("login") {
                            popUpTo("main") { inclusive = true }
                        }
                    }
                    .absolutePadding(15.dp, 0.dp, 15.dp, 0.dp),
                style = TextStyle(
                    fontSize = 25.sp,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                ),
                color = Color.Black
            )

            var searchText by rememberSaveable { mutableStateOf("") }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp, 10.dp, 15.dp, 0.dp)
            ) {
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    query = searchText,
                    onQueryChange = {
                        searchText = it
                    },
                    onSearch = {},
                    active = false,
                    onActiveChange = {},
                    placeholder = {
                        Box(
                            contentAlignment = Alignment.CenterStart,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = "Find your perfect coffee",
                                color = colorResource(id = R.color.EzemGray),
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.Normal
                                ),
                                textAlign = TextAlign.Start,
                                modifier = Modifier.padding(start = 8.dp) // Padding to align text
                            )
                        }
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.round_search_24),
                            contentDescription = "Search Icon"
                        )
                    },
                ) {
                    // Additional content can go here if needed
                }

                // Transparent button to capture clicks
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .align(Alignment.Center)
                        .clickable {
                            navController.navigate("search")
                        }
                        .zIndex(1f)
                )
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .absolutePadding(15.dp, 20.dp, 15.dp, 0.dp),
                text = category,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                ),
                color = colorResource(id = R.color.EzemBlack)
            )

            DetailsContent(viewModel = coffeeViewModel, navController = navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesCard(
    cat: CoffeeCategory,
    isSelected: Boolean,
    onClick: () -> Unit
){
    val textColor = if(isSelected) Color.White else colorResource(id = R.color.EzemGray)
    val cardColor = if(isSelected) colorResource(id = R.color.EzemGreen) else Color.LightGray

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 0.dp, 6.dp, 0.dp),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        )
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Text(
                text = cat.name,
                style = TextStyle(
                    color = textColor,
                    fontSize = 12.sp,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}

@Composable
fun DetailsContent(
    viewModel: CoffeeViewModel = viewModel(),
    coffeeListViewModel: CoffeeListViewModel = viewModel(),
    navController: NavController
) {
    val categories by viewModel.categories.observeAsState(emptyList())
    val filteredCoffeeList by coffeeListViewModel.filteredCoffeeList.observeAsState(emptyList())
    var selectedIndex by remember { mutableStateOf(-1) }

    LaunchedEffect(Unit) {
        viewModel.fetchCategories()
        coffeeListViewModel.fetchCoffeeList()
    }

    // Cari index dari kategori "Americano"
    LaunchedEffect(categories) {
        if (categories.isNotEmpty() && selectedIndex == -1) {
            val americanoIndex = categories.indexOfFirst { it.name == "Americano" }
            if (americanoIndex != -1) {
                selectedIndex = americanoIndex
                coffeeListViewModel.filterCoffeeByCategory(categories[americanoIndex])
            }
        }
    }

    Column {
        LazyRow(
            modifier = Modifier
                .absolutePadding(15.dp, 10.dp, 15.dp, 0.dp),
        ) {
            itemsIndexed(categories) { index, category ->
                CategoriesCard(
                    cat = category,
                    isSelected = selectedIndex == index,
                    onClick = {
                        selectedIndex = index
                        coffeeListViewModel.filterCoffeeByCategory(category)
                    }
                )
            }
        }

        CoffeeList(coffeeList = filteredCoffeeList, viewModel = coffeeListViewModel, navController = navController)
    }
}

@Composable
fun CoffeeCard(
    coffeeId: String,
    value: String,
    rate: String,
    price: String,
    imageUrl: String,
    viewModel: CoffeeListViewModel,
    navController: NavController
) {
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(imageUrl) {
        viewModel.fetchImage("http://192.168.1.6:5000/images/$imageUrl") { bitmap ->
            imageBitmap = bitmap
        }
    }

    Box(modifier = Modifier
        .absolutePadding(15.dp, 80.dp, 0.dp, 0.dp)
        .clickable {
            navController.navigate("coffeeDetail/$coffeeId")
        }
    ) {
        // Card di belakang
        Card(
            modifier = Modifier
                .wrapContentHeight()
                .zIndex(0f), // Card berada di lapisan bawah
            shape = RoundedCornerShape(20.dp),
        ) {
            Column(
                modifier = Modifier
                    .width(210.dp)
                    .height(210.dp)
                    .background(color = colorResource(id = R.color.EzemGreen))
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = value,
                    style = TextStyle (
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Normal
                    ),
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(color = colorResource(id = R.color.EzemLightGreen))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Image(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(id = R.drawable.baseline_star_24),
                            contentDescription = "Rate"
                        )
                        Text(
                            text = rate,
                            style = TextStyle(
                                textAlign = TextAlign.Center,
                                fontSize = 13.sp,
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Normal,
                            ),
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                    text = "$$price",
                    style = TextStyle (
                        fontSize = 20.sp,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                    ),
                    color = Color.White
                )
            }
        }

        // Gambar di depan Card
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-80).dp) // Sesuaikan dengan kebutuhan Anda
                .zIndex(1f) // Gambar berada di lapisan atas
        ) {
            imageBitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    modifier = Modifier.size(160.dp),
                    contentDescription = "Coffee"
                )
            }
        }
    }
}

@Composable
fun CoffeeList(
    coffeeList: List<Coffee>,
    viewModel: CoffeeListViewModel,
    navController: NavController
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp, 0.dp, 15.dp, 0.dp)
    ) {
        items(coffeeList) { coffee ->
            CoffeeCard(
                coffeeId = coffee.id,
                value = coffee.name,
                rate = coffee.rating,
                price = coffee.price,
                imageUrl = coffee.imagePath,
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}

@Composable
fun HeaderText(value: String){
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .absolutePadding(15.dp, 20.dp, 15.dp, 0.dp),
        text = value,
        style = TextStyle(
            fontSize = 18.sp,
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.SemiBold,
        ),
        color = Color.Black
    )
}

@Composable
fun CoffeeTopPicks(
    rate: String,
    name: String,
    type: String,
    price: String,
    imageUrl: String,
    viewModel: CoffeeListViewModel
){
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(imageUrl) {
        viewModel.fetchImage("http://192.168.1.6:5000/images/$imageUrl") { bitmap ->
            imageBitmap = bitmap
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ){
        Row(Modifier.fillMaxWidth()
        ) {
            Column {
                Box {
                    imageBitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            modifier = Modifier.size(100.dp),
                            contentDescription = "Coffee"
                        )
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = 16.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(color = colorResource(id = R.color.EzemGreen))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Image(
                                modifier = Modifier.size(16.dp),
                                painter = painterResource(id = R.drawable.baseline_star_24),
                                contentDescription = "Rate"
                            )
                            Text(
                                text = rate,
                                style = TextStyle(
                                    fontSize = 13.sp,
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.Normal,
                                ),
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp)
                    .align(Alignment.CenterVertically),
            ) {
                Text(
                    text = name,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                    ),
                    color = colorResource(id = R.color.EzemBlack)
                )
                Text(
                    modifier = Modifier.padding(bottom = 20.dp),
                    text = type,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                    ),
                    color = colorResource(id = R.color.EzemGray)
                )
                Text(
                    text = "$$price",
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                    ),
                    color = colorResource(id = R.color.EzemBlack)
                )
            }
        }
    }
}

@Composable
fun CoffeeTopPicksSection(viewModel: CoffeeListViewModel = viewModel()) {
    val topPicks by viewModel.topPicks.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.fetchTopPicks()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp, top = 8.dp, start = 16.dp, end = 16.dp)
    ) {
        items(topPicks) { coffee ->
            CoffeeTopPicks(
                rate = coffee.rating,
                name = coffee.name,
                type = coffee.category,
                price = coffee.price,
                imageUrl = coffee.imagePath,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun CoffeeSearch(
    context: Context,
    viewModel: CoffeeListViewModel = viewModel(),
    navController: NavHostController
) {
    val filteredCoffeeList by viewModel.filteredCoffeeListByName.observeAsState(emptyList())
    val coffeeList by viewModel.coffeeList.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.fetchCoffeeList()
    }

    var query by rememberSaveable { mutableStateOf("") }

    Column {
        HeaderSearchBar(
            context = context,
            navController = navController,
            onSearchQueryChange = {
                query = it
                viewModel.searchCoffee(it)
            }
        )

        HeaderText(value = stringResource(id = R.string.searchResult))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            val displayList = if (query.isEmpty()) coffeeList else filteredCoffeeList

            items(displayList) { coffee ->
                CoffeeTopPicks(
                    rate = coffee.rating,
                    name = coffee.name,
                    type = coffee.category,
                    price = coffee.price,
                    imageUrl = coffee.imagePath,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun CoffeeCart(
    id: Int,
    value: Int,
    size: String,
    name: String,
    type: String,
    unitPrice: String,
    imagePath: String,
    cartViewModel: CartViewModel
) {
    var quantity by remember { mutableStateOf(value) }
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val totalPrice = unitPrice.toDouble() * quantity

    LaunchedEffect(imagePath) {
        cartViewModel.fetchImage("http://192.168.1.6:5000/images/$imagePath") { bitmap ->
            imageBitmap = bitmap
        }
    }

    // Update cart when quantity changes
    LaunchedEffect(quantity) {
        if (quantity > 0) {
            val updatedItem = CartItem(
                id = id,
                name = name,
                size = size,
                category = type,
                quantity = quantity,
                unitPrice = unitPrice,
                price = totalPrice.toString(),
                imagePath = imagePath
            )
            cartViewModel.addOrUpdateItemQuantity(quantity, updatedItem)
        } else {
            cartViewModel.removeItem(name, size)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Box {
                    imageBitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "CoffeeTop",
                            modifier = Modifier.size(100.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(start = 10.dp)
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 2.dp),
                    text = name,
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                    ),
                    color = colorResource(id = R.color.EzemBlack)
                )
                Text(
                    modifier = Modifier.padding(bottom = 15.dp),
                    text = type,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                    ),
                    color = colorResource(id = R.color.EzemGray)
                )
                Text(
                    modifier = Modifier.padding(bottom = 10.dp),
                    text = "Size: $size",
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Normal,
                    ),
                    color = colorResource(id = R.color.EzemBlack)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(20.dp))
                        .background(color = colorResource(id = R.color.white))
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    if (quantity > 0) quantity -= 1
                                },
                            painter = painterResource(id = R.drawable.baseline_horizontal_rule_24),
                            contentDescription = "minus"
                        )

                        Text(
                            modifier = Modifier.padding(start = 13.dp, end = 13.dp),
                            text = quantity.toString()
                        )

                        Image(
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    quantity += 1
                                },
                            painter = painterResource(id = R.drawable.baseline_add_24),
                            contentDescription = "Add"
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(start = 5.dp, bottom = 5.dp) // Ensure Column takes full height of Row
                    .align(Alignment.Bottom) // Align this column to the bottom of the Row
            ) {
                Text(
                    text = "$${String.format(Locale.US, "%.2f", totalPrice)}",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                    ),
                    color = colorResource(id = R.color.EzemBlack),
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }
    }
}

@Composable
fun Textfield(text: String, value: String, onUsernameChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onUsernameChange,
        placeholder = { Text(text = text, fontFamily = poppinsFontFamily, fontWeight = FontWeight.Normal, fontSize = 13.sp) },
        modifier = Modifier
            .absolutePadding(20.dp, 0.dp, 20.dp, 15.dp)
            .fillMaxWidth(),
        textStyle = TextStyle(
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp
        )
    )
}

@Composable
fun EmailTextField(email: String, onEmailChange: (String) -> Unit) {
    TextField(
        value = email,
        onValueChange = onEmailChange,
        placeholder = { Text(text = "Email", fontFamily = poppinsFontFamily, fontWeight = FontWeight.Normal, fontSize = 13.sp)},
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        textStyle = TextStyle(
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp
        ),
        modifier = Modifier
            .absolutePadding(20.dp, 0.dp, 20.dp, 15.dp)
            .fillMaxWidth()
    )
}

@Composable
fun PasswordTextField(password: String, onPasswordChange: (String) -> Unit) {
    TextField(
        value = password,
        onValueChange = onPasswordChange,
        placeholder = { Text(text = "Password", fontFamily = poppinsFontFamily, fontWeight = FontWeight.Normal, fontSize = 13.sp)},
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = PasswordVisualTransformation(),
        textStyle = TextStyle(
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp
        ),
        modifier = Modifier
            .absolutePadding(20.dp, 0.dp, 20.dp, 15.dp)
            .fillMaxWidth()
    )
}

@Composable
fun LoginOrSignButton(value: String, onClick: () -> Unit){
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(50.dp),
        modifier = Modifier
            .absolutePadding(20.dp, 0.dp, 20.dp, 0.dp)
            .fillMaxWidth()
            .height(50.dp),
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.EzemGreen))
    ) {
        Text(
            fontSize = 14.sp,
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Bold,
            text = value,
            color = Color.White,
            letterSpacing = 1.5.sp
        )
    }
}

@Composable
fun ClickAbleLoginText(value1: String, value2: String, onClick: () -> Unit){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .absolutePadding(20.dp, 20.dp, 20.dp, 0.dp),
        contentAlignment = Alignment.Center,
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color.Gray,
                            fontSize = 13.sp,
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Normal
                        )
                    ) {
                        append(value1)
                    }
                },
            )

            ClickableText(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = colorResource(id = R.color.EzemGreen),
                            fontSize = 13.sp,
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Medium
                        )
                    ) {
                        append(" ")
                        append(value2)
                    }
                },
                onClick = { offset ->
                    onClick()
                }
            )
        }
    }
}

@Composable
fun CartBar(
    value: String,
    navController: NavController,
    sharedPreferences: SharedPreferences
){
    val fullName = remember { sharedPreferences.getString("fullName", "Guest") ?: "Guest" }

    Box(modifier = Modifier.fillMaxWidth()){
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier
                    .size(38.dp)
                    .clickable {
                        navController.navigate("main/$fullName") {
                            popUpTo("cart") { inclusive = true }
                        }
                    },
                painter = painterResource(id = R.drawable.outline_arrow_circle_left_24),
                contentDescription = "back"
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = value,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderSearchBar(
    context: Context,
    navController: NavController,
    onSearchQueryChange: (String) -> Unit
) {
    val tokenManager = TokenManager(context)
    val fullName = tokenManager.getFullName()
    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 20.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        navController.navigate("main/$fullName") {
                            popUpTo("search") { inclusive = true }
                        }
                    },
                painter = painterResource(id = R.drawable.outline_arrow_circle_left_24),
                contentDescription = "back"
            )

            Spacer(modifier = Modifier.width(15.dp))

            var searchText by rememberSaveable { mutableStateOf("") }

            SearchBar(
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                query = searchText,
                onQueryChange = { newText ->
                    searchText = newText
                    onSearchQueryChange(newText)
                },
                onSearch = { },
                active = false,
                onActiveChange = {},
                placeholder = {
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "Find your perfect coffee",
                            color = colorResource(id = R.color.EzemGray),
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Normal
                            ),
                            textAlign = TextAlign.Start,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                },
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.round_search_24),
                        contentDescription = "Search Icon"
                    )
                }
            ) {
                // Additional content can go here if needed
            }
        }
    }
}

@Composable
fun CoffeeDetail(
    text: String,
    coffee: CoffeeDetails,
    viewModel: CoffeeListViewModel,
    cartViewModel: CartViewModel,
    navController: NavController,
    context: Context
) {
    val tokenManager = TokenManager(context)
    val fullName = tokenManager.getFullName()

    val selectedIcon = remember { mutableStateOf("M") }
    val quantity = remember { mutableStateOf(1) }

    // Animasi rotasi dan ukuran
    var rotationAngle by remember { mutableStateOf(0f) }
    val rotation by animateFloatAsState(
        targetValue = rotationAngle,
        animationSpec = tween(
            durationMillis = 500, // Duration in milliseconds
            easing = LinearEasing // Easing function
        )
    )

    val imageSize by animateDpAsState(
        targetValue = when (selectedIcon.value) {
            "S" -> 300.dp
            "M" -> 330.dp
            "L" -> 350.dp
            else -> 330.dp
        }
    )

    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val coffeePrice: Double = coffee.price.toDoubleOrNull() ?: 0.0

    LaunchedEffect(coffee.imagePath) {
        viewModel.fetchImage("http://192.168.1.6:5000/images/${coffee.imagePath}") { bitmap ->
            imageBitmap = bitmap
        }
    }

    LaunchedEffect(selectedIcon.value) {
        rotationAngle += 360f
    }

    // Function to calculate price based on selected size
    fun calculatePrice(): Double {
        return when (selectedIcon.value) {
            "S" -> coffeePrice * 0.85
            "M" -> coffeePrice
            "L" -> coffeePrice * 1.15
            else -> coffeePrice
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize() // Background color for the entire screen
    ) {
        // Background Shape
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(330.dp) // Cover part of the screen
                .background(
                    color = colorResource(id = R.color.EzemGreen), // Green color
                    shape = RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)
                )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()// Enable vertical scrolling
        ) {
            // Header
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp, top = 15.dp, bottom = 10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier
                            .size(38.dp)
                            .clickable {
                                navController.navigate("main/$fullName") {
                                    popUpTo("coffeeDetail/${coffee.id}") { inclusive = true }
                                }
                            },
                        painter = painterResource(id = R.drawable.round_arrow_circle_left_24),
                        contentDescription = "back"
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Details", // Hardcoded for preview
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                }
            }

            // Image with overlay and size indicators
            Box(
                modifier = Modifier
                    .fillMaxWidth(), // Adjust top padding to make space for header
                contentAlignment = Alignment.BottomCenter
            ) {
                imageBitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "coffee",
                        modifier = Modifier
                            .size(imageSize)// Adjust image size as needed
                            .graphicsLayer(
                                rotationZ = rotation
                            )
                    )
                }
            }
        }

        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp, bottom = 15.dp)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = coffee.name,
                        fontSize = 25.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = colorResource(id = R.color.black),
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        maxLines = 3,
                        text = coffee.description,
                        color = colorResource(id = R.color.EzemLightGray),
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp,
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp, bottom = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "$${String.format(Locale.US,"%.2f", calculatePrice())}",
                            color = colorResource(id = R.color.EzemBlack),
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .border(1.dp, Color.Gray, RoundedCornerShape(20.dp))
                                .background(color = colorResource(id = R.color.white))
                                .padding(horizontal = 10.dp, vertical = 10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                Image(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clickable { if (quantity.value > 1) quantity.value -= 1 },
                                    painter = painterResource(id = R.drawable.baseline_horizontal_rule_24),
                                    contentDescription = "minus"
                                )

                                Text(
                                    modifier = Modifier.padding(start = 13.dp, end = 13.dp),
                                    text = quantity.value.toString()
                                )

                                Image(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clickable { quantity.value += 1 },
                                    painter = painterResource(id = R.drawable.baseline_add_24),
                                    contentDescription = "Add"
                                )
                            }
                        }
                    }

                    Button(
                        onClick = {
                            val quantityString = quantity.value
                            val unitPrice = String.format(Locale.US, "%.2f", calculatePrice())
                            val priceString = String.format(Locale.US, "%.2f", calculatePrice() * quantity.value)

                            val item = CartItem(
                                id = coffee.id.toInt(),
                                name = coffee.name,
                                size = selectedIcon.value,
                                category = coffee.category,
                                quantity = quantityString,
                                unitPrice = unitPrice,
                                price = priceString,
                                imagePath = coffee.imagePath
                            )
                            cartViewModel.addOrUpdateItem(quantityString, item)
                            navController.navigate("cart")
                        },
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        contentPadding = PaddingValues(),
                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.EzemGreen))
                    ) {
                        Text(
                            fontSize = 14.sp,
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            text = text,
                            color = Color.White,
                            letterSpacing = 1.5.sp
                        )
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(top = 360.dp)) {
            // Image with overlay and size indicators
            Box(
                modifier = Modifier
                    .fillMaxWidth(), // Adjust top padding to make space for header
                contentAlignment = Alignment.Center
            ) {
                // Round icon for rating
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = (-10).dp, y = (-60).dp) // Adjust offset as needed
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(color = colorResource(id = R.color.orange)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = coffee.rating, // Replace with actual rating
                        fontSize = 18.sp,
                        color = Color.White,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Size indicators
                IconWithOutline(
                    text = "S",
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(x = 35.dp, y = (-10).dp)
                        .clickable { selectedIcon.value = "S" },
                    isSelected = selectedIcon.value == "S",
                    rotationDegrees = 45f
                )
                IconWithOutline(
                    text = "M",
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(x = 0.dp, y = 40.dp)
                        .clickable { selectedIcon.value = "M" },
                    isSelected = selectedIcon.value == "M"
                )
                IconWithOutline(
                    text = "L",
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = (-35).dp, y = (-10).dp)
                        .clickable { selectedIcon.value = "L" },
                    isSelected = selectedIcon.value == "L",
                    rotationDegrees = (-45f)
                )
            }
        }
    }
}

@Composable
fun IconWithOutline(
    text: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    rotationDegrees: Float = 0f
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(if (isSelected) colorResource(id = R.color.EzemGreen) else Color.White)
            .rotate(rotationDegrees)
            .border(2.dp, color = colorResource(id = R.color.EzemGreen), CircleShape), // Outline
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 15.sp,
            color = if(isSelected) Color.White else colorResource(id = R.color.EzemGreen),
            fontWeight = FontWeight.Bold
        )
    }
}
