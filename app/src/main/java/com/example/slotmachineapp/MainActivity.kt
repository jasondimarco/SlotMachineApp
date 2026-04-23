package com.example.slotmachineapp

import android.os.Bundle
import android.service.autofill.Validators.and
import android.service.autofill.Validators.or
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.slotmachineapp.ui.theme.SlotMachineAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SlotMachineAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Slots(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Slots(modifier: Modifier = Modifier) {
    var count1 by remember { mutableIntStateOf(0) }
    var count2 by remember { mutableIntStateOf(1) }
    var count3 by remember { mutableIntStateOf(2) }
    var isCounting by remember { mutableStateOf(3) }
    var countJob1: Job? by remember { mutableStateOf(null) }
    var countJob2: Job? by remember { mutableStateOf(null) }
    var countJob3: Job? by remember { mutableStateOf(null) }
    var speed by remember { mutableFloatStateOf(200f) }
    var upOrDown1 by remember { mutableStateOf(true) }
    var upOrDown2 by remember { mutableStateOf(true) }
    var upOrDown3 by remember { mutableStateOf(true) }
    var loser by remember { mutableStateOf(false) }
    var win by remember { mutableStateOf(0) }
    val coroutine = rememberCoroutineScope()
    val fruit = listOf(
        R.drawable.cherry,
        R.drawable.grape,
        R.drawable.pear,
        R.drawable.strawberry
    )
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.LightGray,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Slots!",
                        fontSize = 60.sp
                    )
//                },
//                actions = {
//                    IconButton(
//                        onClick = { count = 0 }
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Refresh,
//                            contentDescription = "Zero"
//                        )
//                    }
//                    IconButton(
//                        onClick = {
//                            if (upOrDown) {
//                                upOrDown = false
//                            } else {
//                                upOrDown = true
//                            }
//                        }
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Build,
//                            contentDescription = "Count Down"
//                        )
//                    }
                }
            )
        }
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = modifier.fillMaxSize()
        ) {
            Row(modifier = Modifier.padding(top = 200.dp)) {
                Image(
                    painter = painterResource(id = fruit[count1]),
                    contentDescription = "fruitSlot1",
                    modifier = Modifier.size(100.dp)
                )
                Image(
                    painter = painterResource(id = fruit[count2]),
                    contentDescription = "fruitSlot2",
                    modifier = Modifier.size(100.dp)
                )
                Image(
                    painter = painterResource(id = fruit[count3]),
                    contentDescription = "fruitSlot3",
                    modifier = Modifier.size(100.dp)
                )
            }
            if (loser) {
                if (count1 == count2) {
                    if (count1 == count3) {
                        Text(
                            text = "You're a winner!",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline,
                            color = Color.Yellow,
                            style = TextStyle(
                                shadow = Shadow(
                                    color = Color.Black,
                                    offset = Offset(0f, 0f),
                                    blurRadius = 10f
                                )
                            )
                        )
                    }
                } else {
                    Text(
                        text = "You Lost haha",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Slider(
                    value = speed,
                    onValueChange = { speed = it },
                    valueRange = 10f..200f,
                    modifier = Modifier.padding(horizontal = 80.dp)
                )
                val markiplier = (((200 - speed.toFloat())/10)/2) + 1
                Text(
                    text = "${"%.1f".format(markiplier)}x Multiplier"
                )
            }
            if (isCounting < 3) {
                Button(
                    onClick = {
                        if (isCounting == 0) {
                            countJob1?.cancel()
                            isCounting = 1
                        } else if (isCounting == 1) {
                            countJob2?.cancel()
                            isCounting = 2
                        } else if (isCounting == 2) {
                            countJob3?.cancel()
                            isCounting = 3
                            loser = true
                        }
                    },
                    modifier = Modifier.padding(bottom = 80.dp)
                ) {
                    Text("Feeling Lucky?")
                }
            } else {
                Button(
                    onClick = {
                        loser = false
                        isCounting = 0
                        countJob1 = coroutine.launch(Dispatchers.Default) {
                            while (true) {
                                delay(speed.toLong())
                                if (upOrDown1)
                                    count1++
                                else
                                    count1--
                                if (count1 == 3)
                                    upOrDown1 = false
                                if (count1 == 0)
                                    upOrDown1 = true
                            }
                        }
                        countJob2 = coroutine.launch(Dispatchers.Default) {
                            while (true) {
                                delay(speed.toLong()+10)
                                if (upOrDown2)
                                    count2++
                                else
                                    count2--
                                if (count2 == 3)
                                    upOrDown2 = false
                                if (count2 == 0)
                                    upOrDown2 = true
                            }
                        }
                        countJob3 = coroutine.launch(Dispatchers.Default) {
                            while (true) {
                                delay(speed.toLong()+20)
                                if (upOrDown3)
                                    count3++
                                else
                                    count3--
                                if (count3 == 3)
                                    upOrDown3 = false
                                if (count3 == 0)
                                    upOrDown3 = true
                            }

                        }
                    },
                    modifier = Modifier.padding(bottom = 80.dp)
                ) {
                    Text("Take a spin!")
                }
            }
        }
    }
}