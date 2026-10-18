package com.example.calculator

import android.os.Bundle
import android.text.Layout
import android.text.style.BackgroundColorSpan
import android.view.Display
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.InspectableModifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculator.ui.theme.CalculatorTheme

class MainActivity : ComponentActivity() {
    enum class Operation {
        ADD, SUB, MULT, DIV, NONE
    }
    private var display by mutableStateOf("0")
    private var currentValue = 0.0
    private var resetOnNextDigit = false
    private var currentOperation = Operation.NONE
    private var firstNum = 0.0

    private fun appendDigit(digit: String) {
        val newDisplay = if (currentValue == 0.0 || resetOnNextDigit) {
            digit
        }else {
            if (digit != "0" || display.isNotEmpty()) display + digit else display
        }
        currentValue = newDisplay.toDoubleOrNull() ?: 0.0
        display = newDisplay
        resetOnNextDigit = false
    }

    private fun clearDisplay() {
        display = "0"
        currentValue = 0.0
        resetOnNextDigit = true
    }

    private fun onClickOperate(operator: String){
        firstNum = display.toDoubleOrNull() ?: 0.0

        currentOperation = when (operator) {
            "+" -> Operation.ADD
            "-" -> Operation.SUB
            "*" -> Operation.MULT
            "/" -> Operation.DIV
            else -> Operation.NONE
        }
        clearDisplay()
    }

    private fun onEqual() {
        val secondNum = display.toDoubleOrNull() ?: 0.0

        val result = when (currentOperation){
            Operation.ADD -> firstNum + secondNum
            Operation.SUB -> firstNum - secondNum
            Operation.MULT -> firstNum * secondNum
            Operation.DIV -> firstNum / secondNum
            else -> secondNum
        }
        display = result.toString()

        currentOperation = Operation.NONE
        resetOnNextDigit = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculatorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CalculatorScreen(
                        display = display,
                        onDigitClick = {
                            when (it) {
                            "C" -> clearDisplay()
                            "+", "-", "*", "/" -> onClickOperate(it)
                            "=" -> onEqual()
                            else -> appendDigit(it)
                            }
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CalculatorTheme {
//        MainScreen()
        CalculatorScreen(
            display = "0",
            onDigitClick = {}
        )
    }
}

//---------------------------------------------

//@Composable
//fun MainScreen(modifier: Modifier = Modifier){
//    Box(
//        modifier.fillMaxSize().background(Color.Black)
//    ){
//        CalculatorScreen(display = "0", )
//    }
//}

val buttonList = listOf(
    "<-", "C", "%", "/",
    "7", "8", "9", "*",
    "4", "5", "6", "-",
    "1", "2", "3", "+",
    "0", ".", "="
)

@Composable
fun CalcButton(btn: String, onDigitClick: (String) -> Unit){
    LargeFloatingActionButton(
        modifier = Modifier.fillMaxSize().padding(3.dp),
        onClick = { onDigitClick(btn) },
        contentColor = Color.White,
        containerColor = GetColor(btn),
    ){
        Text(
            text = btn,
            fontSize = 36.sp)
    }
}

//fun GetButtonFunc(btn: String,
//                  appendDigit: (String) -> Unit,
//                  clearDisplay: () -> Unit,
//                  onClickOperate:(btn: String) -> Unit,
//                  onEquals: () -> Unit){
//    when (btn) {
//        "C" -> clearDisplay()
//        "+", "-", "*", "/" -> onClickOperate(btn)
//        "=" -> onEquals()
//        else -> appendDigit(btn)
//    }
//}

fun GetColor(btn: String) : Color{
    if (btn == "<-" ||
        btn == "C" ||
        btn == "%" ||
        btn == "/" ||
        btn == "*" ||
        btn == "-" ||
        btn == "+"){
        return Color(0xFF3EAD89)
    }
    else if (btn == "="){
        return Color(0xFF79FF12)
    }
    else{
        return Color(0xFF787878)
    }
}

@Composable
fun CalculatorScreen(
    display: String,
    onDigitClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier.fillMaxSize().background(Color.Black)
    ) {
        Column(modifier = modifier.fillMaxSize().padding(8.dp)) {
            OutlinedTextField(
                value = display,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                textStyle = TextStyle(
                    textAlign = TextAlign.End,
                    fontSize = 50.sp,
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.weight(1f))

            val btnHeight: Int = 95
            val btnPadding: Int = 4
            val btnTextSize: Int = 30
            LazyVerticalGrid(
                columns = GridCells.Fixed(count = 4),
                modifier = Modifier.fillMaxWidth().padding(10.dp)
            ) {
                items(buttonList,
                    span = { index ->
                        val zeroChar : Boolean = (index == "0")
                        if (zeroChar) { GridItemSpan(2)}
                        else {GridItemSpan(1)}
                    }
                ){
                    CalcButton(it, onDigitClick)
                }
//                var btnText: Int = 9
//                for (i in 1..19) {
//                    if (i != 17) {
//                        item {
//                            when (i) {
//                                1 -> Button(
//                                    modifier = Modifier.height(btnHeight.dp).padding(btnPadding.dp),
//                                    onClick = {},
//                                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
//                                ) { Text(text = "<-", fontSize = btnTextSize.sp) }
//
//                                2 -> Button(
//                                    modifier = Modifier.height(btnHeight.dp).padding(btnPadding.dp),
//                                    onClick = {},
//                                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
//                                ) { Text(text = "C", fontSize = btnTextSize.sp) }
//
//                                3 -> Button(
//                                    modifier = Modifier.height(btnHeight.dp).padding(btnPadding.dp),
//                                    onClick = { onDigitClick("%") },
//                                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
//                                ) { Text(text = "%", fontSize = btnTextSize.sp) }
//
//                                4 -> Button(
//                                    modifier = Modifier.height(btnHeight.dp).padding(btnPadding.dp),
//                                    onClick = { onDigitClick("/") },
//                                    colors = ButtonDefaults.buttonColors(
//                                        containerColor = Color.hsl(
//                                            166.0f,
//                                            1.0f,
//                                            0.2f
//                                        )
//                                    )
//                                ) { Text(text = "/", fontSize = btnTextSize.sp) }
//
//                                8 -> Button(
//                                    modifier = Modifier.height(btnHeight.dp).padding(btnPadding.dp),
//                                    onClick = { onDigitClick("*") },
//                                    colors = ButtonDefaults.buttonColors(
//                                        containerColor = Color.hsl(
//                                            166.0f,
//                                            1.0f,
//                                            0.2f
//                                        )
//                                    )
//                                ) { Text(text = "*", fontSize = btnTextSize.sp) }
//
//                                12 -> Button(
//                                    modifier = Modifier.height(btnHeight.dp).padding(btnPadding.dp),
//                                    onClick = { onDigitClick("-") },
//                                    colors = ButtonDefaults.buttonColors(
//                                        containerColor = Color.hsl(
//                                            166.0f,
//                                            1.0f,
//                                            0.2f
//                                        )
//                                    )
//                                ) { Text(text = "-", fontSize = btnTextSize.sp) }
//
//                                16 -> Button(
//                                    modifier = Modifier.height(btnHeight.dp).padding(btnPadding.dp),
//                                    onClick = { onDigitClick("+") },
//                                    colors = ButtonDefaults.buttonColors(
//                                        containerColor = Color.hsl(
//                                            166.0f,
//                                            1.0f,
//                                            0.2f
//                                        )
//                                    )
//                                ) { Text(text = "+", fontSize = btnTextSize.sp) }
//
//                                18 -> Button(
//                                    modifier = Modifier.height(btnHeight.dp).padding(btnPadding.dp),
//                                    onClick = { onDigitClick(".") },
//                                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
//                                ) { Text(text = ".", fontSize = btnTextSize.sp) }
//
//                                19 -> Button(
//                                    modifier = Modifier.height(btnHeight.dp).padding(btnPadding.dp),
//                                    onClick = { onDigitClick("%") },
//                                    colors = ButtonDefaults.buttonColors(
//                                        containerColor = Color.hsl(
//                                            94.0f,
//                                            0.89f,
//                                            0.58f
//                                        )
//                                    ),
//                                ) { Text(text = "=", fontSize = btnTextSize.sp) }
//
//                                else -> {
//                                    val currentNum: Int = btnText
//                                    Button(
//                                        modifier = Modifier.height(btnHeight.dp)
//                                            .padding(btnPadding.dp),
//                                        onClick = { onDigitClick("$currentNum") },
//                                        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
//                                    ) { Text(text = "$btnText", fontSize = btnTextSize.sp) }
//                                    btnText--
//                                }
//                            }
//                        }
//                    } else item(span = { GridItemSpan(currentLineSpan = 2) }) {
//                        Button(
//                            modifier = Modifier.height(btnHeight.dp).padding(btnPadding.dp),
//                            onClick = {},
//                            colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
//                        ) { Text(text = "$btnText", fontSize = btnTextSize.sp) }
//                    }
//                }
            }
        }
    }
}



//@Composable
//fun CalcBtns(onDigitClick: (String) -> Unit) {
//    val btnHeight : Int = 95
//    val btnPadding : Int = 4
//    val btnTextSize : Int = 30
//    LazyVerticalGrid(
//        columns = GridCells.Fixed(count = 4),
//        modifier = Modifier.fillMaxSize().padding(10.dp)
//    ) {
//        var btnText : Int = 9
//        for (i in 1..19) {
//            if (i != 17) {
//                item {
//                    when (i) {
//                        1 -> Button(
//                            modifier = Modifier.height(btnHeight.dp).padding(btnPadding.dp),
//                            onClick = {},
//                            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
//                        ) { Text(text = "<-", fontSize = btnTextSize.sp) }
//
//                        2 -> Button(
//                            modifier = Modifier.height(btnHeight.dp).padding(btnPadding.dp),
//                            onClick = {},
//                            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
//                        ) { Text(text = "C", fontSize = btnTextSize.sp) }
//
//                        3 -> Button(
//                            modifier = Modifier.height(btnHeight.dp).padding(btnPadding.dp),
//                            onClick = {},
//                            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
//                        ) { Text(text = "%", fontSize = btnTextSize.sp) }
//
//                        4 -> Button(
//                            modifier = Modifier.height(btnHeight.dp).padding(btnPadding.dp),
//                            onClick = {},
//                            colors = ButtonDefaults.buttonColors(containerColor = Color.hsl(166.0f, 1.0f, 0.2f))
//                        ) { Text(text = "/", fontSize = btnTextSize.sp) }
//
//                        8 -> Button(
//                            modifier = Modifier.height(btnHeight.dp).padding(btnPadding.dp),
//                            onClick = {},
//                            colors = ButtonDefaults.buttonColors(containerColor = Color.hsl(166.0f, 1.0f, 0.2f))
//                        ) { Text(text = "*", fontSize = btnTextSize.sp) }
//
//                        12 -> Button(
//                            modifier = Modifier.height(btnHeight.dp).padding(btnPadding.dp),
//                            onClick = {},
//                            colors = ButtonDefaults.buttonColors(containerColor = Color.hsl(166.0f, 1.0f, 0.2f))
//                        ) { Text(text = "-", fontSize = btnTextSize.sp) }
//
//                        16 -> Button(
//                            modifier = Modifier.height(btnHeight.dp).padding(btnPadding.dp),
//                            onClick = {},
//                            colors = ButtonDefaults.buttonColors(containerColor = Color.hsl(166.0f, 1.0f, 0.2f))
//                        ) { Text(text = "+", fontSize = btnTextSize.sp) }
//
//                        18 -> Button(
//                            modifier = Modifier.height(btnHeight.dp).padding(btnPadding.dp),
//                            onClick = {},
//                            colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
//                        ) { Text(text = ".", fontSize = btnTextSize.sp) }
//
//                        19 -> Button(
//                            modifier = Modifier.height(btnHeight.dp).padding(btnPadding.dp),
//                            onClick = {},
//                            colors = ButtonDefaults.buttonColors(containerColor = Color.hsl(94.0f, 0.89f, 0.58f)),
//                        ) { Text(text = "=", fontSize = btnTextSize.sp) }
//
//                        else -> {
//                            Button(
//                                modifier = Modifier.height(btnHeight.dp).padding(btnPadding.dp),
//                                onClick = { onDigitClick("1") },
//                                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
//                            ) { Text(text = "$btnText", fontSize = btnTextSize.sp) }
//                            btnText--
//                        }
//                    }
//                }
//            }
//            else item (span = {GridItemSpan(currentLineSpan = 2) }) {
//                Button(
//                            modifier = Modifier.height(btnHeight.dp).padding(btnPadding.dp),
//                    onClick = {},
//                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
//                ) { Text(text = "$btnText", fontSize = btnTextSize.sp) }
//            }
//        }
//    }
//}

